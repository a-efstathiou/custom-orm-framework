package org.unipi.database;

import java.util.List;


//This is the class that is responsible for calling the strategy methods.
public class DatabaseContext{

    private DatabaseStrategyInterface strategy;

    //We use a design pattern to create a unique instance of DatabaseContext using
    //Singleton Lazy Initialization with on-demand holder
    private DatabaseContext(){}
    private static class DatabaseContextHolder {
        static DatabaseContext databaseContext = new DatabaseContext();
    }
    public static DatabaseContext getInstance() {
        return DatabaseContextHolder.databaseContext;
    }


    //Sets the strategy
    public void setStrategy(DatabaseStrategyInterface strategy) {
        this.strategy = strategy;
    }

    //Returns the connection string
    public String getConnectionString(String dbName) {
        return strategy.getConnectionString(dbName);
    }

    //maps the type of the field to a database column type
    public List<Class<?>> mapColumnType(String columnType){
        return strategy.mapColumnType(columnType);
    }

    //does the reverse of mapColumnTypes
    public String getColumnType(String fieldType){
        return strategy.getColymnType(fieldType);
    }

}
