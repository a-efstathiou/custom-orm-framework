package org.unipi.input;

import org.unipi.annotations.DBMethod;
import org.unipi.annotations.Database;
import org.unipi.annotations.Field;
import org.unipi.annotations.Table;
import org.unipi.annotations.PrimaryKey;

import java.util.List;

@Database(name="UnipiDB",type="H2")
@Table(name="Students")
public class Student {

    @Field(name="name",type="varchar")
    private String name;
    @Field(name="isStudent",type="Boolean")
    private boolean isStudent;
    private String lastName;
    @PrimaryKey
    @Field(name="Id",type="VarChar")
    private String AM;

    @DBMethod(type = "DeleteOne")
    public int deleteStudent(String param){
        return 0;
    }
    @DBMethod(type = "SelectAll")
    public List<String> getAllStudents(){
        return null;
    }

}
