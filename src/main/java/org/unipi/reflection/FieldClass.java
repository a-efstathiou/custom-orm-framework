package org.unipi.reflection;

import java.util.function.BinaryOperator;

//Field class represents the Field
// and includes only characteristics
// that are saved in the DB
public class FieldClass {
    //The name of the field and its type on the DB.
    //E.g. name, Text.
    private String name, columnType;

    //Booleans that indicate if the field is PK, Unique or Not Null.
    private boolean isPrimaryKey, isUnique, isNotNull;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }
}