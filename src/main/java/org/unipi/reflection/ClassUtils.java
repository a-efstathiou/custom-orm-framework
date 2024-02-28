package org.unipi.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ClassUtils {

    //H κλάση αυτή είναι υπεύθυνη για ό,τι έχει σχέση με reflection στις κλάσεις


    public Class<?> getInputClass(String fullyQualifiedName) {
        try {
            // Dynamically load the class
            return Class.forName(fullyQualifiedName);

        } catch (ClassNotFoundException e) {
            System.err.println("Class " + fullyQualifiedName + " not found.");
            e.printStackTrace();
        }

        return null;
    }

    public Object constructClassObject(Class<?> c){
        try {

            Field[] fields = getDeclaredFields(c);

            Arrays.stream(fields)
                    .flatMap(field -> Arrays.stream(field.getDeclaredAnnotations()))
                    .filter(annotation -> annotation instanceof org.unipi.annotations.Field )
                    .forEach(annotation -> {
                        // Process the annotation
                        System.out.println("Annotation on field: " + annotation.annotationType().getSimpleName());
                    });


            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Field[] getDeclaredFields(Class<?> c){
        return c.getDeclaredFields();
    }

}
