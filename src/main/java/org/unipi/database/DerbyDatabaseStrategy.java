package org.unipi.database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyDatabaseStrategy implements DatabaseStrategyInterface{



    @Override
    public String getConnectionString(String dbName) {
        return "jdbc:derby:"+dbName+";create=true";
    }
   /* @Override
    public StringBuilder createDatabase(String dbName) {
        StringBuilder createDBString = new StringBuilder("CREATE DATABASE "+dbName);
        return createDBString;
    }

    @Override
    public void connect(String dbName) {

    }

    @Override
    public void disconnect(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the Derby database");
            } else {
                System.out.println("Connection was already closed or null");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DerbyDatabaseStrategy.class.getName()).log(Level.SEVERE, "Error disconnecting from the database", ex);
        }
    }*/

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
            /*case "SMALLINT" -> List.of(short.class , int.class);
            case "TINYINT" -> List.of(byte.class, boolean.class);
            case "NUMERIC","DECIMAL","DEC" -> List.of(BigDecimal.class);
            case "REAL" -> List.of(float.class);
           */ case "DOUBLE" -> "DOUBLE";
            //case "CHARACTER","CHAR", "VARCHAR", "LONGVARCHAR" -> List.of(String.class);
            case "STRING" -> "VARCHAR(20)";
            case "BOOLEAN" -> "BOOLEAN";
            case "DATE" -> "DATE";
            case "DATETIME" -> "TIME";
            //case "TIMESTAMP" -> List.of(Timestamp.class);
            default -> throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        };
    }

}
