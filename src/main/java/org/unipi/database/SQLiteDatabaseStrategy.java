package org.unipi.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class SQLiteDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString() { ///////////////   WRONG STRING
        return "jdbc:sqlite:javaprotal5.db";
    }


    @Override
    public Class<?> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INTEGER" -> int.class;
            case "TEXT" -> long.class;
            case "REAL" -> float.class;
            case "BOOLEAN" -> boolean.class;
            case "BLOB" -> byte[].class;
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }
}
