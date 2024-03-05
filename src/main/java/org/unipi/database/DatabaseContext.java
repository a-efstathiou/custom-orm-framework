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

    public String getConnectionString() {
        return strategy.getConnectionString();
    }

    public List<Class<?>> mapColumnType(String columnType){
        return strategy.mapColumnType(columnType);
    }
    public String getColumnType(String fieldType){
        return strategy.getColymnType(fieldType);
    }


    public Connection connect(String dbName) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(getConnectionString());
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void disconnect(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the database");
            } else {
                System.out.println("Connection was already closed or null");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DerbyDatabaseStrategy.class.getName()).log(Level.SEVERE, "Error disconnecting from the database", ex);
        }
    }
}
