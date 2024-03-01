package org.unipi.reflection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
        // Write content to the file
        stringBuilder
                .append("package org.unipi.output;\n\n")
                .append("public class ").append(className).append(" {\n")
        ;
        writeOutput(writer,stringBuilder.toString());

    }

    public void finalizeFile(BufferedWriter writer){
        writeOutput(writer,"}");
        closeBufferedWriter(writer);
    }

    public void findFileNames(){
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

                    //Write fields
                    writeFields(writer,fieldsStringList);

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
            stringbuilder.append("  ").append(field);
        }
        stringbuilder.append("\n");

        writeOutput(writer,stringbuilder.toString());
    }




}
