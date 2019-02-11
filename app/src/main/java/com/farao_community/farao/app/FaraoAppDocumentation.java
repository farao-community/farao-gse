/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import com.powsybl.commons.config.ModuleConfig;
import com.powsybl.commons.config.PlatformConfig;
import com.powsybl.gse.spi.GseAppDocumentation;
import javafx.application.Application;

import java.util.Optional;

/**
 * Application documentation provider for FARAO GSE.
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public final class FaraoAppDocumentation implements GseAppDocumentation {
    private final Application application;
    private final String url;

    public static Optional<GseAppDocumentation> getOptionalDocumentation(Application application) {
        Optional<ModuleConfig> faraoOptional = PlatformConfig.defaultConfig().getOptionalModuleConfig("farao-gse");
        if (faraoOptional.isPresent() && faraoOptional.get().hasProperty("documentation-url")) {
            return Optional.of(new FaraoAppDocumentation(application, faraoOptional.get().getStringProperty("documentation-url")));
        } else {
            return Optional.empty();
        }
    }

    private FaraoAppDocumentation(Application application, String url) {
        this.application = application;
        this.url = url;
    }

    @Override
    public void show() {
        application.getHostServices().showDocument(url);
    }
}
