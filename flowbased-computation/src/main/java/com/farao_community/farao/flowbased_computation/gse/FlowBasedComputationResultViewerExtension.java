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
import com.powsybl.gse.spi.ProjectFileViewer;
import com.powsybl.gse.spi.ProjectFileViewerExtension;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Scene;

import java.util.ResourceBundle;

@AutoService(ProjectFileViewerExtension.class)
public class FlowBasedComputationResultViewerExtension implements ProjectFileViewerExtension<ProjectFile> {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowBased");

    @Override
    public Class<ProjectFile> getProjectFileType() {
        return ProjectFile.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return FlowBasedComputationRunner.class;
    }

    @Override
    public String getMenuText(ProjectFile projectFile) {
        return RESOURCE_BUNDLE.getString("ShowResult");
    }

    @Override
    public ProjectFileViewer newViewer(ProjectFile importedRaoFile, Scene scene, GseContext gseContext) {
        return new FlowBasedComputationResultViewer(importedRaoFile, gseContext);
    }
}
