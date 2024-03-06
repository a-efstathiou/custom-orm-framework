package org.unipi.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class SQLiteDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString(String dbName) {
        return "jdbc:sqlite:"+dbName+";create=true";
    }


    @Override
    public List<Class<?>> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INTEGER" -> List.of(int.class);
            case "TEXT" -> List.of(String.class);
            case "REAL" -> List.of(float.class);
            case "BOOLEAN" -> List.of(boolean.class);
            case "BLOB" -> List.of(byte[].class);
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }

    @Override
    public String getColymnType(String fieldType) {
        String colType = fieldType.toUpperCase();
        return switch (colType) {
            case "INT" -> "INTEGER";
            case "LONG" -> "INTEGER";
            /*case "SMALLINT" -> List.of(short.class , int.class);
            case "TINYINT" -> List.of(byte.class, boolean.class);
            case "NUMERIC","DECIMAL","DEC" -> List.of(BigDecimal.class);
            case "REAL" -> List.of(float.class);
           */ case "DOUBLE" -> "REAL";
            //case "CHARACTER","CHAR", "VARCHAR", "LONGVARCHAR" -> List.of(String.class);
            case "STRING" -> "TEXT";
            case "BOOLEAN" -> "BOOLEAN";
            /*case "DATE" -> "DATE";
            case "DATETIME" -> "TIME";*/
            //case "TIMESTAMP" -> List.of(Timestamp.class);
            default -> throw new IllegalArgumentException("Unsupported column type: " + fieldType);
        };
    }
}
