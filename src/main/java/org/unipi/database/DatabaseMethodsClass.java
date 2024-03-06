package org.unipi.database;


import org.unipi.reflection.FieldClass;
import org.unipi.reflection.MethodClass;
import java.util.List;
import java.util.Map;

//This class is responsible for returning the Strings of the methods related to the database. In a "pure" strategy design
// pattern, this class wouldn't exist, and it would be incorporated in the different strategies. In our cases, H2, Derby
// and SQLite have the same syntax in almost every database operation except the connection string. So we create this class
// that returns the strings that are essentially the same for all strategies instead of repeating code in every strategy.
public class DatabaseMethodsClass {
    //get instance of Database Context
    static DatabaseContext databaseContext = DatabaseContext.getInstance();

    //Returns a StringBuilder with the createTable Method ready to be used. It used the variables taken through reflection
    //to set the appropriate representations in the method.
    public static StringBuilder createTable(List<FieldClass> allFieldsClass, String tableName) {
        StringBuilder createTableBuilder = new StringBuilder();
        int counter = 0 ;

        createTableBuilder
                .append("\tprivate static void createTable(){\n")
                .append("\t\ttry {\n")
                .append("\t\t\tConnection connection = connect();\n")
                .append("\t\t\tString createTableSQL = \"CREATE TABLE ").append(tableName).append("\"\n");

        for(FieldClass field : allFieldsClass){

            //Appends the special column types (UNIQUE, NOT NULL, PRIMARY KEY) if they exist for the given field
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
                .append("\t\t\tSystem.err.println(ex.getMessage());\n")
                .append("\t\t}\n")
                .append("\t}\n");

        return createTableBuilder;

    }

    //Returns a StringBuilder of the delete Method for the database.
    public static <T> StringBuilder delete(MethodClass methodClass, String tableName){
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
                .append("\t       System.err.println(ex.getMessage());\n")
                .append("\t   }\n")
                .append("\treturn count;\n")
                .append("\t}\n");
        return deleteDBString;
    }

    //Returns a String with the selectAll Method for the Database.
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

                //here we use the dataColumn Map that contains <key,value> pairs that are in the form of
        //<name parameter of @Field Annotation , type + name of field>, e.g for the annotated field:
        //@Field(name="Id",type="String")
        //String AM
        //the expected key, value pair is <"Id","String AM">.
        dataColumn.entrySet()
                .forEach(entry -> {

                    String type = entry.getValue();

                    // Split the string based on one or more whitespace characters
                    String[] parts = type.split("\\s+");
                    // Retrieve the first part from the array (e.g. boolean)
                    String firstPart = parts[0];
                    //Make only the 1st character of the string Upper Case (e.g. boolean -> Boolean)
                    String result = firstPart.substring(0, 1).toUpperCase() + firstPart.substring(1);

                    finalStringBuilder.append("\t\t\t\t")
                            .append(entry.getValue()).append(" = ")
                            .append("resultSet.get").append(result).append("(\"").append(entry.getKey()).append("\");\n");
                });

        //Construct a new Object and add that to the list of objects that we return
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
                .append("\t\t\t\tSystem.err.println(ex.getMessage());\n")
                .append("\t\t}\n")
                .append("\t\treturn list;\n")
                .append("\t}\n");


        return finalStringBuilder.toString();
    }

    //Returns the String of the connect Method for the db
    public static String getConnectMethodString(String dbName){
        StringBuilder stringbuilder = new StringBuilder();

        String connectionString = databaseContext.getConnectionString(dbName);

        stringbuilder.append("    private static Connection connect() {\n")
            .append("        String connectionString = \"").append(connectionString).append("\";\n")
            .append("        Connection connection = null;\n")
            .append("        try {\n")
            .append("            connection = DriverManager.getConnection(connectionString);\n")
            .append("        } catch (SQLException ex) {\n")
            .append("            System.err.println(ex.getMessage());\n")
            .append("        }\n")
            .append("    return connection;\n")
            .append("    }\n")
            .append("\n");

        return stringbuilder.toString();
    }

}
