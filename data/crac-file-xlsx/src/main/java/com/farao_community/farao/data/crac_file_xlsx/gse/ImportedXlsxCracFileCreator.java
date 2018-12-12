package com.farao_community.farao.data.crac_file_xlsx.gse;

import com.powsybl.afs.ProjectFolder;
import com.powsybl.gse.spi.GseContext;
import com.powsybl.gse.spi.ProjectCreationTask;
import com.powsybl.gse.spi.ProjectFileCreator;
import com.powsybl.gse.util.NodeSelectionPane;
import com.farao_community.farao.afs.crac_file.xlsx.AfsXlsxCracFile;
import com.farao_community.farao.afs.crac_file.xlsx.ImportedXlsxCracFileBuilder;
import com.farao_community.farao.data.crac_file.xlsx.model.TimesSeries;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Objects;
import java.util.ResourceBundle;
/**
 * @author Marc Erkol {@literal <marc.erkol at rte-france.com>}
 */
public class ImportedXlsxCracFileCreator extends GridPane implements ProjectFileCreator {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.ImportedXlsxCracFile");

    private final ProjectFolder folder;

    private final NodeSelectionPane<AfsXlsxCracFile> cracFileSelectionPane;

    ImportedXlsxCracFileCreator(ProjectFolder folder, Scene scene, GseContext context) {
        this.folder = Objects.requireNonNull(folder);
        Objects.requireNonNull(scene);

        cracFileSelectionPane = new NodeSelectionPane(folder.getFileSystem().getData(), RESOURCE_BUNDLE.getString("File"), true,
                scene.getWindow(), context, AfsXlsxCracFile.class);
        setHgap(5);
        setPrefWidth(450);
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column0, column1);
        add(cracFileSelectionPane.getLabel(), 0, 0);
        add(cracFileSelectionPane.getTextField(), 1, 0);
        add(cracFileSelectionPane.getButton(), 2, 0);
    }

    @Override
    public String getTitle() {
        return RESOURCE_BUNDLE.getString("CracImportConfig");
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public BooleanBinding okProperty() {
        return cracFileSelectionPane.nodeProperty().isNotNull();
    }

    @Override
    public ProjectCreationTask createTask() {
        AfsXlsxCracFile aFile = cracFileSelectionPane.nodeProperty().getValue();
        return new ProjectCreationTask() {
            @Override
            public String getNamePreview() {
                return aFile.getName();
            }

            @Override
            public void run() {
                for (TimesSeries ts : TimesSeries.values()) {
                    folder.fileBuilder(ImportedXlsxCracFileBuilder.class)
                            .withAfsCracFile(aFile)
                            .withName(aFile.getName())
                            .withHour(ts.name())
                            .build();
                }
            }

            @Override
            public void undo() {
                throw new AssertionError("TODO"); // TODO
            }

            @Override
            public void redo() {
                throw new AssertionError("TODO"); // TODO
            }
        };
    }

    @Override
    public void dispose() {
        // nothing to dispose
    }
}
