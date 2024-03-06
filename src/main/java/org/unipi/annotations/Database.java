package org.unipi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//Can only be applied on classes and indicates the name of the Database
// and its type (Derby, Sqlite, H2).
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {
    String type(); //(Derby, Sqlite, H2)
    String name();
}