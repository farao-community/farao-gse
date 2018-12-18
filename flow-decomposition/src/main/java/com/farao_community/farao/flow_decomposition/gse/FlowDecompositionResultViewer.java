/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.google.common.collect.Table;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import com.powsybl.gse.util.GseUtil;
import com.farao_community.farao.data.flow_decomposition_results.FlowDecompositionResults;
import com.farao_community.farao.data.flow_decomposition_results.PerBranchResult;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Flow decomposition results viewer
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
public class FlowDecompositionResultViewer implements ProjectFileViewer {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowDecompositionGse");

    private final FlowDecompositionRunner runner;

    private final Scene scene;

    private final GseContext context;

    @FXML private Node root;
    @FXML private ListView branchesList;
    @FXML private PieChart pieChart;
    @FXML private BarChart loopFlows;
    @FXML private TableView<String> flowTable;

    private final Service<FlowDecompositionResults> resultsLoadingService;

    public FlowDecompositionResultViewer(FlowDecompositionRunner runner, Scene scene, GseContext context) {
        this.runner = Objects.requireNonNull(runner);
        this.scene = Objects.requireNonNull(scene);
        this.context = Objects.requireNonNull(context);

        resultsLoadingService = GseUtil.createService(new Task<FlowDecompositionResults>() {
            @Override
            protected FlowDecompositionResults call() throws Exception {
                return runner.readResult();
            }
        }, context.getExecutor());
    }

    @Override
    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(FlowDecompositionResultViewer.class.getResource("/fxml/FlowDecompositionResultViewer.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("lang.FlowDecompositionGse"));
        fxmlLoader.setController(this);
        Node content = null;
        try {
            content = fxmlLoader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return content;
    }

    @Override
    public void view() {
        resultsLoadingService.setOnSucceeded(event -> {
            FlowDecompositionResults result = (FlowDecompositionResults) event.getSource().getValue();
            if (result != null) {
                fillWithResults(result);
            }
        });
        resultsLoadingService.start();
    }

    @Override
    public void dispose() {
        // nothing to dispose
    }

    @Override
    public boolean isClosable() {
        return true;
    }

    public void fillWithResults(FlowDecompositionResults results) {
        ObservableList<String> items = FXCollections.observableArrayList(results.getPerBranchResults().keySet().stream().sorted().collect(Collectors.toList()));
        branchesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectBranch((String) newValue, results));
        branchesList.setItems(items);

        // Select first branch
        branchesList.getSelectionModel().selectFirst();
    }

    public void selectBranch(String branchName, FlowDecompositionResults results) {
        // Deal with PieChart
        PerBranchResult perBranchResult = results.getPerBranchResult(branchName);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(RESOURCE_BUNDLE.getString("InternalFlows"), perBranchResult.getTotalInternalFlows()),
                new PieChart.Data(RESOURCE_BUNDLE.getString("LoopFlows"), perBranchResult.getTotalLoopFlows()),
                new PieChart.Data(RESOURCE_BUNDLE.getString("ImportFlows"), perBranchResult.getTotalImportFlows()),
                new PieChart.Data(RESOURCE_BUNDLE.getString("ExportFlows"), perBranchResult.getTotalExportFlows()),
                new PieChart.Data(RESOURCE_BUNDLE.getString("TransitFlows"), perBranchResult.getTotalTransitFlows()),
                new PieChart.Data(RESOURCE_BUNDLE.getString("PstFlows"), perBranchResult.getTotalPstFlows())
        );
        pieChart.setData(pieChartData);

        // Deal with BarChart
        ObservableList<XYChart.Series> barChartSeries = FXCollections.observableArrayList(
                results.getPerBranchResult(branchName).getCountryExchangeFlows().cellSet().stream()
                        .filter(cell -> cell.getColumnKey().equals(cell.getRowKey()))
                        .map(cell -> new XYChart.Series(cell.getColumnKey(), FXCollections.observableArrayList(new XYChart.Data(RESOURCE_BUNDLE.getString("LoopFlows"), cell.getValue())))).collect(Collectors.toList())
        );
        loopFlows.getData().setAll(barChartSeries);

        // Reset flowTable
        flowTable.getColumns().clear();
        Table<String, String, Double> exchangeTable = results.getPerBranchResult(branchName).getCountryExchangeFlows();
        flowTable.setItems(GuavaTableConverter.getItems(exchangeTable));
        flowTable.getColumns().add(GuavaTableConverter.getRowColumn(exchangeTable, RESOURCE_BUNDLE.getString("Country"), Object::toString));
        flowTable.getColumns().addAll(GuavaTableConverter.getColumns(exchangeTable, Object::toString, d -> String.format("%.2f", d)));
        flowTable.getColumns().forEach(tc -> {
            tc.setSortable(false);
            tc.impl_setReorderable(false);
        });
    }
}
