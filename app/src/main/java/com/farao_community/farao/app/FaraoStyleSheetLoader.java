/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.StyleSheetLoader;
import com.powsybl.gse.util.DefaultStyleSheetLoader;
import javafx.scene.Scene;

/**
 * CSS style sheet loader implementation for FARAO GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(StyleSheetLoader.class)
public class FaraoStyleSheetLoader extends DefaultStyleSheetLoader {
    @Override
    public void load(Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/css/farao-gse.css").toExternalForm());
    }
}
