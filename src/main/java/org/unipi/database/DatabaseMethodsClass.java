package org.unipi.database;


import org.unipi.reflection.FieldClass;
import org.unipi.reflection.MethodClass;

import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

public class DatabaseMethodsClass {

    static DatabaseContext databaseContext = DatabaseContext.getInstance();

    public static StringBuilder createTable(List<FieldClass> allFieldsClass, String tableName,String className) {
        StringBuilder createTableBuilder = new StringBuilder();
        int counter = 0 ;

        createTableBuilder
                .append("\tprivate static void createTableAndData(){\n")
                .append("\t\ttry {\n")
                .append("\t\t\tConnection connection = connect();\n")
                .append("\t\t\tString createTableSQL = \"CREATE TABLE ").append(tableName).append("\"\n");

        for(FieldClass field : allFieldsClass){


            createTableBuilder.append("\t\t\t\t\t+ \"");
            if(counter == 0){
                createTableBuilder.append("(");
                ++counter;
            }
            createTableBuilder.append(field.getName()).append(" ")
                    .append(field.getColumnType()).append(" ");
            if(field.isUnique()){
                createTableBuilder.append(" UNIQUE");
            }
            if(field.isNotNull()){
                createTableBuilder.append(" NOT NULL");
            }
            if(field.isPrimaryKey()){
                createTableBuilder.append(" PRIMARY KEY");
            }
            createTableBuilder.append(",\"\n");
        }
        createTableBuilder.delete(createTableBuilder.length() - 3, createTableBuilder.length()).append(")\";\n");


        createTableBuilder.append("\t\t\tStatement statement = connection.createStatement();\n")
                .append("\t\t\tstatement.executeUpdate(createTableSQL);\n")
                .append("\t\t\tstatement.close();\n")
                .append("\t\t\tconnection.close();\n")
                .append("\t\t} catch (SQLException ex) {\n")
                .append("\t\t\tLogger.getLogger(").append(className).append(".class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("\t\t}\n")
                .append("\t}\n");

        return createTableBuilder;

    }

    public static <T> StringBuilder delete(MethodClass methodClass, String tableName,String className){
        StringBuilder deleteDBString = new StringBuilder();
        deleteDBString.append("\t").append(methodClass.getModifier()).append(" static ")
                .append(methodClass.getReturnType()).append(" ")
                .append(methodClass.getName()).append("(").append(methodClass.getParamType()).append(" ")
                .append(methodClass.getParamName())
                .append("){\n\t   int count=0;\n")
                .append("\t   try {\n")
                .append("\t       Connection connection = connect();\n")
                .append("\t       String insertSQL = \"DELETE FROM ").append(tableName)
                .append(" WHERE ").append(methodClass.getParamName()).append(" = ?")
                .append("\";\n")
                .append("\t       PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);\n")
                .append("\t       preparedStatement.set").append(methodClass.getParamType()).append("(1,").append(String.valueOf(methodClass.getParamName())).append(" );\n")
                .append("\t       count = preparedStatement.executeUpdate();\n")
                .append("\t       if(count>0){\n")
                .append("\t           System.out.println(count+\" record deleted\");\n")
                .append("\t       }\n")
                .append("\t       preparedStatement.close();\n")
                .append("\t       connection.close();\n")
                .append("\t   } catch (SQLException ex) {\n")
                .append("\t       Logger.getLogger(").append(className).append(".class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("\t   }\n")
                .append("\treturn count;\n")
                .append("\t}\n");
        return deleteDBString;
    }

    public static String selectAll(MethodClass methodClass,String tableName, Map<String,String> dataColumn,String className,List<String> parameters){

        StringBuilder finalStringBuilder = new StringBuilder();

        finalStringBuilder
                .append("\t").append(methodClass.getModifier()).append(" static ").append(methodClass.getReturnType())
                .append("<").append(className).append("> ").append(methodClass.getName()).append("(");

                if(methodClass.getParamType() != null && methodClass.getParamName() != null) {
                    finalStringBuilder.append(methodClass.getParamType())
                            .append(" ").append(methodClass.getParamName());
                }
                finalStringBuilder
                .append("){\n")
                .append("\t\tList<").append(className).append("> list = new ArrayList<>();\n\n")
                .append("\t\ttry {\n")
                .append("\t\t\tConnection connection = connect();\n")
                .append("\t\t\tStatement statement = connection.createStatement();\n")
                .append("\t\t\tString selectSQL = \"select * from ").append(tableName).append("\";\n")
                .append("\t\t\tResultSet resultSet = statement.executeQuery(selectSQL);\n")
                .append("\t\t\twhile(resultSet.next()){\n");

        dataColumn.entrySet()
                .forEach(entry -> {

                    String type = entry.getValue();

                    // Split the string based on one or more whitespace characters
                    String[] parts = type.split("\\s+");
                    // Retrieve the first part from the array
                    String firstPart = parts[0];
                    //Make only the 1st character of the string Upper Case
                    String result = firstPart.substring(0, 1).toUpperCase() + firstPart.substring(1);

                    finalStringBuilder.append("\t\t\t\t")
                            .append(entry.getValue()).append(" = ")
                            .append("resultSet.get").append(result).append("(\"").append(entry.getKey()).append("\");\n");
                });
        finalStringBuilder
                .append("\t\t\t\t").append(className).append(" ").append(className.toLowerCase()).append("1 = ").append("new ")
                .append(className).append("(");
        parameters.forEach(param -> finalStringBuilder.append(param).append(","));
        finalStringBuilder.deleteCharAt(finalStringBuilder.length()-1).append(");\n");

        finalStringBuilder
                .append("\t\t\t\tlist.add(input1);\n")
                .append("\t\t\t}\n")
                .append("\t\t\tstatement.close();\n")
                .append("\t\t\tconnection.close();\n")
                .append("\t\t\tSystem.out.println(\"Done!\");\n")
                .append("\t\t} catch (SQLException ex) {\n")
                .append("\t\t\t\tLogger.getLogger(").append(className).append(".class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("\t\t}\n")
                .append("\t\treturn list;\n")
                .append("\t}\n");


        return finalStringBuilder.toString();
    }

    public static String getConnectMethodString(String className,String dbName){
        StringBuilder stringbuilder = new StringBuilder();

        String connectionString = databaseContext.getConnectionString(dbName);

        stringbuilder.append("    private static Connection connect() {\n")
            .append("        String connectionString = \"").append(connectionString).append("\";\n")
            .append("        Connection connection = null;\n")
            .append("        try {\n")
            .append("            connection = DriverManager.getConnection(connectionString);\n")
            .append("        } catch (SQLException ex) {\n")
            .append("            Logger.getLogger(").append(className).append(".class.getName()).log(Level.SEVERE, null, ex);\n")
            .append("        }\n")
            .append("    return connection;\n")
            .append("    }\n")
            .append("\n");

        return stringbuilder.toString();
    }

}
