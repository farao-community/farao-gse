/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.powsybl.afs.Project;
import com.powsybl.afs.ProjectFile;
import com.powsybl.afs.ProjectNode;
import com.powsybl.afs.ext.base.ProjectCase;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.util.ProjectNodeSelectionPane;
import com.farao_community.farao.data.crac_file.CracFileProvider;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
abstract class AbstractFlowDecompositionRunnerPane<T extends ProjectNode> extends GridPane {

    protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.FlowDecompositionGse");

    protected final T node;

    protected final TextField nameTextField = new TextField();
    protected final ProjectNodeSelectionPane<ProjectFile> caseSelectionPane;
    protected final ProjectNodeSelectionPane<ProjectFile> cracFileProviderSelectionPane;


    protected AbstractFlowDecompositionRunnerPane(T node, Scene scene, GseContext context) {
        this.node = Objects.requireNonNull(node);

        Project project = node.getProject();
        caseSelectionPane = new ProjectNodeSelectionPane<>(project, RESOURCE_BUNDLE.getString("Case"), true, scene.getWindow(),
                context, ProjectFile.class, ProjectCase.class);
        cracFileProviderSelectionPane = new ProjectNodeSelectionPane<>(project, RESOURCE_BUNDLE.getString("CracFileProvider"), true, scene.getWindow(),
                context, ProjectFile.class, CracFileProvider.class);
        setVgap(5);
        setHgap(5);
        setPrefWidth(450);
        setPrefHeight(150);
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column0, column1);
        add(new Label(RESOURCE_BUNDLE.getString("Name") + ":"), 0, 0);
        add(nameTextField, 1, 0, 2, 1);
        add(caseSelectionPane.getLabel(), 0, 1);
        add(caseSelectionPane.getTextField(), 1, 1);
        add(caseSelectionPane.getButton(), 2, 1);
        add(cracFileProviderSelectionPane.getLabel(), 0, 2);
        add(cracFileProviderSelectionPane.getTextField(), 1, 2);
        add(cracFileProviderSelectionPane.getButton(), 2, 2);
        new ValidationSupport().registerValidator(nameTextField, Validator.createEmptyValidator(RESOURCE_BUNDLE.getString("MandatoryName")));
    }

    public String getTitle() {
        return RESOURCE_BUNDLE.getString("CreateFlowDecomposition");
    }

    public Node getContent() {
        return this;
    }

    public BooleanBinding okProperty() {
        return nameTextField.textProperty().isNotEmpty()
                .and(caseSelectionPane.nodeProperty().isNotNull())
                .and(cracFileProviderSelectionPane.nodeProperty().isNotNull());
    }

    public void dispose() {
        // nothing to dispose
    }
}
