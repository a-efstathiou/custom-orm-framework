package org.unipi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//It can only be applied to fields and
// indicate if the field will be set as Primary key on the DB (with SQl).
// We set in other classes that the Input class must have only one Primary key annotation.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
}