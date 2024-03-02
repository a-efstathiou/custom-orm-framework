package org.unipi.input;

import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.annotations.Table;

@Database(name="UnipiDB",type="Derby")
@Table(name="Student")
public class Input {

    @Field(name="somethingName",type="integer")
    private int something;
    @Field(name="availability",type="Boolean")
    private boolean isOpen;
    private String name;

    public void random(){
        System.out.println("inside random");
    }

}
