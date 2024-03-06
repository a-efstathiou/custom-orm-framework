package org.unipi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//It is applied on fields and indicates if a field will be a column on the DB.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    String name(); //name of the field.
    String type(); //Type of the DB column (Varchar(20), Text, etc).
}