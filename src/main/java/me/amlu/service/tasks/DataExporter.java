/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service.tasks;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

public class DataExporter {

    public <T> ByteArrayInputStream exportToCsv(List<T> data, Function<T, String[]> rowMapper) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out),
                     CSVFormat.Builder.create().setHeader(getHeadersFromEntity(data.get(0).getClass())).build())) {

            for (T item : data) {
                csvPrinter.printRecord((Object[]) rowMapper.apply(item));
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private String[] getHeadersFromEntity(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        String[] headers = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            headers[i] = fields[i].getName();
        }
        return headers;
    }
}