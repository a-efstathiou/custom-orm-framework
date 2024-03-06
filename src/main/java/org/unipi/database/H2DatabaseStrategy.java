package org.unipi.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

//Strategy for H2 Database
public class H2DatabaseStrategy implements DatabaseStrategyInterface{


    @Override
    public String getConnectionString(String dbName) {
        //Need to add dbName from annotation
        return "jdbc:h2:"+dbName+";create=true";
    }

    @Override
    public List<Class<?>> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INT" -> List.of(int.class);
            case "BIGINT" -> List.of(long.class);
            case "SMALLINT" -> List.of(short.class , int.class);
            case "TINYINT" -> List.of(byte.class, boolean.class);
            case "NUMERIC","DECIMAL","DEC" -> List.of(BigDecimal.class);
            case "REAL" -> List.of(float.class);
            case "DOUBLE" -> List.of(double.class);
            case "CHARACTER","CHAR", "VARCHAR", "LONGVARCHAR" -> List.of(String.class);
            case "BOOLEAN" -> List.of(boolean.class);
            case "DATE" -> List.of(Date.class);
            case "TIME" -> List.of(Time.class);
            case "TIMESTAMP" -> List.of(Timestamp.class);
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }

    @Override
    public String getColymnType(String fieldType) {
        String colType = fieldType.toUpperCase();
        return switch (colType) {
            case "INT" -> "INT";
            case "LONG" -> "BIGINT";
            case "DOUBLE" -> "DOUBLE";
            case "STRING" -> "VARCHAR(20)";
            case "BOOLEAN" -> "BOOLEAN";
            case "DATE" -> "DATE";
            case "DATETIME" -> "TIME";
            default -> throw new IllegalArgumentException("Unsupported column type: " + fieldType);
        };
    }

}
