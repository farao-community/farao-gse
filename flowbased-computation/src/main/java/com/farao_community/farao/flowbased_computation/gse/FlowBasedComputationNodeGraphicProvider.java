/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.NodeGraphicProvider;
import com.powsybl.gse.util.Glyph;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Node;

/**
 * Add icons
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
@AutoService(NodeGraphicProvider.class)
public class FlowBasedComputationNodeGraphicProvider implements NodeGraphicProvider {

    @Override
    public Node getGraphic(Object file) {
        if (file instanceof FlowBasedComputationRunner) {
            return Glyph.createAwesomeFont('\uf20e')
                    .size("1.4em")
                    .color("#0050a3");

        }
        return null;
    }
}
