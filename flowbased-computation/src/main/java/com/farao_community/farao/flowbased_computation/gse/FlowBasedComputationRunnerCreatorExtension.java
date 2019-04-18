/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation.gse;

import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileCreatorExtension;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileCreatorExtension.class)
public class FlowBasedComputationRunnerCreatorExtension implements ProjectFileCreatorExtension {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowBased");

    @Override
    public Class<FlowBasedComputationRunner> getProjectFileType() {
        return FlowBasedComputationRunner.class;
    }

    @Override
    public String getMenuText() {
        return RESOURCE_BUNDLE.getString("CreateFbComputation") + "...";
    }

    @Override
    public FlowBasedComputationRunnerCreator newCreator(ProjectFolder folder, Scene scene, GseContext context) {
        return new FlowBasedComputationRunnerCreator(folder, scene, context);
    }
}
