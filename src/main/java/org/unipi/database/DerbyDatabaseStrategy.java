package org.unipi.database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Strategy for Derby Database
public class DerbyDatabaseStrategy implements DatabaseStrategyInterface{



    @Override
    public String getConnectionString(String dbName) {
        return "jdbc:derby:"+dbName+";create=true";
    }


    @Override
    public List<Class<?>> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INTEGER" -> List.of(int.class);
            case "BIGINT" -> List.of(long.class);
            case "SMALLINT" -> List.of(short.class);
            case "DECIMAL" -> List.of(BigDecimal.class);
            case "REAL" -> List.of(float.class);
            case "DOUBLE" -> List.of(double.class);
            case "CHAR", "VARCHAR", "LONGVARCHAR" -> List.of(String.class);
            case "DATE" -> List.of(Date.class);
            case "TIME" -> List.of(Time.class);
            case "BOOLEAN" -> List.of(boolean.class);
            case "TIMESTAMP" -> List.of(Timestamp.class);
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }

    @Override
    public String getColymnType(String fieldType) {
        String colType = fieldType.toUpperCase();
        return switch (colType) {
            case "INT","INTEGER" -> "INT";
            case "LONG" -> "BIGINT";
            case "DOUBLE" -> "DOUBLE";
            case "STRING" -> "VARCHAR(20)";
            case "BOOLEAN" -> "BOOLEAN";
            case "DATE" -> "DATE";
            case "DATETIME" -> "TIME";
            default -> throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        };
    }

}
