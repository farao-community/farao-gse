package com.farao_community.farao.data.crac_file_xlsx.gse;

import com.google.auto.service.AutoService;
import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileCreatorExtension;
import com.farao_community.farao.afs.crac_file.xlsx.ImportedXlsxCracFile;
import javafx.scene.Scene;

import java.util.ResourceBundle;
/**
 * @author Marc Erkol {@literal <marc.erkol at rte-france.com>}
 */
@AutoService(ProjectFileCreatorExtension.class)
public class ImportedXlsxCracFileCreatorExtension implements ProjectFileCreatorExtension {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedXlsxCracFile");
    @Override
    public Class<ImportedXlsxCracFile> getProjectFileType() {
        return ImportedXlsxCracFile.class;
    }

    @Override
    public String getMenuText() {
        return RESOURCE_BUNDLE.getString("ImportCracFile") + "...";
    }

    @Override
    public ImportedXlsxCracFileCreator newCreator(ProjectFolder projectFolder, Scene scene, GseContext gseContext) {
        return new ImportedXlsxCracFileCreator(projectFolder, scene, gseContext);
    }
}
