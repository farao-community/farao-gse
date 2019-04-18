/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.NodeGraphicProvider;
import com.powsybl.gse.util.Glyph;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.scene.Node;

/**
 * Icons management plugin
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(NodeGraphicProvider.class)
public class FlowDecompositionNodeGraphicProvider implements NodeGraphicProvider {
    @Override
    public Node getGraphic(Object file) {
        if (file instanceof FlowDecompositionRunner) {
            return Glyph.createAwesomeFont('\uf200')
                    .size("1.4em")
                    .color("#c2b280");

        }
        return null;
    }
}
