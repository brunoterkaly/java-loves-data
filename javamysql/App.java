// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaMySQL;

// import statements here
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App 
{
    static private Connection connect = null;
    static private Statement statement = null;
    static private PreparedStatement preparedStatement = null;
    static private ResultSet resultSet = null;
    // add code here
    public static void main( String[] args )
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
        
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Setup the connection with the DB
	        connect = DriverManager.getConnection(
	            "jdbc:mysql://[your ip address of your VM:3306/azurecourse?" +
	            "user=mysqluser&password=[your password]&useUnicode=true&characterEncoding=UTF-8");

  
            // the mysql insert statement
            String query = "insert into courses(id, coursenumber, coursetitle) " +
                "values(?, ?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = connect.prepareStatement(query);
            preparedStmt.setInt(1, 5);
            preparedStmt.setString(2, "0401");
            preparedStmt.setString(3, "Intro to Data");
            // execute the preparedstatement
            preparedStmt.executeUpdate();
             
             // Display the data
             PerformSelectOfData();
             connect.close();           
                
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }     
    public static void PerformSelectOfData() throws SQLException {
        // Statements allow to issue SQL queries to the database
        Statement statement;
        try {
            statement = connect.createStatement();
            resultSet = 
                statement.executeQuery("select * from azurecourse.courses");
                WriteResults(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Result set get the result of the SQL query
    }  
    public static void WriteResults(ResultSet resultSet) throws SQLException {
        System.out.println("The columns in the table are: ");
        try {
            System.out.println("Table: " +
                               resultSet.getMetaData().getTableName(1));
            // Loop through columns
            for (int i = 1;
                 i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.println("Column " + i + " " +
                   resultSet.getMetaData().getColumnName(i));
            }
            // Loop through rows
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String coursenumber = resultSet.getString("coursenumber");
                String coursetitle = resultSet.getString("coursetitle");
                System.out.println("ID: " + String.valueOf(id));
                System.out.println("Course Number: " + coursenumber);
                System.out.println("Course Title: " + coursetitle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}



















