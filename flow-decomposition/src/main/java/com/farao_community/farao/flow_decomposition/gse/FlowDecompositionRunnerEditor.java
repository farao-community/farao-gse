/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.powsybl.afs.ProjectFile;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileEditor;
import com.farao_community.farao.flow_decomposition.afs.FlowDecompositionRunner;
import javafx.scene.Scene;

/**
 * Flow decomposition runner editor
 *
 * @author Sebastien Murgey <sebastien.murgey at rte-france.com>
 */
public class FlowDecompositionRunnerEditor extends AbstractFlowDecompositionRunnerPane<FlowDecompositionRunner> implements ProjectFileEditor {

    public FlowDecompositionRunnerEditor(FlowDecompositionRunner runner, Scene scene, GseContext context) {
        super(runner, scene, context);
    }

    @Override
    public void edit() {
        nameTextField.setText(node.getName());
        nameTextField.setDisable(true);
        node.getCase().ifPresent(aCase -> caseSelectionPane.nodeProperty().setValue((ProjectFile) aCase));
        node.getCracFileProvider().ifPresent(cracFileProvider -> cracFileProviderSelectionPane.nodeProperty().setValue((ProjectFile) cracFileProvider));
    }

    @Override
    public void saveChanges() {
        node.setCase(caseSelectionPane.nodeProperty().getValue());
        node.setCracFileProvider(cracFileProviderSelectionPane.nodeProperty().getValue());
    }
}
