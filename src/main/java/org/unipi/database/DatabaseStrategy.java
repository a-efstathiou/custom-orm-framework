package org.unipi.database;

import java.sql.Connection;

//Strategy design pattern
public interface DatabaseStrategy {
    void createDatabase(String dbName);
    void connect(String dbName);

    void disconnect(Connection connection);
}
