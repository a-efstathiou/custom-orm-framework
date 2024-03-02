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

    public static String selectAll(String tableName, Map<String,String> dataColumn,String className,List<String> parameters){

        StringBuilder finalStringBuilder = new StringBuilder();

        finalStringBuilder
                .append("private List<").append(className).append("> selectAll(){\n")
                .append("\tList<").append(className).append("> list = new ArrayList<>();\n\n")
                .append("    try {\n")
                .append("        Connection connection = connect();\n")
                .append("        Statement statement = connection.createStatement();\n")
                .append("        String selectSQL = \"select * from ").append(tableName).append("\";\n")
                .append("        ResultSet resultSet = statement.executeQuery(selectSQL);\n")
                .append("        while(resultSet.next()){\n");

        dataColumn.entrySet()
                .forEach(entry -> {

                    String type = entry.getValue();

                    // Split the string based on one or more whitespace characters
                    String[] parts = type.split("\\s+");
                    // Retrieve the first part from the array
                    String firstPart = parts[0];
                    //Make only the 1st character of the string Upper Case
                    String result = firstPart.substring(0, 1).toUpperCase() + firstPart.substring(1);

                    finalStringBuilder.append("        \t")
                            .append(entry.getValue()).append(" = ")
                            .append("resultSet.get").append(result).append("(\"").append(entry.getKey()).append("\");\n");
                });
        finalStringBuilder
                .append("\t\t\t").append(className).append(" ").append(className.toLowerCase()).append("1 = ").append("new ")
                .append(className).append("(");
        parameters.forEach(param -> finalStringBuilder.append(param).append(","));
        finalStringBuilder.deleteCharAt(finalStringBuilder.length()-1).append(");\n");

        finalStringBuilder
                .append("        }\n")
                .append("        statement.close();\n")
                .append("        connection.close();\n")
                .append("        System.out.println(\"Done!\");\n")
                .append("    } catch (SQLException ex) {\n")
                .append("        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("    }\n")
                .append("\treturn list;\n")
                .append("}");






        return finalStringBuilder.toString();
    }

    public static String getConnectMethodString(){
        StringBuilder stringbuilder = new StringBuilder();

        String connectionString = databaseContext.getConnectionString();

        stringbuilder.append("    private static Connection connect() {\n");
        stringbuilder.append("        String connectionString = \"").append(connectionString).append("\";\n");
        stringbuilder.append("        Connection connection = null;\n");
        stringbuilder.append("        try {\n");
        stringbuilder.append("            connection = DriverManager.getConnection(connectionString);\n");
        stringbuilder.append("        } catch (SQLException ex) {\n");
        stringbuilder.append("            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n");
        stringbuilder.append("        }\n");
        stringbuilder.append("        return connection;\n");
        stringbuilder.append("    }\n");
        stringbuilder.append("\n");

        return stringbuilder.toString();
    }

}
