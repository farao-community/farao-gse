/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.google.auto.service.AutoService;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import com.powsybl.gse.spi.ProjectFileViewerExtension;
import com.powsybl.gse.util.Glyph;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * Flow decomposition results viewer plugin
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
@AutoService(ProjectFileViewerExtension.class)
public class FlowDecompositionResultViewerExtension implements ProjectFileViewerExtension<FlowDecompositionRunner> {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowDecompositionGse");

    @Override
    public Class<FlowDecompositionRunner> getProjectFileType() {
        return FlowDecompositionRunner.class;
    }

    @Override
    public Class<?> getAdditionalType() {
        return null;
    }

    @Override
    public Node getMenuGraphic(FlowDecompositionRunner runner) {
        return Glyph.createAwesomeFont('\uf200');
    }

    @Override
    public String getMenuText(FlowDecompositionRunner flowDecompositionRunner) {
        return RESOURCE_BUNDLE.getString("ShowResults");
    }

    @Override
    public boolean isMenuEnabled(FlowDecompositionRunner flowDecompositionRunner) {
        return flowDecompositionRunner.hasResult();
    }

    @Override
    public ProjectFileViewer newViewer(FlowDecompositionRunner runner, Scene scene, GseContext context) {
        return new FlowDecompositionResultViewer(runner, scene, context);
    }
}
