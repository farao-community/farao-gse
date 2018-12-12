/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse.rae;
/**
 * GSE user interface for visualizing Remedial Action Pst
 *
 * @author Marc Erkol {@literal <marc.erkol at rte-france.com>}
 */
import com.farao_community.farao.data.crac_file.RedispatchRemedialActionElement;
import com.farao_community.farao.data.crac_file.RemedialAction;
import com.farao_community.farao.data.crac_file.RemedialActionElement;
import com.powsybl.gse.spi.GseContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GenRedispatchTableView extends TableView implements Initializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedCracFileViewer");

    @FXML
    private TableView<RemedialActionElement> remedialActionElementsTable;

    @FXML
    private TableColumn<RemedialActionElement, String> remedialActionElementIDTabColumn;

    @FXML
    private TableColumn<RemedialActionElement, String> remedialActionmMinimumPowerTabColumn;

    @FXML
    private TableColumn<RemedialActionElement, String> remedialActionMaximumPowerTabColumn;

    @FXML
    private TableColumn<RemedialActionElement, String> remadialActionStartupCostTabColumn;

    @FXML
    private TableColumn<RemedialActionElement, String> remedialActionMarginalCostTabColumn;


    public GenRedispatchTableView(GseContext gseContext) {
    }

    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(GenRedispatchTableView.class.getResource("/fxml/GenRedispatchTableView.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("lang.ImportedCracFileViewer"));
        fxmlLoader.setController(this);

        Node content = null;
        try {
            content = fxmlLoader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return content;
    }

    public void selectRemedialAction(RemedialAction remedialAction) {
        // Deal with remedial action elements table
        ObservableList<RemedialActionElement> remedialActionElements = FXCollections.observableArrayList(
                remedialAction.getRemedialActionElements().stream()
                        .filter(rae -> rae instanceof RedispatchRemedialActionElement).collect(Collectors.toList())
        );
        if (!remedialActionElements.isEmpty()) {
            remedialActionElementsTable.setItems(remedialActionElements);
            remedialActionElementsTable.refresh();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Remedial Action Element
        remedialActionElementIDTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("id"));
        remedialActionmMinimumPowerTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("minimumPower"));
        remedialActionMaximumPowerTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("maximumPower"));
        remadialActionStartupCostTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("startupCost"));
        remedialActionMarginalCostTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("marginalCost"));
    }
}
