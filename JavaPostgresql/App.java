// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaPostgresql;


import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
    
    public static void main( String[] args )
            throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        try {
            Class.forName("org.postgresql.Driver").newInstance(); 
            // Setup the connection with the DB
            connect = DriverManager.getConnection(
                "jdbc:postgresql://[ put your ip address here ]:5432/azurecourse?" +
                "user=postgres&password=[ put your password here ]&useUnicode=true&characterEncoding=UTF-8");
                
            DatabaseMetaData dbm = connect.getMetaData();
            
            // -------------------------------
            // check if "courses" table is there
            ResultSet tables = dbm.getTables(null, null, "courses", null);
            if (tables.next()) {
              // -------------------------------
              // Drop if exists
              statement = connect.createStatement();
              String sql = "drop table courses;";
              statement.executeUpdate(sql);
              System.out.println("Table Dropped.");
            }
            // -------------------------------
            // create courses table
            statement = connect.createStatement();
            String sql = "create table courses(id int, " +
                "coursenumber varchar(128), coursetitle varchar(512)); ";
            statement.executeUpdate(sql);
            
            
            // -------------------------------
            // Perform the insert
            // the postgresql insert statement
            String query = "insert into courses(id, coursenumber, coursetitle) " +
                "values(?, ?, ?)";
            // create the postgresql insert preparedstatement
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
    public static void PerformSelectOfData() throws SQLException {
        // Statements allow to issue SQL queries to the database
        Statement statement;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from courses");
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









