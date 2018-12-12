/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.RaoComputationResult;
import com.farao_community.farao.ra_optimisation.afs.RaoComputationRunner;
import com.farao_community.farao.ra_optimisation.converter.RaoComputationResultExporters;
import com.google.auto.service.AutoService;
import com.powsybl.afs.TaskMonitor;
import com.powsybl.gse.spi.ExecutionTaskConfigurator;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileExecutionTaskExtension;
import com.powsybl.gse.util.Glyph;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Rao computation results export plugin
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
@AutoService(ProjectFileExecutionTaskExtension.class)
public class OutputExporterExtension implements ProjectFileExecutionTaskExtension<RaoComputationRunner, OutputExporterParameters> {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.OutputExporter");

    @Override
    public Class<RaoComputationRunner> getProjectFileType() {
        return RaoComputationRunner.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return null;
    }

    @Override
    public String getMenuText(RaoComputationRunner runner) {
        return RESOURCE_BUNDLE.getString("ResultsExport");
    }

    @Override
    public Node getMenuGraphic(RaoComputationRunner runner) {
        return Glyph.createAwesomeFont('\uf093').size("1em");
    }

    @Override
    public boolean isMenuEnabled(RaoComputationRunner raoComputationRunner) {
        return raoComputationRunner.hasResult() && !RaoComputationResultExporters.getFormats().isEmpty();
    }

    @Override
    public ExecutionTaskConfigurator<OutputExporterParameters> createConfigurator(RaoComputationRunner raoComputationRunner, Scene scene, GseContext context) {
        return new ExecutionTaskConfigurator<OutputExporterParameters>() {

            private final GridPane main = new GridPane();

            private final ObjectProperty<OutputExporterParameters> configProperty = new SimpleObjectProperty<>();

            private final TextField fileTextField = new TextField();

            private final Button fileButton = new Button("...");

            private final ComboBox formatComboBox = new ComboBox();

            private OutputExporterParameters config = new OutputExporterParameters();

            {
                main.setVgap(5);
                main.setHgap(5);
                main.setPrefWidth(400);
                ColumnConstraints column0 = new ColumnConstraints();
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.ALWAYS);
                main.getColumnConstraints().addAll(column0, column1);
                main.add(new Label(RESOURCE_BUNDLE.getString("DestinationFile") + ":"), 0, 0);
                main.add(fileTextField, 1, 1);
                main.add(fileButton, 2, 1);
                main.add(formatComboBox, 1, 0);

                formatComboBox.setItems(FXCollections.observableArrayList(RaoComputationResultExporters.getFormats()));
                formatComboBox.setOnAction(event -> config.setExporter((String) formatComboBox.getValue()));

                // Items cannot be empty because of the condition put in isMenuEnabled()
                // We can get index 0 without fear
                // Assertion used for maintenance purpose
                // Use Platform.runLater() for action event to be triggered correctly
                assert formatComboBox.getItems().size() > 0;
                Platform.runLater(() -> formatComboBox.setValue(formatComboBox.getItems().get(0)));

                // Add action events
                fileButton.setOnAction(event -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    fileChooser.setTitle(RESOURCE_BUNDLE.getString("SelectTargetFile"));
                    File file = fileChooser.showSaveDialog(scene.getWindow());
                    if (file != null) {
                        fileTextField.setText(file.toString());
                        config.setFilePath(file.toString());
                        configProperty.set(config);
                    }
                });
            }

            @Override
            public String getTitle() {
                return RESOURCE_BUNDLE.getString("ResultExportParameters");
            }

            @Override
            public Node getContent() {
                return main;
            }

            @Override
            public ObjectProperty<OutputExporterParameters> configProperty() {
                return configProperty;
            }

            public void dispose() {
                // nothing to dispose
            }
        };
    }

    private static OutputStream createOutputStream(OutputExporterParameters config) throws IOException {
        return new BufferedOutputStream(Files.newOutputStream(config.getFilePath()));
    }

    @Override
    public void execute(RaoComputationRunner raoComputationRunner, OutputExporterParameters config) {
        TaskMonitor monitor = raoComputationRunner.getFileSystem().getTaskMonitor();
        UUID taskId = monitor.startTask(raoComputationRunner).getId();
        try (OutputStream outputStream = createOutputStream(config)) {
            monitor.updateTaskMessage(taskId, MessageFormat.format(RESOURCE_BUNDLE.getString("ExportResultsToXlsx"), config.getFilePath()));
            RaoComputationResult results = raoComputationRunner.readResult();
            config.getExporter().export(results, outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            monitor.stopTask(taskId);
        }
    }
}
