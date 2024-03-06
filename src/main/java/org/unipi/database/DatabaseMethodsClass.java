package org.unipi.database;

import org.unipi.annotations.Required;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

public class DatabaseMethodsClass {

    static DatabaseContext databaseContext = DatabaseContext.getInstance();

    public static StringBuilder createTable(List<FieldClass> allFieldsClass, String tableName) {
        StringBuilder createTableBuilder = new StringBuilder();
        createTableBuilder.append("try {\n")
                .append("    Connection connection = connect();\n")
                .append("    String createTableSQL = \"CREATE TABLE (\"").append(tableName+"\n");

        for(FieldClass field : allFieldsClass){
            createTableBuilder.append("            + \"").append(field.getName()).append(" ")
                    .append(field.getColumnType()).append(" ");
            if(field.isUnique() == true){
                createTableBuilder.append(" UNIQUE ");
            }
            if(field.isNotNull() == true){
                createTableBuilder.append(" NOT NULL ");
            }

            if(field.isPrimaryKey()==true){
                createTableBuilder.append(" PRIMARY KEY,");
            }
        }
                /*.append("            + \"(ID INTEGER NOT NULL PRIMARY KEY,\"\n")
                .append("            + \"USERNAME VARCHAR(20),\"\n")
                .append("            + \"PASSWORD VARCHAR(20))\";\n")*/
        createTableBuilder.append("    Statement statement = connection.createStatement();\n")
                .append("    statement.executeUpdate(createTableSQL);\n")
                .append("    statement.close();\n")
                .append("    connection.close();\n")
                .append("} catch (SQLException ex) {\n")
                .append("    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("}");

        return createTableBuilder;

    }

    public static <T> StringBuilder delete(MethodClass methodClass, String tableName){
        StringBuilder deleteDBString = new StringBuilder();
        deleteDBString.append(methodClass.getModifier()).append(" ")
                .append(methodClass.getReturnType()).append(" ")
                .append(methodClass.getName()).append("(").append(methodClass.getParamType()).append(" ")
                .append(methodClass.getParamName())
                .append("){\n   int count;\n")
                .append("   try {\n")
                .append("       Connection connection = connect();\n")
                .append("       String insertSQL = \"DELETE FROM ").append(tableName)
                .append(" WHERE ").append(methodClass.getParamName()).append(" == ?")
                .append("\";\n")
                .append("       PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);\n")
                .append("       preparedStatement.setInt(1,").append(String.valueOf(methodClass.getParamName())).append(" );\n")
                .append("       count = preparedStatement.executeUpdate();\n")
                .append("       if(count>0){\n")
                .append("           System.out.println(count+\" record deleted\");\n")
                .append("       }\n")
                .append("       preparedStatement.close();\n")
                .append("       connection.close();\n")
                .append("   } catch (SQLException ex) {\n")
                .append("       Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);\n")
                .append("   }\n")
                .append("      return count;\n")
                .append("}");
        return deleteDBString;
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
