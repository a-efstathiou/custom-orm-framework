package org.unipi.database;

import org.unipi.annotations.Required;

import java.sql.Connection;
import java.util.List;

//Strategy design pattern
    public interface DatabaseStrategyInterface {
    String getConnectionString(); //returns the appropriate connection-string

    /*StringBuilder createDatabase(@Required(type = "String")String dbName);
    void connect(@Required(type = "String")String dbName);

    void disconnect(Connection connection);*/

    // We use mapColumnType to know what to use with get in the resultSet in the selectAll query in the db.
    // It is also used to check for invalid inputs in the type of the Field Annotation.
    List<Class<?>> mapColumnType(String columnType);


}
