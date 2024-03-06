package org.unipi.reflection;

import org.unipi.database.DatabaseMethodsClass;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {

    //Η κλάση αυτή θα είναι υπεύθυνη για τα αρχεία.

    String outputDirectoryPath = "src/main/java/org/unipi/output/";
    ReflectionHandler rh = ReflectionHandler.getInstance();


    private FileHandler(){}
    private static class FileHandlerHolder {
        static FileHandler fileHandler = new FileHandler();
    }
    public static FileHandler getInstance() {
        return FileHandlerHolder.fileHandler;
    }

    public void writeOutput(BufferedWriter writer, String outputString){

        try {
            // Write content to the file
            writer.write(outputString);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error writing in Output file", ex);
        }
    }

    public void initializeFile(BufferedWriter writer,String className){
        StringBuilder stringBuilder = new StringBuilder();
        String imports = getImports();
        // Write content to the file
        stringBuilder
                .append("package org.unipi.output;\n\n")
                .append(imports)
                .append("public class ").append(className).append(" {\n")
        ;
        writeOutput(writer,stringBuilder.toString());

    }

    public void finalizeFile(BufferedWriter writer){
        writeOutput(writer,"}");
        closeBufferedWriter(writer);
    }

    public void handleInputFiles(){
        String filePath = "src/main/java/org/unipi/input/";
        File directory = new File(filePath);
        File[] files = directory.listFiles();


        if (files != null && files.length > 0) {
            // Iterating through the files
            for (File file : files) {
                // Extracting the name of each file
                String fileName = file.getName();
                try {
                    // Extracting the file name without extension
                    String fileNameWithoutExtention = fileName.substring(0, fileName.lastIndexOf('.'));
                    String fullyQualifiedName = "org.unipi.input."+fileNameWithoutExtention;
                    System.out.println("Fully Qualified Class Name: " + fullyQualifiedName);


                    BufferedWriter writer = createBufferedWriter(fileName);
                    //Create output file and initialize it
                    createOutputFile(writer, fileName);
                    initializeFile(writer,fileNameWithoutExtention);
                    // Output the file name
                    System.out.println("File Name: " + fileName);

                    //Reflection
                    Class<?> c = rh.getInputClass(fullyQualifiedName);
                    rh.reflection(c,fileName);

                    List<String> fieldsStringList = rh.getFieldsString(c);
                    Map<String,String> columns = rh.getDatabaseColumns(c);
                    String tableName = rh.getTableName(c);
                    String dbName = rh.getDatabaseName(c);



                    //Write fields
                    writeFields(writer,fieldsStringList);

                    //Write main method
                    writeMainMethod(writer);

                    //Create constructor with only fields annotated with @Field
                    List<String> parameters =createConstructor(writer,columns,fileNameWithoutExtention);

                    //Write Create Table Method
                    writeCreateMethod(writer, c,tableName,fileNameWithoutExtention);
                    //Write DB Methods
                    writeMethods(writer, c,tableName,columns,fileNameWithoutExtention,parameters);
                    //Write connect method
                    writeConnectMethod(writer,fileNameWithoutExtention,dbName);
                    //Close the bufferedWriter
                    finalizeFile(writer);
                }
                catch (IOException  ex) {
                    Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error handling output file", ex);
                }



            }
        } else {
            throw new IllegalArgumentException("At least one input file must be in the input package.");
        }
    }

    public void createOutputFile(BufferedWriter writer, String fileName){

        try {
            writer.write("");
            System.out.println("Output file "+fileName+" created successfully.");
        } catch (IOException ex) {
            // Handle any errors that may occur during the file creation process
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error creating the output file", ex);
        }

    }

    public BufferedWriter createBufferedWriter(String fileName) throws IOException {
        return new BufferedWriter(new FileWriter(outputDirectoryPath + fileName,false));
    }

    public void closeBufferedWriter(BufferedWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error closing BufferedWriter", ex);
            }
        }
    }

    private void writeFields(BufferedWriter writer,List<String> fieldsStringList) {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("\n");
        for(String field : fieldsStringList){
            stringbuilder.append("\t").append(field);
        }
        stringbuilder.append("\n");

        writeOutput(writer,stringbuilder.toString());
    }

    public List<String> createConstructor(BufferedWriter writer,Map<String,String> fields,String className) {
        StringBuilder sb = new StringBuilder();
        List <String> parameters = new ArrayList<>();
        sb.append("\t").append("public ").append(className).append("(");
        fields.entrySet()
                        .forEach( entry -> {
                            String type = entry.getValue();
                            // Split the string based on one or more whitespace characters
                            String[] parts = type.split("\\s+");
                            // Retrieve the first part from the array
                            String firstPart = parts[0];
                            //Make only the 1st character of the string Upper Case
                            String lastPart = parts[1];
                            sb.append(type).append(",");
                            parameters.add(lastPart);
                        });
        sb.deleteCharAt(sb.length()-1).append(") {\n");
        parameters.forEach( param ->  sb.append("\t\t\t this.").append(param).append(" = ").append(param).append(";\n"));
        sb.append("\t}\n\n");

        writeOutput(writer,sb.toString());

        return parameters;
    }

    private void writeCreateMethod(BufferedWriter writer, Class<?> c,String tableName,String className){
        List<FieldClass> allFieldClass = rh.getAllFieldClass(c.getDeclaredFields());
        StringBuilder methodStringBuilder = DatabaseMethodsClass.createTable(allFieldClass,tableName,className);
        System.out.println(methodStringBuilder);
        writeOutput(writer,methodStringBuilder.toString());

    }

    private void writeMethods(BufferedWriter writer, Class<?> c,String tableName,Map<String,String> dataColumn,String className,List<String> parameters){
        List<MethodClass> allMethods = rh.getMethods(c, c.getDeclaredFields());
        StringBuilder sb = new StringBuilder();
        for(MethodClass method : allMethods){
            if(method.getDbMethodType().equalsIgnoreCase("deleteOne")){
                sb.append(DatabaseMethodsClass.delete(method, tableName, className));
                sb.append("\n");
            }
            else if(method.getDbMethodType().equalsIgnoreCase("SelectAll")){
                sb.append(DatabaseMethodsClass.selectAll(method,tableName,dataColumn,className,parameters));
                sb.append("\n");
            }
        }

        writeOutput(writer,sb.toString());
    }

    private void writeConnectMethod(BufferedWriter writer,String className,String dbName){
        writeOutput(writer,DatabaseMethodsClass.getConnectMethodString(className,dbName));
    }

    private String getImports(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nimport java.sql.*;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.logging.Level;\n" +
                "import java.util.logging.Logger;\n");

        return sb.toString();
    }

    private void writeMainMethod(BufferedWriter writer){
        String main = "\tpublic static void main(String[] args) {\n" +
                "\t\t\n" +
                "\t}\n\n";
        writeOutput(writer,main);
    }



}
