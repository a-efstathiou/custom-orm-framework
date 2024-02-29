package org.unipi.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class H2DatabaseStrategy implements DatabaseStrategyInterface{
    @Override
    public String getConnectionString() {
        //Need to add dbName from annotation
        String dbName= "Database";
        return "jdbc:h2:"+dbName+";create=true";
    }

    @Override
    public Class<?> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INT" -> int.class;
            case "BIGINT" -> long.class;
            case "SMALLINT" -> short.class;
            case "TINYINT" -> byte.class;
            case "DECIMAL" -> BigDecimal.class;
            case "REAL" -> float.class;
            case "DOUBLE" -> double.class;
            case "CHAR", "VARCHAR", "LONGVARCHAR" -> String.class;
            case "BOOLEAN" -> boolean.class;
            case "DATE" -> Date.class;
            case "TIME" -> Time.class;
            case "TIMESTAMP" -> Timestamp.class;
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }

}
