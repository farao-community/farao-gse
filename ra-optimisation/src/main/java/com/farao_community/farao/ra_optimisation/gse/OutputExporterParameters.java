/**
 * Copyright (c) 2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.ra_optimisation.gse;

import com.farao_community.farao.ra_optimisation.converter.RaoComputationResultExporter;
import com.farao_community.farao.ra_optimisation.converter.RaoComputationResultExporters;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Rao computation results export parameters
 *
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
class OutputExporterParameters {
    private String filePath;
    private String exporter;

    Path getFilePath() {
        return Paths.get(filePath);
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public RaoComputationResultExporter getExporter() {
        return RaoComputationResultExporters.getExporter(exporter);
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }
}
