package org.unipi.database;

public class SQLiteDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString() { ///////////////   WRONG STRING
        return "jdbc:sqlite:javaprotal5.db";
    }
}
