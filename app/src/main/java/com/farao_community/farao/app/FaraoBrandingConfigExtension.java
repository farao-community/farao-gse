/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.BrandingConfig;
import com.powsybl.gse.spi.BrandingExtension;
import com.powsybl.gse.spi.GseAppDocumentation;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Main branding extension for FARAO GSE
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@AutoService(BrandingExtension.class)
public class FaraoBrandingConfigExtension implements BrandingExtension {
    @Override
    public BrandingConfig getConfig() {
        return new BrandingConfig() {
            /**
             * @return String titre of application
             */
            @Override
            public String getTitle() {
                return "Farao GSE";
            }

            /**
             * @return Image icon of the application
             */
            @Override
            public List<Image> getIcons() {
                return Collections.singletonList(new Image(getClass().getResourceAsStream("/icone-lancement-farao.svg"))); // icon of application
            }

            /**
             * @return Region (javaFx) logo of the application
             * you can put in region text, image, object here.
             */
            @Override
            public Region getLogo() {
                return new FaraoLogo(32, 32);
            } // call logo

            /**
             * @return Pane
             */
            @Override
            public Pane getAboutPane() {
                return new FaraoAboutPane();
            }

            /**
             * @param application
             * @return GseAppDocumentation documentation of you application for the user
             */
            @Override
            public Optional<GseAppDocumentation> getDocumentation(Application application) {
                return FaraoAppDocumentation.getOptionalDocumentation(application);
            }
        };
    }
}
