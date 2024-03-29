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

//This class is responsible for operations on files. It handles the writing on the files and calling the ReflectionHandler
//to get the required parameters for its methods. It is essentially functioning as the main method of the program as everything
//is done in this class.
public class FileHandler {

    String outputDirectoryPath = "src/main/java/org/unipi/output/";
    ReflectionHandler rh = ReflectionHandler.getInstance();


    //We use a design pattern to create a unique instance of FileHandler using
    //Singleton Lazy Initialization with on-demand holder. In this case, since only Main calls for this class, a simple
    //constructor would have done the same work, but we keep the design pattern in favor of sustainability.
    private FileHandler(){}
    private static class FileHandlerHolder {
        static FileHandler fileHandler = new FileHandler();
    }
    public static FileHandler getInstance() {
        return FileHandlerHolder.fileHandler;
    }

    //Writes in the output file
    public void writeOutput(BufferedWriter writer, String outputString){

        try {
            // Write content to the file
            writer.write(outputString);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error writing in Output file", ex);
        }
    }

    //Initializes the file. writes the package on top, the imports and the class name which is taken using i/o methods.
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

    //Closes the file and terminates the BufferedWriter
    public void finalizeFile(BufferedWriter writer){
        writeOutput(writer,"}");
        closeBufferedWriter(writer);
    }

    //Does the main work of the class. Scans the package directory input for files and for each one of them, it creates
    //a new file in the output package directory with the same name as the Input File. It calls the ReflectionHandler to
    //handle reflection related operations.
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

                    //Creates a bufferedWriter for the file
                    BufferedWriter writer = createBufferedWriter(fileName);
                    //Create output file and initialize it
                    createOutputFile(writer, fileName);
                    initializeFile(writer,fileNameWithoutExtention);

                    // Output the file name
                    System.out.println("File Name: " + fileName);

                    //----------Reflection--------------

                    //Get class name
                    Class<?> c = rh.getInputClass(fullyQualifiedName);
                    //check if the number of PrimaryKey Annotations are correctly implemented
                    rh.checkValidNumberOfPK(c);
                    //set the strategy that is going to be used depending on the type of the @Database Annotation
                    rh.setStrategy(c);

                    //Returns a List of all fields
                    List<String> fieldsStringList = rh.getFieldsString(c);
                    //Returns a Map<String, String>. The demand of this implementation is explained in the DatabaseMethodsClass
                    // in the commends of the selectAll Method
                    Map<String,String> columns = rh.getDatabaseColumns(c);
                    //Gets table name and dbName from the annotations
                    String tableName = rh.getTableName(c);
                    String dbName = rh.getDatabaseName(c);

                    //Write fields
                    writeFields(writer,fieldsStringList);

                    //Write main method
                    writeMainMethod(writer);

                    //Create constructor with only fields annotated with @Field
                    List<String> parameters =createConstructor(writer,columns,fileNameWithoutExtention);

                    //Write Create Table Method
                    writeCreateMethod(writer, c,tableName);
                    //Write DB Methods
                    writeMethods(writer, c,tableName,columns,fileNameWithoutExtention,parameters);
                    //Write connect method
                    writeConnectMethod(writer,dbName);
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

    //Creates the output file by writing the 1st line in it.
    public void createOutputFile(BufferedWriter writer, String fileName){

        try {
            writer.write("");
            System.out.println("Output file "+fileName+" created successfully.");
        } catch (IOException ex) {
            // Handle any errors that may occur during the file creation process
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error creating the output file", ex);
        }

    }

    //Creates a BufferedWriter to be used in the methods.
    public BufferedWriter createBufferedWriter(String fileName) throws IOException {
        return new BufferedWriter(new FileWriter(outputDirectoryPath + fileName,false));
    }

    //Closes the chosen BufferedWriter
    public void closeBufferedWriter(BufferedWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, "Error closing BufferedWriter", ex);
            }
        }
    }

    //Writes in the output files the fields of the Input file
    private void writeFields(BufferedWriter writer,List<String> fieldsStringList) {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("\n");
        for(String field : fieldsStringList){
            stringbuilder.append("\t").append(field);
        }
        stringbuilder.append("\n");

        writeOutput(writer,stringbuilder.toString());
    }

    //Creates a constructor only with the fields annotated with @Field. It is used in the selectAll method of the database
    //to create an object which in turn is added to the List and returned to the user
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

    //Writes in the output file the create table Method
    private void writeCreateMethod(BufferedWriter writer, Class<?> c,String tableName){
        List<FieldClass> allFieldClass = rh.getAllFieldClass(c.getDeclaredFields());
        StringBuilder methodStringBuilder = DatabaseMethodsClass.createTable(allFieldClass,tableName);
        writeOutput(writer,methodStringBuilder.toString());

    }

    //Writes the methods annotated with @DBMethod. Future additions should be added here in the for loop.
    private void writeMethods(BufferedWriter writer, Class<?> c,String tableName,Map<String,String> dataColumn,String className,List<String> parameters){
        List<MethodClass> allMethods = rh.getMethods(c, c.getDeclaredFields());
        StringBuilder sb = new StringBuilder();
        for(MethodClass method : allMethods){
            if(method.getDbMethodType().equalsIgnoreCase("deleteOne")){
                sb.append(DatabaseMethodsClass.delete(method, tableName));
                sb.append("\n");
            }
            else if(method.getDbMethodType().equalsIgnoreCase("SelectAll")){
                sb.append(DatabaseMethodsClass.selectAll(method,tableName,dataColumn,className,parameters));
                sb.append("\n");
            }
        }

        writeOutput(writer,sb.toString());
    }

    //Writes the connect method for the database in the output file
    private void writeConnectMethod(BufferedWriter writer,String dbName){
        writeOutput(writer,DatabaseMethodsClass.getConnectMethodString(dbName));
    }

    //Writes the necessary imports for the database methods to work
    private String getImports(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nimport java.sql.*;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n");

        return sb.toString();
    }

    //Writes a main method in the output file
    private void writeMainMethod(BufferedWriter writer){
        String main = "\tpublic static void main(String[] args) {\n" +
                "\t\t\n" +
                "\t}\n\n";
        writeOutput(writer,main);
    }



}
