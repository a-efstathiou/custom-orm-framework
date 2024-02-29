package org.unipi.reflection;

import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.database.DatabaseMethodsClass;
import org.unipi.input.Input;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionHandler {

    FileHandler fh = FileHandler.getInstance();


    private ReflectionHandler(){}
    private static class ReflectionHandlerHolder {
        static ReflectionHandler reflectionHandler = new ReflectionHandler();
    }
    public static ReflectionHandler getInstance() {
        return ReflectionHandlerHolder.reflectionHandler;
    }

    public void reflection(Class<?> c,String outputFileName){
        //List to store the fields.
        // Create a HashMap to store name and type associations
        Map<String, String> fieldMap = new HashMap<>();


            //Reflection to read annotations
            Annotation[] annotations = c.getAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof Database dbAnnotation){
                    System.out.println(dbAnnotation.name());
                    System.out.println(dbAnnotation.type());
                }
                if(annotation instanceof Field fieldAnnotation){
                    System.out.println();
                    System.out.println(fieldAnnotation.name());
                    System.out.println(fieldAnnotation.type());


                    String name = fieldAnnotation.name();
                    String type = fieldAnnotation.type();

                    fieldMap.put(name, type);
                }
            }

            //Replace tableName with the name taken from reflection
            System.out.println(DatabaseMethodsClass.selectAll("Students",outputFileName,fieldMap));


    }

    public List<String> getFieldsString(Class<?> c){
        List<String> fieldsString = new ArrayList<>();

        //Reflection to get Fields
        java.lang.reflect.Field[] declaredFields = c.getDeclaredFields();

        for(java.lang.reflect.Field f : declaredFields){
            Class<?> type = f.getType();
            String name = f.getName();
            String modifier = Modifier.toString(f.getModifiers());
            fieldsString.add(modifier + " "+type.getSimpleName()+" "+name+ ";\n");
        }

        return fieldsString;
    }

    public Class<?> getInputClass(String fullyQualifiedName){
        try {
            // Dynamically load the class
            return Class.forName(fullyQualifiedName);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }


}
