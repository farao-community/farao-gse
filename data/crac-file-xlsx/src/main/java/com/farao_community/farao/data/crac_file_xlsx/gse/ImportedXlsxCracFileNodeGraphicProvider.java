package com.farao_community.farao.data.crac_file_xlsx.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.NodeGraphicProvider;
import com.powsybl.gse.util.Glyph;
import com.farao_community.farao.afs.crac_file.xlsx.AfsXlsxCracFile;
import com.farao_community.farao.afs.crac_file.xlsx.ImportedXlsxCracFile;
import javafx.scene.Node;
/**
 * @author Marc Erkol {@literal <marc.erkol at rte-france.com>}
 */
@AutoService(NodeGraphicProvider.class)
public class ImportedXlsxCracFileNodeGraphicProvider implements NodeGraphicProvider {

    private Node createCracGlyph() {
        return Glyph.createAwesomeFont('\uf1cd')
                .color("darkred")
                .size("1.4em");
    }
    @Override
    public Node getGraphic(Object file) {
        if (file instanceof AfsXlsxCracFile || file instanceof ImportedXlsxCracFile) {
            return createCracGlyph();
        }
        return null;
    }
}
