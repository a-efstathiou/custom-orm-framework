package org.unipi.database;

import org.unipi.annotations.Required;

import java.sql.Connection;

//Strategy design pattern
public interface DatabaseStrategyInterface {
    StringBuilder createDatabase(@Required(type = "String") String dbName);
    Connection connect(@Required(type = "String") String dbName);

    void disconnect(Connection connection);

    void selectAll(@Required(type = "String") String tableName,String dbName);

    void createTable(@Required(type = "String") String tableName);

}
