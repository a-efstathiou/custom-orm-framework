package org.unipi.database;

import org.unipi.annotations.Required;

import java.sql.Connection;

//Strategy design pattern
public interface DatabaseStrategyInterface {
    StringBuilder createDatabase(@Required(type = "String")String dbName);
    void connect(@Required(type = "String")String dbName);

    void disconnect(Connection connection);
}
