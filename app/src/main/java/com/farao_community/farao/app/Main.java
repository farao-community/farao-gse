/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import com.powsybl.gse.app.GseApp;

/**
 * Main class for launching FARAO GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        GseApp.main(args);
    }
}
