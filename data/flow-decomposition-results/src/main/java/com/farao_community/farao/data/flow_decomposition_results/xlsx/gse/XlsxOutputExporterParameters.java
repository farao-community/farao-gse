/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.data.flow_decomposition_results.xlsx.gse;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Flow decomposition results XLSX export parameters
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
class XlsxOutputExporterParameters {

    private static  final String XLSX_EXT = ".xlsx";

    private String filePath;

    Path getFilePath() {
        if (!filePath.toLowerCase().endsWith(XLSX_EXT)) {
            filePath += XLSX_EXT;
        }
        return Paths.get(filePath);
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
