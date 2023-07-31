package org.ecsail.static_tools;

import org.ecsail.dto.SchemaDTO;

import java.util.*;
import java.util.stream.*;

public class SchemaToSql {

    public static List<String> convertToSql(List<SchemaDTO> schemas) {
        // Group by Table
        Map<String, List<SchemaDTO>> tableMap = schemas.stream().collect(Collectors.groupingBy(SchemaDTO::getTable));

        List<String> createTableStatements = new ArrayList<>();

        for (String tableName : tableMap.keySet()) {
            List<SchemaDTO> columns = tableMap.get(tableName);

            StringJoiner columnSql = new StringJoiner(",\n  ");

            List<String> primaryKeys = new ArrayList<>();
            List<String> uniqueKeys = new ArrayList<>();

            for (SchemaDTO column : columns) {
                String columnDefinition = String.format("`%s` %s", column.getColumn(), column.getColumnType());
                if (column.getIsNullable().equalsIgnoreCase("NO")) {
                    columnDefinition += " NOT NULL";
                }

                if (column.getDefaultValue() != null && !column.getDefaultValue().equalsIgnoreCase("null")) {
                    columnDefinition += " DEFAULT " + column.getDefaultValue();
                }

                if (column.getExtra() != null && !column.getExtra().isEmpty()) {
                    columnDefinition += " " + column.getExtra();
                }

                columnSql.add(columnDefinition);

                if (column.getKey().equalsIgnoreCase("PRI")) {
                    primaryKeys.add(column.getColumn());
                } else if (column.getKey().equalsIgnoreCase("UNI")) {
                    uniqueKeys.add(column.getColumn());
                }
            }

            if (!primaryKeys.isEmpty()) {
                columnSql.add(String.format("PRIMARY KEY (`%s`)", String.join("`, `", primaryKeys)));
            }

            for (String uniqueKey : uniqueKeys) {
                columnSql.add(String.format("UNIQUE KEY (`%s`)", uniqueKey));
            }

            String createTableSql = String.format(
                    "CREATE TABLE `%s` (\n  %s\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;",
                    tableName, columnSql.toString());

            createTableStatements.add(createTableSql);
        }

        return createTableStatements;
    }
}
