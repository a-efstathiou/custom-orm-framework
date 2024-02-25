package org.unipi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyDatabaseStrategy implements DatabaseStrategyInterface{

    @Override
    public String getConnectionString() { ///////////////   WRONG STRING
        return "jdbc:derby:javaprotal5d;create=true";
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
}
