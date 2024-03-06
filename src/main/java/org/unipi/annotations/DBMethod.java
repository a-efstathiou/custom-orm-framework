package org.unipi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//It is only applied on methods and indicates if a method is DB Method .
// A Db method is a method that executes some queries on a Database.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DBMethod {
    String type();
}
