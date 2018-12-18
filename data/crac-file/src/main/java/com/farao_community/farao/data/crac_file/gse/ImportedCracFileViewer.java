/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse;

import com.farao_community.farao.data.crac_file.gse.rae.GenRedispatchTableView;
import com.farao_community.farao.data.crac_file.gse.rae.PstTableView;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import com.farao_community.farao.data.crac_file.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GSE user interface for visualizing JSON CRAC file in FARAO-GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class ImportedCracFileViewer implements ProjectFileViewer, Initializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedCracFileViewer");

    private final GseContext gseContext;

    @FXML
    private TextField cracIdField;

    @FXML
    private TextField cracNameField;

    @FXML
    private TextField cracFormatField;

    @FXML
    private TextArea cracDescriptionArea;

    @FXML
    private TextField preContigenciesNbrField;

    @FXML
    private TextField contingenciesNbrField;

    @FXML
    private TextField rateMonitoredBranchesField;

    @FXML
    private TableView<MonitoredBranch> monitoredBranchesTable;

    @FXML
    private TableColumn<MonitoredBranch, String> idTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, String> nameTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, String> branchIdTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, Double> fmaxTableColumn;

    @FXML
    private TableView<Contingency> contingenciesTable;

    @FXML
    private TableColumn<Contingency, String> contingenciesIDTabColumn;

    @FXML
    private TableColumn<Contingency, String> contingenciesNameTabColumn;

    @FXML
    private TableView<ContingencyElement> contingencyElementsTable;

    @FXML
    private TableColumn<ContingencyElement, String> contingencyElementIDTabColumn;

    @FXML
    private TableColumn<Contingency, String> contingencyElementNameTabColumn;

    @FXML
    private TableView<MonitoredBranch> postContingencyMonitoredBranchesTable;

    @FXML
    private TableColumn<MonitoredBranch, String> postContingencyIdTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, String> postContingencyNameTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, String> postContingencyBranchIdTableColumn;

    @FXML
    private TableColumn<MonitoredBranch, Double> postContingencyFmaxTableColumn;

    @FXML
    private BorderPane remedialActionElementsBorderPane;

    @FXML
    private TableView<RemedialAction> remedialActionTable;

    @FXML
    private TableColumn<RemedialAction, String> remedialActionIDTabColumn;

    @FXML
    private TableColumn<RemedialAction, String> remedialActionNameTabColumn;

    @FXML
    private TableColumn<RemedialAction, String> remedialActionTypeTabColumn;

    @FXML
    private TableColumn<RemedialAction, String> remedialActionElementTypeTabColumn;

    @FXML
    private  TableView<UsageRule> usageRulesTable;

    @FXML
    private TableColumn<UsageRule, String> raUsageRulesIdTableColumn;

    @FXML
    private TableColumn<UsageRule, String> raUsageRulesInstantTableColumn;

    @FXML
    private TableColumn<UsageRule, String> raUsageColumn;

    @FXML
    private TableColumn<UsageRule, String> raContingenciesColumn;

    @FXML
    private TableColumn<UsageRule, String> raCriticalBranchColumn;

    @FXML
    private BorderPane remedialActionBorderPane;

    @FXML
    private SplitPane remedialActionElementsSplit;

    private CracFileProvider cracFileProvider;

    public ImportedCracFileViewer(CracFileProvider cracFileProvider, GseContext gseContext) {
        this.cracFileProvider = Objects.requireNonNull(cracFileProvider);
        this.gseContext = gseContext;
    }

    @Override
    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(ImportedCracFileViewer.class.getResource("/fxml/ImportedCracFileViewer.fxml"));
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

    @Override
    public void view() {

        // Overview
        CracFile cracFile = cracFileProvider.getCracFile();
        cracIdField.setText(cracFile.getId());
        cracNameField.setText(cracFile.getName());
        cracFormatField.setText(cracFile.getSourceFormat());
        cracDescriptionArea.setText(cracFile.getDescription());

        int preContigenciesNbr = cracFile.getPreContingency().getMonitoredBranches().size();
        String preContigenciesNbrStr = String.valueOf(preContigenciesNbr);
        preContigenciesNbrField.setText(preContigenciesNbrStr);

        int contingenciesNbr = cracFile.getContingencies().size();
        String contingenciesNbrString = String.valueOf(contingenciesNbr);
        contingenciesNbrField.setText(contingenciesNbrString);

        int monitoredBranchesNbr = cracFile.getContingencies().stream().mapToInt(contingencies -> contingencies.getMonitoredBranches().size()).sum();
        String rateString = contingenciesNbr == 0 ? "N/A" : String.valueOf(monitoredBranchesNbr / contingenciesNbr);
        rateMonitoredBranchesField.setText(rateString);

        // Precontiongencies
        MonitoredBranch[] details = cracFile.getPreContingency().getMonitoredBranches().stream()
                .toArray(MonitoredBranch[]::new);
        monitoredBranchesTable.setItems(FXCollections.observableArrayList(details));

        idTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("name"));
        branchIdTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("branchId"));
        fmaxTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, Double>("fmax"));

        // PostContingencies
        Contingency[] contingencyTable = cracFile.getContingencies().stream().toArray(Contingency[]::new);
        contingenciesTable.setItems(FXCollections.observableArrayList(contingencyTable));
        contingenciesIDTabColumn.setCellValueFactory(new PropertyValueFactory<Contingency, String>("id"));
        contingenciesNameTabColumn.setCellValueFactory(new PropertyValueFactory<Contingency, String>("name"));

        contingencyElementIDTabColumn.setCellValueFactory(new PropertyValueFactory<ContingencyElement, String>("elementId"));
        contingencyElementNameTabColumn.setCellValueFactory(new PropertyValueFactory<Contingency, String>("name"));

        postContingencyIdTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("id"));
        postContingencyNameTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("name"));
        postContingencyBranchIdTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, String>("branchId"));
        postContingencyFmaxTableColumn.setCellValueFactory(new PropertyValueFactory<MonitoredBranch, Double>("fmax"));

        //Remedial Action
        RemedialAction[] raTable = cracFile.getRemedialActions().stream().toArray(RemedialAction[]::new);
        remedialActionTable.setItems(FXCollections.observableArrayList(raTable));
        remedialActionIDTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialAction, String>("id"));
        remedialActionNameTabColumn.setCellValueFactory(new PropertyValueFactory<RemedialAction, String>("name"));

        // Usage rules
        raUsageRulesIdTableColumn.setCellValueFactory(new PropertyValueFactory<UsageRule, String>("id"));
        raUsageRulesInstantTableColumn.setCellValueFactory(new PropertyValueFactory<UsageRule, String>("instants"));
        raUsageColumn.setCellValueFactory(new PropertyValueFactory<UsageRule, String>("usage"));
        raContingenciesColumn.setCellValueFactory(new PropertyValueFactory<UsageRule, String>("contingenciesID"));
        raCriticalBranchColumn.setCellValueFactory(new PropertyValueFactory<UsageRule, String>("constraintsID"));

        remedialActionTypeTabColumn.setCellValueFactory(cellDataFatures -> {
            if (cellDataFatures.getValue() != null) {
                return new SimpleStringProperty(getTypeFromRemedialAction(cellDataFatures.getValue()));
            } else {
                return new SimpleStringProperty("");
            }
        });
    }

    @Override
    public void dispose() {
        // nothing to dispose
    }

    @Override
    public boolean isClosable() {
        return true;
    }

    public String getTypeFromRemedialAction(RemedialAction remedialAction) {
        List<Class> classes = remedialAction.getRemedialActionElements().stream()
                .map(RemedialActionElement::getClass)
                .distinct()
                .collect(Collectors.toList());
        List<String> typeRa = new ArrayList<>();
        classes.stream().forEach(type -> {
            if (type.equals(RedispatchRemedialActionElement.class)) {
                typeRa.add(RESOURCE_BUNDLE.getString("RaRedispatchingType"));
            }

            if (type.equals(PstElement.class)) {
                typeRa.add(RESOURCE_BUNDLE.getString("RaPSTType"));
            }
        });
        return String.join(", ", typeRa);
    }

    private void remedialAction(RemedialAction remedialAction) {

        // Reset panels and controllers
        remedialActionElementsSplit.getItems().clear();
        remedialActionElementsSplit.setOrientation(Orientation.VERTICAL);
        remedialActionTable.refresh();
        // Redispatch
        if (remedialAction.getRemedialActionElements().stream().anyMatch(elem -> elem instanceof RedispatchRemedialActionElement)) {
            GenRedispatchTableView genRedispatchTableView = new GenRedispatchTableView(gseContext);
            remedialActionElementsSplit.getItems().add(genRedispatchTableView.getContent());
            genRedispatchTableView.selectRemedialAction(remedialAction);
        }

        //PST
        if (remedialAction.getRemedialActionElements().stream().anyMatch(elem -> elem instanceof PstElement)) {
            PstTableView pstTableView = new PstTableView(gseContext);
            remedialActionElementsSplit.getItems().add(pstTableView.getContent());
            pstTableView.selectRemedialAction(remedialAction);
        }

        // Deal with UsageRule elements table
        ObservableList<UsageRule> usageRuleElements = FXCollections.observableArrayList(
                remedialAction.getUsageRules()
        );
        usageRulesTable.setItems(usageRuleElements);
        usageRulesTable.refresh();


    }

    private void selectContingency(Contingency contingency) {
        // Deal with contingency elements table
        ObservableList<ContingencyElement> contingencyElements = FXCollections.observableArrayList(
                contingency.getContingencyElements()
        );
        contingencyElementsTable.setItems(contingencyElements);
        contingencyElementsTable.refresh();

        // Deal with post contingency monitored branches table
        ObservableList<MonitoredBranch> postContingencyMonitoredBranches = FXCollections.observableArrayList(
                contingency.getMonitoredBranches()
        );
        postContingencyMonitoredBranchesTable.setItems(postContingencyMonitoredBranches);
        postContingencyMonitoredBranchesTable.refresh();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // nothing to initialize
        contingenciesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectContingency(newValue));
        remedialActionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> remedialAction(newValue));
    }
}

