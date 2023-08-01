package org.ecsail.dto;

public class SchemaDTO {
    String table;
    String column;
    String DataType;
    String ColumnType;
    String isNullable;
    String key;
    String extra;
    String defaultValue;
    String comment;
    String referencedTable;
    String referencedColumn;


    public SchemaDTO(String table, String column, String dataType, String columnType, String isNullable, String key,
                     String extra, String defaultValue, String comment, String referencedTable, String referencedColumn) {
        this.table = table;
        this.column = column;
        DataType = dataType;
        ColumnType = columnType;
        this.isNullable = isNullable;
        this.key = key;
        this.extra = extra;
        this.defaultValue = defaultValue;
        this.comment = comment;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public SchemaDTO() {
    }
    public String getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(String referencedTable) {
        this.referencedTable = referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }

    public void setReferencedColumn(String referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public String getColumnType() {
        return ColumnType;
    }

    public void setColumnType(String columnType) {
        ColumnType = columnType;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "SchemaDTO{" +
                "table='" + table + '\'' +
                ", column='" + column + '\'' +
                ", DataType='" + DataType + '\'' +
                ", ColumnType='" + ColumnType + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", key='" + key + '\'' +
                ", extra='" + extra + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}