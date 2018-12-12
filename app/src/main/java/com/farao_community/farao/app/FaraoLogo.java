/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.app;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoader;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Panel containing FARAO penguin logo
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class FaraoLogo extends Pane {

    /**
     * @param width
     * @param height
     */
    public FaraoLogo(double width, double height) {
        SvgImageLoaderFactory.install();
        ImageView faraoLogo = new ImageView(new Image(SvgImageLoader.class.getResourceAsStream("/logo-farao.svg"))); // logo of Farao put here (if you want change format don't se ImageView
        faraoLogo.setFitHeight(height);
        faraoLogo.setFitWidth(width);
        getChildren().addAll(faraoLogo); // the children is a Pane of Java fx
    }
}
