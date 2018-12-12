/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse;

import com.farao_community.data.crac_file.afs.ImportedCracFile;
import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileCreatorExtension;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * CRAC file import integration extension in GSE.
 *
 * Used to register CRAC file import feature in FARAO-GSE
 * @see ImportedCracFileCreator
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(ProjectFileCreatorExtension.class)
public class ImportedCracFileCreatorExtension implements ProjectFileCreatorExtension {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedCracFile");
    @Override
    public Class<ImportedCracFile> getProjectFileType() {
        return ImportedCracFile.class;
    }

    @Override
    public String getMenuText() {
        return RESOURCE_BUNDLE.getString("ImportCracFile") + "...";
    }

    @Override
    public ImportedCracFileCreator newCreator(ProjectFolder projectFolder, Scene scene, GseContext gseContext) {
        return new ImportedCracFileCreator(projectFolder, scene, gseContext);
    }
}
