/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse;

import com.farao_community.data.crac_file.afs.AfsCracFile;
import com.farao_community.data.crac_file.afs.ImportedCracFileBuilder;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectCreationTask;
import com.powsybl.gse.spi.ProjectFileCreator;
import com.powsybl.gse.util.NodeSelectionPane;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * GSE user interface for importing JSON CRAC file in AFS and FARAO-GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class ImportedCracFileCreator extends GridPane implements ProjectFileCreator {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedCracFile");

    private final ProjectFolder folder;

    private final NodeSelectionPane<AfsCracFile> cracFileSelectionPane;

    ImportedCracFileCreator(ProjectFolder folder, Scene scene, GseContext context) {
        this.folder = Objects.requireNonNull(folder);
        Objects.requireNonNull(scene);

        cracFileSelectionPane = new NodeSelectionPane(folder.getFileSystem().getData(), RESOURCE_BUNDLE.getString("File"), true,
                scene.getWindow(), context, AfsCracFile.class);
        setHgap(5);
        setPrefWidth(450);
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column0, column1);
        add(cracFileSelectionPane.getLabel(), 0, 0);
        add(cracFileSelectionPane.getTextField(), 1, 0);
        add(cracFileSelectionPane.getButton(), 2, 0);
    }

    @Override
    public String getTitle() {
        return RESOURCE_BUNDLE.getString("CracImportConfig");
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public BooleanBinding okProperty() {
        return cracFileSelectionPane.nodeProperty().isNotNull();
    }

    @Override
    public ProjectCreationTask createTask() {
        AfsCracFile aFile = cracFileSelectionPane.nodeProperty().getValue();
        return new ProjectCreationTask() {
            @Override
            public String getNamePreview() {
                return aFile.getName();
            }

            @Override
            public void run() {
                folder.fileBuilder(ImportedCracFileBuilder.class)
                        .withAfsCracFile(aFile)
                        .build();
            }

            @Override
            public void undo() {
                throw new AssertionError("TODO"); // TODO
            }

            @Override
            public void redo() {
                throw new AssertionError("TODO"); // TODO
            }
        };
    }

    @Override
    public void dispose() {
        // nothing to dispose
    }
}
