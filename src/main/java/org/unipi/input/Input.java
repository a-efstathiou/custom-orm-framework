package org.unipi.input;

import org.unipi.annotations.DBMethod;
import org.unipi.annotations.Database;
import org.unipi.annotations.Field;

import java.util.List;

@Database(name="UnipiDB",type="Derby")
public class Input {

    @Field(name="something",type="Integer")
    private int something;
    private boolean isOpen;
    private String name;

    @DBMethod(type = "DeleteOne", paramName = "id")
    public int deleteInputs(String id){
        return 0;
    }
    @DBMethod(type = "SelectAll", paramName = "")
    public List<String> getAllStudents(){
        return null;
    }


}
