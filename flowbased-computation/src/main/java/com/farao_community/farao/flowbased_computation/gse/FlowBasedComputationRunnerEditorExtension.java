/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation.gse;

import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFile;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileEditor;
import com.powsybl.gse.spi.ProjectFileEditorExtension;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileEditorExtension.class)
public class FlowBasedComputationRunnerEditorExtension implements ProjectFileEditorExtension {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowBased");

    @Override
    public Class<FlowBasedComputationRunner> getProjectFileType() {
        return FlowBasedComputationRunner.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return null;
    }

    @Override
    public String getMenuText(ProjectFile file) {
        return RESOURCE_BUNDLE.getString("EditFbComputation") + "...";
    }

    @Override
    public ProjectFileEditor newEditor(ProjectFile file, Scene scene, GseContext context) {
        return new FlowBasedComputationRunnerEditor((FlowBasedComputationRunner) file, scene, context);
    }

}
