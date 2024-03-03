package org.unipi.reflection;

import org.unipi.annotations.DBMethod;
import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.database.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Create context

            //Reflection to read annotations
            Annotation[] annotations = c.getAnnotations();

            //First loop to set strategy and dbName
            for(Annotation annotation : annotations) {
                System.out.println(annotation.toString());
                if (annotation instanceof Database dbAnnotation) {
                    System.out.println(dbAnnotation.name());
                    System.out.println(dbAnnotation.type().toLowerCase());

                    setDatabaseStrategy(dbAnnotation.type(), databaseContext);

                }

                /*if(annotation instanceof Field fieldAnnotation){
                    System.out.println();
                    System.out.println(fieldAnnotation.name());
                    System.out.println(fieldAnnotation.type());


                    String name = fieldAnnotation.name();
                    String type = fieldAnnotation.type();

                    fieldMap.put(name, type);
                }*/
            }

            getDatabaseColumns(c);
            List<MethodClass> allMethods = getMethods(c);
            for(MethodClass method : allMethods){
                if(method.getDbMethodType().toLowerCase().equals("deleteOne".toLowerCase())){
                    System.out.println(DatabaseMethodsClass.delete(method, "Students"));
                }
                else if(method.getDbMethodType().toLowerCase().equals("SelectAll".toLowerCase())){
                    System.out.println(DatabaseMethodsClass.selectAll("Students",outputFileName,fieldMap));
                }
            }
            //Replace tableName with the name taken from reflection
            //System.out.println(DatabaseMethodsClass.selectAll("Students",outputFileName,fieldMap));
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

    public List<String> getDatabaseColumns(Class<?> c){

        List<String> fieldsString = new ArrayList<>();
        //Reflection to get Fields
        java.lang.reflect.Field[] declaredFields = c.getDeclaredFields();

        for(java.lang.reflect.Field field : declaredFields){

            Annotation[] annotations = field.getAnnotations();


            for (Annotation annotation : annotations) {
                if(annotation instanceof Field fieldAnnotation){
                    String fieldType = fieldAnnotation.type();
                    try {
                        checkFields(field, fieldType);
                    }
                    catch (IllegalArgumentException ex){
                        ex.printStackTrace();
                    }
                }
            }

            Class<?> type = field.getType();
            String name = field.getName();
            String modifier = Modifier.toString(field.getModifiers());
            fieldsString.add(modifier + " "+type.getSimpleName()+" "+name+ ";\n");
        }

        return fieldsString;
    }

    public List<MethodClass> getMethods(Class<?> c) {
        Method[] methodsArray = c.getDeclaredMethods();
        List<MethodClass> methodClassesList = new ArrayList<>();
        for (Method method : methodsArray) {
            MethodClass methodFound = new MethodClass();
            methodFound.setName(method.getName());
            methodFound.setModifier(mapModifiers(method.getModifiers()));
            methodFound.setReturnType(method.getReturnType().getSimpleName());

            Annotation[] methodAnnotations = method.getDeclaredAnnotations();
            for (Annotation annotation : methodAnnotations) {
                if (annotation instanceof DBMethod) {
                    DBMethod methodAnnotation = (DBMethod) annotation;
                    methodFound.setDbMethodType(methodAnnotation.type());
                    methodFound.setParamName(methodAnnotation.paramName());
                }
            }
            for (Parameter parameter : method.getParameters()){
                methodFound.setParamType(parameter.getType().getSimpleName());
            }
            methodClassesList.add(methodFound);
        }
        return methodClassesList;
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
        System.out.println("Inside setDatabaseStrategy");
        switch(dbType.toLowerCase()){
            case "derby" -> context.setStrategy(new DerbyDatabaseStrategy());
            case "h2" -> context.setStrategy(new H2DatabaseStrategy());
            case "sqlite" -> context.setStrategy(new SQLiteDatabaseStrategy());
            default -> throw new IllegalArgumentException();
        }
    }

    private void checkFields(java.lang.reflect.Field field, String fieldType){
        System.out.println("Inside checkFields");
        List<Class<?>> acceptableFields = databaseContext.mapColumnType(fieldType);
        if(!isFieldTypeOk(field,acceptableFields)){
            throw new IllegalArgumentException("Field type doesn't match the parameter");
        }
    }

    private boolean isFieldTypeOk(java.lang.reflect.Field field, List<Class<?>> acceptableFields){
        return acceptableFields.contains(field.getType());
    }

    private String mapModifiers(int code){
        if(Modifier.isPublic(code)){
            return "public";
        }
        else if(Modifier.isPrivate(code)){
            return "private";
        }
        else {
            return"protected";
        }
    }


}
