package org.unipi.output;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    private static Connection connect() {
        String connectionString = "jdbc:sqlite:DB;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    return connection;
    }

}