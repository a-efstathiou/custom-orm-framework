package org.unipi.input;

import org.unipi.annotations.Database;

@Database(name="UnipiDB",type="Derby")
public class Input {

    public void random(){
        System.out.println("inside random");
    }

}
