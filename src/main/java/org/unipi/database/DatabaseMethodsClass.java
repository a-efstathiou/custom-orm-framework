package org.unipi.database;

import org.unipi.annotations.Required;

public class DatabaseMethodsClass {

    public StringBuilder createDatabase(String dbName) {
        StringBuilder createDBString = new StringBuilder("CREATE DATABASE "+dbName);
        return createDBString;
    }

    public StringBuilder delete(@Required(type = "String") String field, @Required(type = "String")String value){
        StringBuilder createDBString = new StringBuilder(
                "DELETE FROM Customers WHERE "+field+"=="+value);
        return createDBString;
    }
}
