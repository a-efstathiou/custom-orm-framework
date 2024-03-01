package org.unipi.input;

import org.unipi.annotations.Database;
import org.unipi.annotations.Field;

@Database(name="UnipiDB",type="Derby")
public class Input {

    @Field(name="something",type="Integer")
    private int something;
    private boolean isOpen;
    private String name;

    public void random(){
        System.out.println("inside random");
    }

}
