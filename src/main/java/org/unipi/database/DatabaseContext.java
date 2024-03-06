package org.unipi.database;

import org.unipi.Main;
import org.unipi.reflection.FileHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseContext{

    private DatabaseStrategyInterface strategy;
    private DatabaseContext(){}
    private static class DatabaseContextHolder {
        static DatabaseContext databaseContext = new DatabaseContext();
    }
    public static DatabaseContext getInstance() {
        return DatabaseContextHolder.databaseContext;
    }

    public void setStrategy(DatabaseStrategyInterface strategy) {
        this.strategy = strategy;
    }

    public String getConnectionString(String dbName) {
        return strategy.getConnectionString(dbName);
    }

    public List<Class<?>> mapColumnType(String columnType){
        return strategy.mapColumnType(columnType);
    }
    public String getColumnType(String fieldType){
        return strategy.getColymnType(fieldType);
    }

}
