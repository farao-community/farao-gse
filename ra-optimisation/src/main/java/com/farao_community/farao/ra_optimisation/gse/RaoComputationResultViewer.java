/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.powsybl.afs.ProjectFile;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileViewer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class RaoComputationResultViewer implements ProjectFileViewer, Initializable {

    public RaoComputationResultViewer(ProjectFile importedRaoFile, GseContext gseContex) {
    }

    @Override
    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(RaoComputationResultViewer.class.getResource("/fxml/launchRaoCalculationViewer.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("lang.Rao"));
        fxmlLoader.setController(this);
        Node content = null;
        try {
            content = fxmlLoader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return content;
    }

    @Override
    public void view() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
