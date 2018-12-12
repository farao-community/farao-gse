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
import com.farao_community.farao.data.crac_file.PstElement;
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

public class PstTableView implements Initializable {
    @FXML
    private TableView<RemedialActionElement> pstElementTable;

    @FXML
    private TableColumn<RemedialActionElement, String> pstElementId;

    @FXML
    private TableColumn<RemedialActionElement, String> pstTypeOfLimit;

    @FXML
    private TableColumn<RemedialActionElement, String> pstMinStepRange;

    @FXML
    private TableColumn<RemedialActionElement, String> pstMaxStepRange;

    public PstTableView(GseContext gseContext) {
    }

    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(PstTableView.class.getResource("/fxml/PstTableView.fxml"));
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
        ObservableList<RemedialActionElement> pstElements = FXCollections.observableArrayList(
                remedialAction.getRemedialActionElements().stream()
                        .filter(rae -> rae instanceof PstElement).collect(Collectors.toList())
        );

        pstElementTable.setItems(pstElements);
        pstElementTable.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pstElementId.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("id"));
        pstTypeOfLimit.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("typeOfLimit"));
        pstMinStepRange.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("minStepRange"));
        pstMaxStepRange.setCellValueFactory(new PropertyValueFactory<RemedialActionElement, String>("maxStepRange"));
    }
}
