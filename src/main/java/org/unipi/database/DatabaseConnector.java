package org.unipi.database;

import org.unipi.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector{
    private String connectionString;
    private DatabaseStrategyInterface databaseStrategyInterface;
    public DatabaseConnector(DatabaseStrategyInterface databaseStrategyInterface){
        this.databaseStrategyInterface = databaseStrategyInterface;
        this.connectionString = databaseStrategyInterface.getConnectionString();

//        if(databaseStrategyInterface instanceof DerbyDatabaseStrategy){
//            databaseStrategyInterface.
//        }
    }

    public Connection connect(String dbName) {
        String connectionString = "jdbc:derby:javaprotal5d;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

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
    }
}
