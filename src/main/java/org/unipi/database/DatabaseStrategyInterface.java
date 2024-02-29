package org.unipi.database;

import org.unipi.annotations.Required;

import java.sql.Connection;

//Strategy design pattern
public interface DatabaseStrategyInterface {
    String getConnectionString(); //returns the appropriate connection-string

    /*StringBuilder createDatabase(@Required(type = "String")String dbName);
    void connect(@Required(type = "String")String dbName);

    void disconnect(Connection connection);*/

    Class<?> mapColumnType(String columnType);


}
