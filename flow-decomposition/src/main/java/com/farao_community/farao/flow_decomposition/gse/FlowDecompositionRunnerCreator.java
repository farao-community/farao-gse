/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.powsybl.afs.ProjectFile;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectCreationTask;
import com.powsybl.gse.spi.ProjectFileCreator;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunnerBuilder;
import javafx.scene.Scene;

/**
 * Flow decomposition runner creator
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
public class FlowDecompositionRunnerCreator extends AbstractFlowDecompositionRunnerPane<ProjectFolder> implements ProjectFileCreator {

    public FlowDecompositionRunnerCreator(ProjectFolder folder, Scene scene, GseContext context) {
        super(folder, scene, context);
    }

    @Override
    public ProjectCreationTask createTask() {
        String name = nameTextField.getText();
        ProjectFile aCase = caseSelectionPane.nodeProperty().getValue();
        ProjectFile aCracFileProvider = cracFileProviderSelectionPane.nodeProperty().getValue();
        return new ProjectCreationTask() {
            @Override
            public String getNamePreview() {
                return name;
            }

            @Override
            public void run() {
                node.fileBuilder(FlowDecompositionRunnerBuilder.class)
                        .withName(name)
                        .withCase(aCase)
                        .withCracFileProvider(aCracFileProvider)
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
}
