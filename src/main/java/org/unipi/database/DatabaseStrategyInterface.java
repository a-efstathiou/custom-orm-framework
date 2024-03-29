package org.unipi.database;


import java.sql.Connection;
import java.util.List;

    //Strategy design pattern. We use the strategy design pattern because we don't know what the database type is from the start of the program.
    //At runtime using reflection we get the database type and set the strategy using the DatabaseContext class. It is not
    // a "pure" strategy design pattern, as the different db operations are on the DatabaseMethodsClass. We explain the difference
    // between our implementation in the DatabaseMethodsClass.
    public interface DatabaseStrategyInterface {

        //returns the appropriate connection-string
        String getConnectionString(String dbName);

        // We use mapColumnType to know what to use with get in the resultSet in the selectAll query in the db.
        // It is also used to check for invalid inputs in the type of the Field Annotation. It returns a List with all
        // acceptable classes.
        List<Class<?>> mapColumnType(String columnType);

        //does the opposite of mapColumnType. We use it to get the Column Types of the Database from a String
        String getColymnType(String fieldType);


    }
