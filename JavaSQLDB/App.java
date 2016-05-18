// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaSQLDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class App 
{
    // Declare the JDBC objects.
    static private Connection connection = null;
    static private Statement statement = null;
    static private PreparedStatement preparedStatement = null;
    static private ResultSet resultSet = null;
    public static void main(String[] args) {
          String connectionString =
                "jdbc:sqlserver://dbazurecourse.database.windows.net:1433;" +
                  "database=dbazurecourse;" +
                  "user=azureuser@dbazurecourse;" +
                  "password=[ put your password here ]" +
                  "encrypt=true;" +
                  "trustServerCertificate=false;" +
                  "hostNameInCertificate=*.database.windows.net;" +
                  "loginTimeout=30;";


          try {
              // -------------------------------
              // Connect to Azure SQL Database
              connection = DriverManager.getConnection(connectionString);
              DatabaseMetaData dbm = connection.getMetaData();
              // -------------------------------
              // check if "courses" table is there
              ResultSet tables = dbm.getTables(null, null, "courses", null);
              if (tables.next()) {
                // -------------------------------
                // Drop if exists
                statement = connection.createStatement();
                String sql = "drop table courses;";
                statement.executeUpdate(sql);
                System.out.println("Table Dropped.");
              }
              // -------------------------------
              // create courses table
              statement = connection.createStatement();
              String sql = "create table courses(id int, " +
                  "coursenumber varchar(128), coursetitle varchar(512)); ";
              statement.executeUpdate(sql);
          
              // -------------------------------
              // insert data
              String query = "insert into courses(id, coursenumber, coursetitle) " +
                  "values(?, ?, ?)";
              preparedStatement = connection.prepareStatement(query);
              preparedStatement.setInt(1, 5);
              preparedStatement.setString(2, "0401");
              preparedStatement.setString(3, "Intro to Data");
              preparedStatement.executeUpdate();
          
              // -------------------------------
              // do select
              PerformSelectOfData();          
          
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              // Close the connections after the data has been handled.
              if (preparedStatement != null)
                try { preparedStatement.close(); } catch(Exception e) {}
              if (resultSet != null)
                try { resultSet.close(); } catch(Exception e) {}
              if (statement != null)
                try { statement.close(); } catch(Exception e) {}
              if (connection != null)
                try { connection.close(); } catch(Exception e) {}

          }
    }
    // Add PerformSelectOfData(), WriteResults()
    public static void PerformSelectOfData() throws SQLException {
          // Statements allow to issue SQL queries to the database
          Statement statement;
          try {
              statement = connection.createStatement();
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