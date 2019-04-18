/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flowbased_computation.gse;

import com.powsybl.afs.ProjectFile;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectFileEditor;
import com.farao_community.farao.flowbased_computation.afs.FlowBasedComputationRunner;
import javafx.scene.Scene;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public class FlowBasedComputationRunnerEditor extends AbstractFlowBasedComputationRunnerPane<FlowBasedComputationRunner> implements ProjectFileEditor {

    public FlowBasedComputationRunnerEditor(FlowBasedComputationRunner runner, Scene scene, GseContext context) {
        super(runner, scene, context);
    }

    @Override
    public void edit() {
        nameTextField.setText(node.getName());
        nameTextField.setDisable(true);
        node.getCase().ifPresent(aCase -> caseSelectionPane.nodeProperty().setValue((ProjectFile) aCase));
        node.getCracFileProvider().ifPresent(flowBasedComputationStore -> cracFileProviderSelectionPane.nodeProperty().setValue((ProjectFile) flowBasedComputationStore));
    }

    @Override
    public void saveChanges() {
        node.setCase(caseSelectionPane.nodeProperty().getValue());
        node.setCracFileProvider(cracFileProviderSelectionPane.nodeProperty().getValue());
    }
}
