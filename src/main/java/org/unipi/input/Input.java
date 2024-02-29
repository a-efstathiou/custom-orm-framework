package org.unipi.input;

import org.unipi.annotations.Database;

@Database(name="UnipiDB",type="Derby")
public class Input {

    private int something;
    private boolean isOpen;
    private String name;

    public void random(){
        System.out.println("inside random");
    }

}
