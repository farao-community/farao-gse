/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.ExecutionTaskConfigurator;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileExecutionTaskExtension;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * Flow decomposition task runner plugin
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileExecutionTaskExtension.class)
public class FlowDecompositionExecutionTaskExtension implements ProjectFileExecutionTaskExtension<FlowDecompositionRunner, Void> {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowDecompositionGse");

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
        return RESOURCE_BUNDLE.getString("RunFlowDecomposition");
    }

    @Override
    public void execute(FlowDecompositionRunner runner, Void config) {
        runner.run();
    }

    @Override
    public ExecutionTaskConfigurator<Void> createConfigurator(FlowDecompositionRunner runner, Scene scene, GseContext context) {
        return null;
    }
}
