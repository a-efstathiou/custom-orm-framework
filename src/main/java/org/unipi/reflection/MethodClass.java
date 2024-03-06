package org.unipi.reflection;

//It is a class that represents a DB Method. It contains all its characteristics.
public class MethodClass {
    private String modifier; //Modifier (public, private, protected)
    private String dbMethodType; //DB Method type (SelectAll or DeleteOne)
    private String returnType; //the return type of the method
    private String name; //the given name of the method e.g. getAllStudents
    private String paramType; //The type of its parameter
    private String paramName; //The name of its parameter.

    public MethodClass(){}
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDbMethodType() {
        return dbMethodType;
    }

    public void setDbMethodType(String dbMethodType) {
        this.dbMethodType = dbMethodType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}