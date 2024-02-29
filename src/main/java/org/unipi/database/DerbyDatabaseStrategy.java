package org.unipi.database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString() {
        String dbName= "Database";
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
    public Class<?> mapColumnType(String columnType){
        String colType = columnType.toUpperCase();
        return switch (colType) {
            case "INTEGER" -> int.class;
            case "BIGINT" -> long.class;
            case "SMALLINT" -> short.class;
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
