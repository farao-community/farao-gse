/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import com.google.common.collect.ImmutableList;
import com.powsybl.gse.spi.DefaultAboutPane;
import javafx.scene.Node;


import java.util.List;

/**
 * About pane implementation for FARAO GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class FaraoAboutPane extends DefaultAboutPane {

    public FaraoAboutPane() {
        getStyleClass().add("farao-about-pane");
    }

    @Override
    protected List<Node> getAdditionalLogos() {
        return ImmutableList.of(new FaraoLogo(LOGO_SIZE, LOGO_SIZE));
    }
}
