package org.unipi.database;

import org.unipi.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyDatabaseStrategy implements DatabaseStrategyInterface{

    //We use a design pattern to create a unique instance of the DerbyDatabaseStrategy using
    //Singleton Lazy Initialization with on-demand holder
    private DerbyDatabaseStrategy(){}
    private static class DerbyDatabaseStrategyHolder {
        static DerbyDatabaseStrategy derbyDatabaseStrategy = new DerbyDatabaseStrategy();
    }
    public static DerbyDatabaseStrategy getInstance() {
        return DerbyDatabaseStrategyHolder.derbyDatabaseStrategy;
    }


    @Override
    public StringBuilder createDatabase(String dbName) {
        StringBuilder createDBString = new StringBuilder("CREATE DATABASE "+dbName);
        return createDBString;
    }

    @Override
    public Connection connect(String dbName) {
        String connectionString = "jdbc:derby:"+dbName+";create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDatabaseStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
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
    }

    @Override
    public void selectAll(String tableName, String dbName) {
        try {
            Connection connection = connect(dbName);
            Statement statement = connection.createStatement();
            String selectSQL = "SELECT * FROM "+tableName;
            ResultSet resultSet = statement.executeQuery(selectSQL);

            //Metadata για το ResultSet (παίρνεις περιγραφή της στήλης)
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            //Πρέπει να αλλάξουμε το List<String> με List<Input Class> όπου
            //Input class είναι η κλάση του εκάστοτε αρχείου που δίνει ο user.
            List<String> resultList = new ArrayList<>();


            while(resultSet.next()){
                //Δημιουργία instance από το Input
                //Input dataObject = new Input();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);

//                    //Reflection για να πάρουμε πίσω τα fields.
//                    Field field = Input.class.getDeclaredField(columnName);
//                    field.setAccessible(true);
//                    field.set(dataObject, columnValue);
                }

//                resultList.add(dataObject); // Add the populated data object to the result list
            }
            statement.close();
            disconnect(connection);
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(DerbyDatabaseStrategy.class.getName()).log(Level.SEVERE, "Error selecting all from table: "+tableName, ex);
        }
    }

    @Override
    public void createTable(String tableName) {

    }
}
