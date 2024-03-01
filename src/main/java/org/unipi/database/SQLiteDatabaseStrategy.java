package org.unipi.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class SQLiteDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString() { ///////////////   WRONG STRING
        return "jdbc:sqlite:javaprotal5.db";
    }


    @Override
    public List<Class<?>> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INTEGER" -> List.of(int.class);
            case "TEXT" -> List.of(long.class);
            case "REAL" -> List.of(float.class);
            case "BOOLEAN" -> List.of(boolean.class);
            case "BLOB" -> List.of(byte[].class);
            default -> throw new IllegalArgumentException("Unsupported column type: " + columnType);
        };

    }
}
