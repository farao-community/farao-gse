/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.flow_decomposition_results.xlsx.gse;

import com.google.auto.service.AutoService;
import com.powsybl.afs.TaskMonitor;
import com.powsybl.gse.spi.ExecutionTaskConfigurator;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileExecutionTaskExtension;
import com.farao_community.farao.data.flow_decomposition_results.FlowDecompositionResults;
import com.farao_community.farao.data.flow_decomposition_results.xlsx.XlsxOutputExporter;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * Flow decomposition results XLSX export plugin
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
@AutoService(ProjectFileExecutionTaskExtension.class)
public class XlsxOutputExporterExtension implements ProjectFileExecutionTaskExtension<FlowDecompositionRunner, XlsxOutputExporterParameters> {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.XlsxOutputExporter");

    @Override
    public Class<FlowDecompositionRunner> getProjectFileType() {
        return FlowDecompositionRunner.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return null;
    }

    @Override
    public String getMenuText(FlowDecompositionRunner runner) {
        return RESOURCE_BUNDLE.getString("ResultsExport");
    }

    @Override
    public boolean isMenuEnabled(FlowDecompositionRunner flowDecompositionRunner) {
        return flowDecompositionRunner.hasResult();
    }

    @Override
    public ExecutionTaskConfigurator<XlsxOutputExporterParameters> createConfigurator(FlowDecompositionRunner flowDecompositionRunner, Scene scene, GseContext context) {
        return new ExecutionTaskConfigurator<XlsxOutputExporterParameters>() {

            private final GridPane main = new GridPane();

            private final ObjectProperty<XlsxOutputExporterParameters> configProperty = new SimpleObjectProperty<>();

            private final TextField fileTextField = new TextField();

            private final Button fileButton = new Button("...");

            private XlsxOutputExporterParameters config = new XlsxOutputExporterParameters();

            {
                main.setVgap(5);
                main.setHgap(5);
                main.setPrefWidth(400);
                ColumnConstraints column0 = new ColumnConstraints();
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.ALWAYS);
                main.getColumnConstraints().addAll(column0, column1);
                main.add(new Label(RESOURCE_BUNDLE.getString("DestinationFile") + ":"), 0, 0);
                main.add(fileTextField, 1, 0);
                main.add(fileButton, 2, 0);

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
            public ObjectProperty<XlsxOutputExporterParameters> configProperty() {
                return configProperty;
            }

            public void dispose() {
                // nothing to dispose
            }
        };
    }

    private static OutputStream createOutputStream(XlsxOutputExporterParameters config) throws IOException {
        return new BufferedOutputStream(Files.newOutputStream(config.getFilePath()));
    }

    @Override
    public void execute(FlowDecompositionRunner flowDecompositionRunner, XlsxOutputExporterParameters config) {
        TaskMonitor monitor = flowDecompositionRunner.getFileSystem().getTaskMonitor();
        UUID taskId = monitor.startTask(flowDecompositionRunner).getId();
        try (OutputStream outputStream = createOutputStream(config)) {
            monitor.updateTaskMessage(taskId, MessageFormat.format(RESOURCE_BUNDLE.getString("ExportResultsToXlsx"), config.getFilePath()));
            FlowDecompositionResults results = flowDecompositionRunner.readResult();
            XlsxOutputExporter.exportInStream(outputStream, results);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            monitor.stopTask(taskId);
        }
    }
}
