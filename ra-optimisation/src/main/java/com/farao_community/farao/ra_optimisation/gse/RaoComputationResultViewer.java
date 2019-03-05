/**
 * Copyright (c) 2018-2019, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.ContingencyResult;
import com.farao_community.farao.ra_optimisation.MonitoredBranchResult;
import com.farao_community.farao.ra_optimisation.PreContingencyResult;
import com.farao_community.farao.ra_optimisation.RaoComputationResult;
import com.farao_community.farao.ra_optimisation.afs.RaoComputationRunner;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class RaoComputationResultViewer implements ProjectFileViewer, Initializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.RaoComputationResultViewer");
    private final GseContext context;
    private RaoComputationRunner runner;

    public final class ContingencyMonitoredBranch {
        private final String id;
        private final String name;
        private final String branchId;
        private final Double preOptimisationFlow;
        private final Double preOptimisationOverload;
        private final Double postOptimisationFlow;
        private final Double postOptimisationOverload;
        private final Double maximumFlow;

        ContingencyMonitoredBranch(ContingencyResult co) {
            this.id = co.getId();
            this.name = co.getName();
            this.branchId = null;
            this.preOptimisationFlow = null;
            this.preOptimisationOverload = null;
            this.postOptimisationFlow = null;
            this.postOptimisationOverload = null;
            this.maximumFlow = null;
        }

        ContingencyMonitoredBranch() {
            this.id = "Results";
            this.name = null;
            this.branchId = null;
            this.preOptimisationFlow = null;
            this.preOptimisationOverload = null;
            this.postOptimisationFlow = null;
            this.postOptimisationOverload = null;
            this.maximumFlow = null;
        }

        ContingencyMonitoredBranch(MonitoredBranchResult cb) {
            this.id = cb.getId();
            this.name = cb.getName();
            this.branchId = cb.getBranchId();
            this.preOptimisationFlow = cb.getPreOptimisationFlow();
            this.preOptimisationOverload = Math.abs(cb.getPreOptimisationFlow() / cb.getMaximumFlow());
            this.postOptimisationFlow = cb.getPostOptimisationFlow();
            this.postOptimisationOverload = Math.abs(cb.getPostOptimisationFlow() / cb.getMaximumFlow());
            this.maximumFlow = cb.getMaximumFlow();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getBranchId() {
            return branchId;
        }

        public Double getPreOptimisationFlow() {
            return preOptimisationFlow;
        }

        public Double getPreOptimisationOverload() {
            return preOptimisationOverload;
        }

        public Double getPostOptimisationFlow() {
            return postOptimisationFlow;
        }

        public Double getPostOptimisationOverload() {
            return postOptimisationOverload;
        }

        public Double getMaximumFlow() {
            return maximumFlow;
        }
    }

    @FXML
    private SplitPane overviewSplitPane;

    @FXML
    private TableView<MonitoredBranchResult> monitoredBranchResultsTable;
    @FXML
    private TableColumn<MonitoredBranchResult, String> idTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, String> nameTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, String> branchIdTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, Number> preOptimisationFlowTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, Number> preOptimisationOverloadTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, Number> postOptimisationFlowTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, Number> postOptimisationOverloadTableColumn;
    @FXML
    private TableColumn<MonitoredBranchResult, Number> maximumFlowTableColumn;

    @FXML
    private TreeTableView<ContingencyMonitoredBranch> cbcoTreeTable;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, String> idAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, String> nameAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, String> branchIdAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, Number> preOptimisationFlowAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, Number> preOptimisationOverloadAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, Number> postOptimisationFlowAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, Number> postOptimisationOverloadAfterCoTableColumn;
    @FXML
    private TreeTableColumn<ContingencyMonitoredBranch, Number> maximumFlowTableAfterCoColumn;

    RaoComputationResultViewer(RaoComputationRunner raoComputationRunner, GseContext gseContext) {
        this.runner = raoComputationRunner;
        this.context = gseContext;
    }

    @Override
    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(RaoComputationResultViewer.class.getResource("/fxml/RaoComputationResultViewer.fxml"));
        fxmlLoader.setResources(RESOURCE_BUNDLE);
        fxmlLoader.setController(this);
        Node content;
        try {
            content = fxmlLoader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return content;
    }

    private void viewPreContingency(RaoComputationResult result) {
        // Double cell factory
        Callback<TableColumn<MonitoredBranchResult, Number>, TableCell<MonitoredBranchResult, Number>> cellFactory = tc -> new TableCell<MonitoredBranchResult, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getInstance();
                    format.setMaximumFractionDigits(1);
                    setText(format.format(value));
                }
            }
        };
        Callback<TableColumn<MonitoredBranchResult, Number>, TableCell<MonitoredBranchResult, Number>> cellPercentFactory = tc -> new TableCell<MonitoredBranchResult, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getPercentInstance();
                    format.setMaximumFractionDigits(1);
                    setText(format.format(value));
                }
            }
        };

        GenRedispatchResultTableView genRedispatchResultTableView = new GenRedispatchResultTableView(context);
        overviewSplitPane.getItems().add(genRedispatchResultTableView.getContent());
        genRedispatchResultTableView.selectResults(result.getPreContingencyResult().getRemedialActionResults());

        PstElementResultTableView pstElementResultTableView = new PstElementResultTableView(context);
        overviewSplitPane.getItems().add(pstElementResultTableView.getContent());
        pstElementResultTableView.selectResults(result.getPreContingencyResult().getRemedialActionResults());

        // Pre contingency
        PreContingencyResult preContingencyResult = result.getPreContingencyResult();
        MonitoredBranchResult[] details = preContingencyResult.getMonitoredBranchResults().toArray(new MonitoredBranchResult[0]);
        monitoredBranchResultsTable.setItems(FXCollections.observableArrayList(details));

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        branchIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        preOptimisationFlowTableColumn.setCellValueFactory(new PropertyValueFactory<>("preOptimisationFlow"));
        preOptimisationFlowTableColumn.setCellFactory(cellFactory);
        preOptimisationOverloadTableColumn.setCellValueFactory(cellDataFeatures -> new SimpleDoubleProperty(getPreOptimisationOverload(cellDataFeatures.getValue())));
        preOptimisationOverloadTableColumn.setCellFactory(cellPercentFactory);
        postOptimisationFlowTableColumn.setCellValueFactory(new PropertyValueFactory<>("postOptimisationFlow"));
        postOptimisationFlowTableColumn.setCellFactory(cellFactory);
        postOptimisationOverloadTableColumn.setCellValueFactory(cellDataFeatures -> new SimpleDoubleProperty(getPostOptimisationOverload(cellDataFeatures.getValue())));
        postOptimisationOverloadTableColumn.setCellFactory(cellPercentFactory);
        maximumFlowTableColumn.setCellValueFactory(new PropertyValueFactory<>("maximumFlow"));
        maximumFlowTableColumn.setCellFactory(cellFactory);
    }

    private void viewPostContingency(RaoComputationResult result) {
        // Double cell factory
        Callback<TreeTableColumn<ContingencyMonitoredBranch, Number>, TreeTableCell<ContingencyMonitoredBranch, Number>> tableCellFactory = tc -> new TreeTableCell<ContingencyMonitoredBranch, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getInstance();
                    format.setMaximumFractionDigits(1);
                    setText(format.format(value));
                }
            }
        };
        Callback<TreeTableColumn<ContingencyMonitoredBranch, Number>, TreeTableCell<ContingencyMonitoredBranch, Number>> tableCellPercentFactory = tc -> new TreeTableCell<ContingencyMonitoredBranch, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getPercentInstance();
                    format.setMaximumFractionDigits(1);
                    setText(format.format(value));
                }
            }
        };

        // PostContingencies
        List<ContingencyResult> contingencyResults = result.getContingencyResults();
        cbcoTreeTable.setRoot(new TreeItem<>(new ContingencyMonitoredBranch()));
        cbcoTreeTable.getRoot().getChildren().addAll(
                contingencyResults.stream().map(contingencyResult -> {
                    TreeItem<ContingencyMonitoredBranch> contingencyItem = new TreeItem<>(new ContingencyMonitoredBranch(contingencyResult));
                    contingencyItem.getChildren().addAll(
                            contingencyResult.getMonitoredBranchResults().stream().map(monitoredBranchResult -> new TreeItem<>(new ContingencyMonitoredBranch(monitoredBranchResult))).collect(Collectors.toList())
                    );
                    return contingencyItem;
                }).collect(Collectors.toList())
        );
        idAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        nameAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        branchIdAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("branchId"));
        preOptimisationFlowAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("preOptimisationFlow"));
        preOptimisationFlowAfterCoTableColumn.setCellFactory(tableCellFactory);
        preOptimisationOverloadAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("preOptimisationOverload"));
        preOptimisationOverloadAfterCoTableColumn.setCellFactory(tableCellPercentFactory);
        postOptimisationFlowAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("postOptimisationFlow"));
        postOptimisationFlowAfterCoTableColumn.setCellFactory(tableCellFactory);
        postOptimisationOverloadAfterCoTableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("postOptimisationOverload"));
        postOptimisationOverloadAfterCoTableColumn.setCellFactory(tableCellPercentFactory);
        maximumFlowTableAfterCoColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("maximumFlow"));
        maximumFlowTableAfterCoColumn.setCellFactory(tableCellFactory);
    }

    @Override
    public void view() {
        RaoComputationResult result = runner.readResult();

        viewPreContingency(result);
        viewPostContingency(result);
    }

    private double getPreOptimisationOverload(MonitoredBranchResult value) {
        return Math.abs(value.getPreOptimisationFlow() / value.getMaximumFlow());
    }

    private double getPostOptimisationOverload(MonitoredBranchResult value) {
        return Math.abs(value.getPostOptimisationFlow() / value.getMaximumFlow());
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isClosable() {
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
