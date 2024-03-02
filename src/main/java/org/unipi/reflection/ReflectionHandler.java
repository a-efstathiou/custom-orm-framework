package org.unipi.reflection;

import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.annotations.Table;
import org.unipi.database.*;
import org.unipi.input.Input;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionHandler {

    FileHandler fh = FileHandler.getInstance();
    DatabaseContext databaseContext = DatabaseContext.getInstance();

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
        String dbName = "";
        String tableName = "";

            //Reflection to read annotations
            Annotation[] annotations = c.getAnnotations();

            //First loop to set strategy and dbName
            for(Annotation annotation : annotations) {
                System.out.println("Annotation Type: " + annotation.annotationType());
                if (annotation instanceof Database dbAnnotation) {
                    dbName = dbAnnotation.name();
                    System.out.println(dbAnnotation.name());
                    System.out.println(dbAnnotation.type().toLowerCase());

                    setDatabaseStrategy(dbAnnotation.type(), databaseContext);

                }
                if(annotation instanceof Table tableAnnotation) {
                    tableName = tableAnnotation.name();
                }

            }

            //Replace tableName with the name taken from reflection


        System.out.println(DatabaseMethodsClass.getConnectMethodString());

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

    public Map<String,String> getDatabaseColumns(Class<?> c){


        Map<String,String> dataColumn = new HashMap<>();
        //Reflection to get Fields
        java.lang.reflect.Field[] declaredFields = c.getDeclaredFields();

        for(java.lang.reflect.Field field : declaredFields){

            Annotation[] annotations = field.getAnnotations();


            for (Annotation annotation : annotations) {
                if(annotation instanceof Field fieldAnnotation){
                    String fieldType = fieldAnnotation.type();
                    String fieldName = fieldAnnotation.name();
                    try {
                        checkFields(field, fieldType);
                    }
                    catch (IllegalArgumentException ex){
                        ex.printStackTrace();
                    }

                    String name = field.getName();
                    String type = field.getType().getSimpleName();
                    dataColumn.put(fieldName,type+" "+name);
                }
            }

        }


        return dataColumn;
    }

    public Class<?> getInputClass(String fullyQualifiedName){
        try {
            // Dynamically load the class
            return Class.forName(fullyQualifiedName);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private void setDatabaseStrategy(String dbType, DatabaseContext context){
        switch(dbType.toLowerCase()){
            case "derby" -> context.setStrategy(new DerbyDatabaseStrategy());
            case "h2" -> context.setStrategy(new H2DatabaseStrategy());
            case "sqlite" -> context.setStrategy(new SQLiteDatabaseStrategy());
            default -> throw new IllegalArgumentException();
        }
    }

    private void checkFields(java.lang.reflect.Field field, String fieldType){
        List<Class<?>> acceptableFields = databaseContext.mapColumnType(fieldType);
        if(!isFieldTypeOk(field,acceptableFields)){
            throw new IllegalArgumentException("Field type : "+fieldType +" doesn't match the parameter : "+field.getType()+
                    "\nChoose from available types: "+acceptableFields);
        }
    }

    private boolean isFieldTypeOk(java.lang.reflect.Field field, List<Class<?>> acceptableFields){
        return acceptableFields.contains(field.getType());
    }

    public String getTableName(Class<?> c){
        //Reflection to read annotations
        Annotation[] annotations = c.getAnnotations();
        String tableName ="";
        //First loop to set strategy and dbName
        for(Annotation annotation : annotations) {
            if(annotation instanceof Table tableAnnotation) {
                tableName = tableAnnotation.name();
            }

        }
        return tableName;
    }

}
