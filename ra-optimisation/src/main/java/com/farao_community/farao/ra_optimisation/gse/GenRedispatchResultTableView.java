package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.RedispatchElementResult;
import com.farao_community.farao.ra_optimisation.RemedialActionResult;
import com.powsybl.gse.spi.GseContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public final class GenRedispatchResultTableView extends TableView implements Initializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("lang.RaoComputationResultViewer");

    @FXML
    private TableView<RedispatchElementResult> genRedispatchTable;

    @FXML
    private TableColumn<RedispatchElementResult, String> idTabColumn;

    @FXML
    private TableColumn<RedispatchElementResult, Number> preOptimisationTargetPTabColumn;

    @FXML
    private TableColumn<RedispatchElementResult, Number> postOptimisationTargetPTabColumn;

    @FXML
    private TableColumn<RedispatchElementResult, Number> redispatchCostTabColumn;

    public GenRedispatchResultTableView(GseContext gseContext) {
    }

    public Node getContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(GenRedispatchResultTableView.class.getResource("/fxml/GenRedispatchResultTableView.fxml"));
        fxmlLoader.setResources(RESOURCE_BUNDLE);
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
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Double cell factory
        Callback<TableColumn<RedispatchElementResult, Number>, TableCell<RedispatchElementResult, Number>> cellFactory = tc -> new TableCell<RedispatchElementResult, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getInstance();
                    format.setMaximumFractionDigits(1);
                    setText(format.format(value));
                }
            }
        };
        Callback<TableColumn<RedispatchElementResult, Number>, TableCell<RedispatchElementResult, Number>> cellCurrencyFactory = tc -> new TableCell<RedispatchElementResult, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || Objects.isNull(value)) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    format.setCurrency(Currency.getInstance("EUR"));
                    setText(format.format(value));
                }
            }
        };

        idTabColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        preOptimisationTargetPTabColumn.setCellValueFactory(new PropertyValueFactory<>("preOptimisationTargetP"));
        preOptimisationTargetPTabColumn.setCellFactory(cellFactory);
        postOptimisationTargetPTabColumn.setCellValueFactory(new PropertyValueFactory<>("postOptimisationTargetP"));
        postOptimisationTargetPTabColumn.setCellFactory(cellFactory);
        redispatchCostTabColumn.setCellValueFactory(new PropertyValueFactory<>("redispatchCost"));
        redispatchCostTabColumn.setCellFactory(cellCurrencyFactory);
    }

    public void selectResults(List<RemedialActionResult> remedialActionResults) {
        // Deal with remedial action elements table
        ObservableList<RedispatchElementResult> redispatchElementResults = FXCollections.observableArrayList(
                remedialActionResults.stream()
                        .filter(RemedialActionResult::isApplied)
                        .flatMap(remedialActionResult -> remedialActionResult.getRemedialActionElementResults().stream())
                        .filter(rae -> rae instanceof RedispatchElementResult)
                        .map(rae -> (RedispatchElementResult) rae).collect(Collectors.toList())
        );
        if (!redispatchElementResults.isEmpty()) {
            genRedispatchTable.setItems(redispatchElementResults);
            genRedispatchTable.refresh();
        }
    }
}
