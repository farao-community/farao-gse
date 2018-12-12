/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse;

import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFile;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import com.powsybl.gse.spi.ProjectFileViewerExtension;
import com.farao_community.farao.data.crac_file.CracFileProvider;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * CRAC file visualization integration extension in GSE.
 *
 * Used to register CRAC file visualization feature in FARAO-GSE
 * @see ImportedCracFileViewer
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileViewerExtension.class)
public class ImportedCracFileViewerExtension implements ProjectFileViewerExtension<ProjectFile> {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedCracFile");

    @Override
    public Class<ProjectFile> getProjectFileType() {
        return ProjectFile.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return CracFileProvider.class;
    }

    @Override
    public String getMenuText(ProjectFile projectFile) {
        return RESOURCE_BUNDLE.getString("ExploreCracFile");
    }

    @Override
    public ProjectFileViewer newViewer(ProjectFile cracFileProvider, Scene scene, GseContext gseContext) {
        return new ImportedCracFileViewer((CracFileProvider) cracFileProvider, gseContext);
    }
}
