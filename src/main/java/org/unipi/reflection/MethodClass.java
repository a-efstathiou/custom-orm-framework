package org.unipi.reflection;

public class MethodClass {
    private String modifier;
    private String dbMethodType;
    private String returnType;
    private String name;
    private String paramType;
    private String paramName;

    public MethodClass(String modifier, String dbMethodType, String returnType, String name, String paramType, String paramName) {
        this.modifier = modifier;
        this.dbMethodType = dbMethodType;
        this.returnType = returnType;
        this.name = name;
        this.paramType = paramType;
        this.paramName = paramName;
    }

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
