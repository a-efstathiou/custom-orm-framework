# custom-orm-framework

## Features

- **Dynamic Method Generation**: The framework uses reflection to generate methods for database CRUD operations (Create, Read, Update, Delete) based on annotations in class files.
- **Annotation-Based Configuration**: Annotations within class files are used to define database table mappings, making it easy to configure database interactions directly within your model classes.
- **Support for Multiple Databases**: The framework supports connecting to various databases (Derby,SQLite, H2) , allowing users to work with different database systems seamlessly.

## Getting Started

To get started with the Custom ORM Framework, follow these steps:

1. **Download or Clone the Repository**: Clone or download the Custom ORM Framework repository from GitHub.
   
2. **Create an input file**: Create an input file and put it inside the input pckage

3. **Annotate Your Class**: Add annotations to your input class to specify database table mappings and other configurations.

4. **Use Generated Methods**: Once your model classes are annotated, the program will automatically generate methods for CRUD operations (specifically Read and Delete). You can then use these methods to interact with the database.

## Example

Consider the following example of an input class annotated with the Custom ORM Framework annotations:

```java
@Database(name="DB",type="SQLite")
@Table(name="Students")
public class Student {

    @Field(name="name",type="TEXT")
    private String name;
    @Field(name="isStudent",type="Boolean")
    private boolean isStudent;
    private String lastName;
    @PrimaryKey
    @Field(name="Id",type="text")
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
```

Feel free to customize the content and structure according to your specific framework and preferences!
