package org.unipi.reflection;

import org.unipi.annotations.*;
import org.unipi.annotations.Field;
import org.unipi.database.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionHandler {

    FileHandler fh = FileHandler.getInstance();
    DatabaseContext databaseContext = DatabaseContext.getInstance();

    private List<FieldClass> fieldClassList;

    private ReflectionHandler(){
        fieldClassList = new ArrayList<>();
    }
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
            checkValidNumberOfPK(c.getDeclaredFields()); //check if the number of PK is less than 2.
            getDatabaseColumns(c);

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

    //gets all the methods of the Input file using Reflection.
    public List<MethodClass> getMethods(Class<?> c, java.lang.reflect.Field[] allFields) {

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
                    //Only one or zero parameters are allowed in DbMethods
                    if(method.getParameters().length>1){
                        throw new IllegalArgumentException("2 or more parameters are not allowed in DBMethods!");
                    }
                    else if(method.getParameters().length == 1){
                        String primaryKey = findPrimaryKey(allFields);
                        if(primaryKey==null){
                            throw new IllegalArgumentException("You have not assigned a Primary key!");
                        }
                        else {
                            //gets the param
                            methodFound.setParamName(primaryKey);
                            methodFound.setParamType(method.getParameters()[0].getType().getSimpleName());
                        }
                    }
                }
            }
            methodClassesList.add(methodFound);
        }
        return methodClassesList;
    }

    //Get th


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

/*    public List<FieldClass> getFieldClassList() {
        return fieldClassList;
    }*/

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

    //get all fieldClass objects
    public List<FieldClass> getAllFieldClass(java.lang.reflect.Field[] fields){
        for(java.lang.reflect.Field field : fields){
            FieldClass fieldClass = new FieldClass();
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if(annotation instanceof PrimaryKey){
                    fieldClass.setPrimaryKey(true);
                }
                if(annotation instanceof Unique){
                    fieldClass.setUnique(true);
                }
                if(annotation instanceof NotNull){
                    fieldClass.setNotNull(true);
                }
            }
            fieldClass.setName(field.getName());
            fieldClass.setColumnType(databaseContext.getColumnType(field.getType().getSimpleName()));
            fieldClassList.add(fieldClass);
        }
        return fieldClassList;
    }

    //finds the PK between the fields
    private String findPrimaryKey(java.lang.reflect.Field[] fields){
        String primaryKey = null;
        for(java.lang.reflect.Field field : fields){
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if(annotation instanceof PrimaryKey){
                    primaryKey = field.getName();
                }
            }
        }
        return primaryKey;
    }

    //check if the file has more than one Primary keys
    private void checkValidNumberOfPK(java.lang.reflect.Field[] fields){
        int counter = 0;
        for(java.lang.reflect.Field field : fields){
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if(annotation instanceof PrimaryKey){
                    counter+=1;
                    if(counter>1){
                        throw new IllegalArgumentException("Only One Primary key is allowed!");
                    }
                }
            }
        }
    }

}
