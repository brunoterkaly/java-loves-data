// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaSQL;

// import statements here
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
    // Some JDBC objects
    static private Connection connect = null;
    static private Statement statement = null;
    static private PreparedStatement preparedStatement = null;
    static private ResultSet resultSet = null;
    public static void main( String[] args )
    
		// There are a bunch of things to check for to make sure the connection works
		// You need to enable a standard login for SQL Server
		// You need to open up ports 1433 and 1434 on the firewall of the VM
		// You need to allow SQL server browser to run
		// You need to set up a network security group rule to allow connection to port 1433 and 1434
    
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            // sa is not an ideal identity. You are better off creating a new user in SQL Server
            // and assigning minimal permissions to that user
            String userName= "sa";
            String password="[put your password here ]";
            String url="jdbc:sqlserver://db-azurecourse.westus.cloudapp.azure.com\\MSSQLSERVER;databaseName=azurecourse";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect=DriverManager.getConnection(url, userName, password);
            
            // SQL insert statement
            String query = "insert into courses(id, coursenumber, coursetitle) " +
                "values(?, ?, ?)";
            // create the sql insert preparedstatement
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
            e.printStackTrace();
        }
    }
	// Add PerformSelectOfData() + WriteResults()
    public static void PerformSelectOfData() throws SQLException {
        // Statements allow to issue SQL queries to the database
        Statement statement;
        try {
            statement = connect.createStatement();
            resultSet =
                statement.executeQuery("select * from dbo.courses");
                WriteResults(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
