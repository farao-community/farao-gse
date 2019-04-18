/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.flow_decomposition.gse;

import com.google.common.collect.Table;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class for dealing with Guava table conversion to
 * JavaFX TableView data
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
public final class GuavaTableConverter {

    private GuavaTableConverter() {
    }

    public static <R, C, V> List<TableColumn<R, String>> getColumns(Table<R, C, V> table, Function<C, String> headingConverter, Function<V, String> valueConverter) {
        Set<C> columns = table.columnKeySet();
        List<TableColumn<R, String>> result = new ArrayList<>(columns.size());
        for (C c : columns) {
            TableColumn<R, String> col = new TableColumn<>(headingConverter.apply(c));
            col.setCellValueFactory(cd -> Bindings.createStringBinding(() -> {
                V value = table.get(cd.getValue(), c);
                return value == null ? null : valueConverter.apply(value);
            }));
            result.add(col);
        }
        return result;
    }

    public static <R> TableColumn<R, String> getRowColumn(Table<R, ?, ?> table, String name, Function<R, String> converter) {
        TableColumn<R, String> column = new TableColumn<>(name);
        column.setCellValueFactory(c -> Bindings.createStringBinding(() -> converter.apply(c.getValue())));
        return column;
    }

    public static <R> ObservableList<R> getItems(Table<R, ?, ?> table) {
        return FXCollections.observableArrayList(table.rowKeySet());
    }
}
