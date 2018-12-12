/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.afs.RaoComputationRunner;
import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileCreatorExtension;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileCreatorExtension.class)
public class RaoComputationRunnerCreatorExtension implements ProjectFileCreatorExtension {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.Rao");

    @Override
    public Class<RaoComputationRunner> getProjectFileType() {
        return RaoComputationRunner.class;
    }

    @Override
    public String getMenuText() {
        return RESOURCE_BUNDLE.getString("CreateRaoComputation") + "...";
    }

    @Override
    public RaoComputationRunnerCreator newCreator(ProjectFolder folder, Scene scene, GseContext context) {
        return new RaoComputationRunnerCreator(folder, scene, context);
    }
}
