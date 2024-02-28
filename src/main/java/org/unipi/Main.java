package org.unipi;

import org.unipi.annotations.Database;
import org.unipi.input.Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {

        String filePath = "src/main/java/org/unipi/input/";
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            // Iterating through the files
            for (File file : files) {
                // Extracting the name of each file
                String fileName = file.getName();

                // Output the file name
                System.out.println("File Name: " + fileName);

                // Extracting the file name without extension
                String fileNameWithoutExtention = fileName.substring(0, fileName.lastIndexOf('.'));
                String fullyQualifiedName = "org.unipi.input."+fileNameWithoutExtention;
                System.out.println("Fully Qualified Class Name: " + fullyQualifiedName);

                try {
                    // Dynamically load the class
                    Class<?> c = Class.forName(fullyQualifiedName);

                    //Reflection to read annotations
                    Annotation[] annotations = c.getAnnotations();
                    for(Annotation annotation : annotations){
                        if(annotation instanceof Database dbAnnotation){
                            System.out.println(dbAnnotation.name());
                            System.out.println(dbAnnotation.type());
                        }
                    }

                    Constructor<Input> constructor = (Constructor<Input>) c.getDeclaredConstructor();
                    Object s = constructor.newInstance();
                    s.random();
                    System.out.println("object s: "+ s);


                } catch (ClassNotFoundException e) {
                    System.err.println("Class " + fullyQualifiedName + " not found.");
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            }
        } else {
            System.out.println("Directory is empty or does not exist.");
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
