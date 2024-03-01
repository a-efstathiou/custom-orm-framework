package org.unipi.database;

import org.unipi.annotations.Required;

import java.util.List;
import java.util.Map;

public class DatabaseMethodsClass {

    static DatabaseContext databaseContext = DatabaseContext.getInstance();

    public StringBuilder createDatabase(String dbName) {
        StringBuilder createDBString = new StringBuilder("CREATE DATABASE "+dbName);
        return createDBString;
    }

    public StringBuilder delete(@Required(type = "String") String field, @Required(type = "String")String value){
        StringBuilder createDBString = new StringBuilder(
                "DELETE FROM Customers WHERE "+field+"=="+value);
        return createDBString;
    }

    public static String selectAll(String tableName,String outputFileName, Map<String,String> fields){

        StringBuilder finalStringBuilder = new StringBuilder();

        finalStringBuilder.append("private static void selectAll(){\n")
                .append("    try {\n")
                .append("        Connection connection = connect();\n")
                .append("        Statement statement = connection.createStatement();\n")
                .append("        String selectSQL = \"select * from ").append(tableName).append("\";\n")
                .append("        ResultSet resultSet = statement.executeQuery(selectSQL);\n")
                .append("        while(resultSet.next()){\n")
                .append("            System.out.println(resultSet.getString(\"USERNAME\")+\",\"+resultSet.getString(\"PASSWORD\"));\n")
                .append("            \n")
                .append("            \n")

                .append("        }\n")
                .append("        statement.close();\n")
                .append("        connection.close();\n")
                .append("        System.out.println(\"Done!\");\n")
                .append("    } catch (SQLException ex) {\n")
                .append("        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("    }\n")
                .append("}");



        return finalStringBuilder.toString();
    }

    public static String getConnectMethodString(){
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("    public static Connection connect() {\n");
        stringbuilder.append("        Connection connection = null;\n");
        stringbuilder.append("        try {\n");
        stringbuilder.append("            connection = DriverManager.getConnection(").append(databaseContext.getConnectionString()).append(");\n");
        stringbuilder.append("        } catch (SQLException ex) {\n");
        stringbuilder.append("            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n");
        stringbuilder.append("        }\n");
        stringbuilder.append("        return connection;\n");
        stringbuilder.append("    }\n");
        stringbuilder.append("\n");

        return stringbuilder.toString();
    }

}
