/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service.Tasks;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

public class DataExporter {

    public <T> ByteArrayInputStream exportToCsv(List<T> data, String[] headers, Function<T, String[]> rowMapper) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out),
                CSVFormat.Builder.create().setHeader(headers).build())) {
            for (T item : data) {
                printer.printRecord((Object) rowMapper.apply(item));
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}