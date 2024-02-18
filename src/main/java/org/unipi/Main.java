package org.unipi;

import org.unipi.annotations.Database;
import org.unipi.input.Input;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class Main {
    public static void main(String[] args) {
        //Reflection to read annotations
        Class<Input> c = Input.class;
        Annotation[] annotations = c.getAnnotations();
        for(Annotation annotation : annotations){
            if(annotation instanceof Database dbAnnotation){
                System.out.println(dbAnnotation.name());
                System.out.println(dbAnnotation.dbtype());
            }
        }


        //Write file in output package

        String outputDirectoryPath = "src/main/java/org/unipi/output/";
        String fileName = "Output.java"; // Specify the file name

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectoryPath + fileName))) {
            // Write content to the file
            writer.write("package org.unipi.output;");
            writer.newLine(); // Add a new line
            writer.write("public class Output {");
            writer.newLine(); // Add a new line
            //Enter code of the class here
            writer.write("\t public int placeholder;"); // Add a new line
            writer.newLine(); // Add a new line
            writer.write("}"); // Add a new line

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
