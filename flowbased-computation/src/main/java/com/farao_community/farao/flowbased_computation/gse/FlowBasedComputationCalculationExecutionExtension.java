/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.ExecutionTaskConfigurator;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileExecutionTaskExtension;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Scene;

import java.util.ResourceBundle;

@AutoService(ProjectFileExecutionTaskExtension.class)
public class FlowBasedComputationCalculationExecutionExtension implements ProjectFileExecutionTaskExtension<FlowBasedComputationRunner, Void> {
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
    public String getMenuText(FlowBasedComputationRunner runner) {
        return RESOURCE_BUNDLE.getString("LaunchCalculation");
    }

    @Override
    public ExecutionTaskConfigurator<Void> createConfigurator(FlowBasedComputationRunner raoComputationRunner, Scene scene, GseContext gseContext) {
        return null;
    }

    @Override
    public void execute(FlowBasedComputationRunner raoComputationRunner, Void aVoid) {
        raoComputationRunner.run();
    }
}
