package org.unipi.output;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Input {

	private int something;
	private boolean isOpen;
	private String name;
	private String AM;

	public static void main(String[] args) {
		
	}

	public Input(int something,boolean isOpen,String AM) {
			 this.something = something;
			 this.isOpen = isOpen;
			 this.AM = AM;
	}

	private static void createTableAndData(){
		try {
			Connection connection = connect();
			String createTableSQL = "CREATE TABLE Student"
					+ "(somethingName INT ,"
					+ "availability BOOLEAN ,"
					+ "Id VARCHAR(20)  PRIMARY KEY)";
			Statement statement = connection.createStatement();
			statement.executeUpdate(createTableSQL);
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
	public static int deleteInputs(String Id){
	   int count=0;
	   try {
	       Connection connection = connect();
	       String insertSQL = "DELETE FROM Student WHERE Id = ?";
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

	public static List<Input> getAllStudents(){
		List<Input> list = new ArrayList<>();

		try {
			Connection connection = connect();
			Statement statement = connection.createStatement();
			String selectSQL = "select * from Student";
			ResultSet resultSet = statement.executeQuery(selectSQL);
			while(resultSet.next()){
				int something = resultSet.getInt("somethingName");
				boolean isOpen = resultSet.getBoolean("availability");
				String AM = resultSet.getString("Id");
				Input input1 = new Input(something,isOpen,AM);
				list.add(input1);
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
        String connectionString = "jdbc:derby:UnipiDB;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    return connection;
    }

}