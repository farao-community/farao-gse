/**
 * Copyright (c) 2019, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.PstElementResult;
import com.farao_community.farao.ra_optimisation.RemedialActionResult;
import com.powsybl.gse.spi.GseContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class PstElementResultTableView extends TableView<PstElementResult> implements Initializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.RaoComputationResultViewer");

    @FXML
    private TableView<PstElementResult> pstElementTable;

    @FXML
    private TableColumn<PstElementResult, String> idTabColumn;

    @FXML
    private TableColumn<PstElementResult, Number> preOptimisationAngleTabColumn;

    @FXML
    private TableColumn<PstElementResult, Integer> preOptimisationTapPositionTabColumn;

    @FXML
    private TableColumn<PstElementResult, Number> postOptimisationAngleTabColumn;

    @FXML
    private TableColumn<PstElementResult, Number> postOptimisationTapPositionTabColumn;

    public PstElementResultTableView(GseContext gseContext) {
    }

    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(GenRedispatchResultTableView.class.getResource("/fxml/PstElementResultTableView.fxml"));
        fxmlLoader.setResources(RESOURCE_BUNDLE);
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
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Double cell factory
        Callback<TableColumn<PstElementResult, Number>, TableCell<PstElementResult, Number>> cellFactory = tc -> new TableCell<PstElementResult, Number>() {
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

        idTabColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        preOptimisationAngleTabColumn.setCellValueFactory(new PropertyValueFactory<>("preOptimisationAngle"));
        preOptimisationAngleTabColumn.setCellFactory(cellFactory);
        preOptimisationTapPositionTabColumn.setCellValueFactory(new PropertyValueFactory<>("preOptimisationTapPosition"));
        postOptimisationAngleTabColumn.setCellValueFactory(new PropertyValueFactory<>("postOptimisationAngle"));
        postOptimisationAngleTabColumn.setCellFactory(cellFactory);
        postOptimisationTapPositionTabColumn.setCellValueFactory(new PropertyValueFactory<>("postOptimisationTapPosition"));
    }

    public void selectResults(List<RemedialActionResult> remedialActionResults) {
        // Deal with remedial action elements table
        ObservableList<PstElementResult> pstElementResults = FXCollections.observableArrayList(
                remedialActionResults.stream()
                        .filter(RemedialActionResult::isApplied)
                        .flatMap(remedialActionResult -> remedialActionResult.getRemedialActionElementResults().stream())
                        .filter(rae -> rae instanceof PstElementResult)
                        .map(rae -> (PstElementResult) rae).collect(Collectors.toList())
        );
        if (!pstElementResults.isEmpty()) {
            pstElementTable.setItems(pstElementResults);
            pstElementTable.refresh();
        }
    }
}
