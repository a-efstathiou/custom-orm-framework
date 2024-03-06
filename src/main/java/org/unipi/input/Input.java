package org.unipi.input;

import org.unipi.annotations.DBMethod;
import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.annotations.Table;
import org.unipi.annotations.PrimaryKey;

import java.util.List;

@Database(name="UnipiDB",type="Derby")
@Table(name="Student")
public class Input {

    @Field(name="somethingName",type="integer")
    private int something;
    @Field(name="availability",type="Boolean")
    private boolean isOpen;
    private String name;
    @PrimaryKey
    private String AM;

    @DBMethod(type = "DeleteOne")
    public int deleteInputs(String param){
        return 0;
    }
    @DBMethod(type = "SelectAll")
    public List<String> getAllStudents(){
        return null;
    }

}
