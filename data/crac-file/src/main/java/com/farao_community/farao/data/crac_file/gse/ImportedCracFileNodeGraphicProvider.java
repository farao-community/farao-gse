/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.crac_file.gse;

import com.farao_community.data.crac_file.afs.AfsCracFile;
import com.farao_community.data.crac_file.afs.ImportedCracFile;
import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.NodeGraphicProvider;
import com.powsybl.gse.util.Glyph;
import javafx.scene.Node;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(NodeGraphicProvider.class)
public class ImportedCracFileNodeGraphicProvider implements NodeGraphicProvider {

    private Node createCracGlyph() {
        return Glyph.createAwesomeFont('\uf1cd')
                .color("darkred")
                .size("1.4em");
    }

    @Override
    public Node getGraphic(Object file) {
        if (file instanceof AfsCracFile || file instanceof ImportedCracFile) {
            return createCracGlyph();
        }
        return null;
    }
}
