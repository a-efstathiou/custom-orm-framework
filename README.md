# Custom ORM Framework

The Custom ORM Framework is a lightweight Object-Relational Mapping (ORM) tool designed to simplify database interactions by automatically generating methods for database operations based on class annotations.

## Features

- **Dynamic Method Generation**: The framework uses reflection to generate methods for database CRUD operations (Read, Delete) based on annotations in class files.
- **Annotation-Based Configuration**: Annotations within class files are used to define database table mappings, making it easy to configure database interactions directly within your model classes.
- **Support for Multiple Databases**: The framework supports connecting to various databases (Derby,SQLite, H2) , allowing users to work with different database systems seamlessly.

## Getting Started

To get started with the Custom ORM Framework, follow these steps:

1. **Download or Clone the Repository**: Clone or download the Custom ORM Framework repository from GitHub.
   
2. **Create an input file**: Create an input file and put it inside the input pckage

3. **Annotate Your Class**: Add annotations to your input class to specify database table mappings and other configurations.

4. **Use Generated Methods**: Once your model classes are annotated, the program will automatically generate methods for CRUD operations (specifically Read and Delete). You can then use these methods to interact with the database.

## Example

Consider the following example of an input class annotated with the Custom ORM Framework annotations for a SQLite database:

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

The generated result can be found in the Output package and it will look like this:

```java
public class Student {

	private String name;
	private boolean isStudent;
	private String lastName;
	private String AM;

	public static void main(String[] args) {
		
	}

	public Student(String name,String AM,boolean isStudent) {
			 this.name = name;
			 this.AM = AM;
			 this.isStudent = isStudent;
	}

	private static void createTable(){
		try {
			Connection connection = connect();
			String createTableSQL = "CREATE TABLE Students"
					+ "(name TEXT ,"
					+ "isStudent BOOLEAN ,"
					+ "Id TEXT  PRIMARY KEY)";
			Statement statement = connection.createStatement();
			statement.executeUpdate(createTableSQL);
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
	public static List<Student> getAllStudents(){
		List<Student> list = new ArrayList<>();

		try {
			Connection connection = connect();
			Statement statement = connection.createStatement();
			String selectSQL = "select * from Students";
			ResultSet resultSet = statement.executeQuery(selectSQL);
			while(resultSet.next()){
				String name = resultSet.getString("name");
				String AM = resultSet.getString("Id");
				boolean isStudent = resultSet.getBoolean("isStudent");
				Student student1 = new Student(name,AM,isStudent);
				list.add(student1);
			}
			statement.close();
			connection.close();
			System.out.println("Done!");
		} catch (SQLException ex) {
				System.err.println(ex.getMessage());
		}
		return list;
	}

	public static int deleteStudent(String Id){
	   int count=0;
	   try {
	       Connection connection = connect();
	       String insertSQL = "DELETE FROM Students WHERE Id = ?";
	       PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
	       preparedStatement.setString(1,Id );
	       count = preparedStatement.executeUpdate();
	       if(count>0){
	           System.out.println(count+" record deleted");
	       }
	       preparedStatement.close();
	       connection.close();
	   } catch (SQLException ex) {
	       System.err.println(ex.getMessage());
	   }
	return count;
	}

    private static Connection connect() {
        String connectionString = "jdbc:sqlite:UnipiDB;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    return connection;
    }

}
```

## License

Feel free to customize the content and structure according to your specific framework and preferences!
