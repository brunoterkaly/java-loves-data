// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;


public class DataManager {
    protected static CloudTableClient tableClient;
    protected static CloudTable table;
    protected final static String tableName = "azurecourse";
  public static final String storageConnectionString =
                "DefaultEndpointsProtocol=https;"
                + "AccountName=[ the name of your Azure storage account ];"
                + "AccountKey=60v+ezCtCocUY8C4dpYOjsz0sI3CI+rVofVUugPUWKEbTJgJ88lIFurMYytKsMBkW+0kTWQLGq2vqbcT/Gm/PQ==";

            
    public void InsertData(HttpServletRequest req, HttpServletResponse res) throws      
                                   InvalidKeyException, URISyntaxException {
        // Setup the cloud storage account.
        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
        
        // Create a table service client.
        tableClient = account.createCloudTableClient();
        
        try {
            // Retrieve a reference to a table.
            table = tableClient.getTableReference(tableName);
            
            // Create the table if it doesn't already exist.
            table.createIfNotExists();
            
            String course = req.getParameter("course");
            String notes = req.getParameter("notes");
            
            // Illustrates how to form and execute a single insert operation.
            BasicInsertEntity(course, notes, res);
        }
        catch (Throwable t) {
            Utility.printException(t);
        }
    }

	// Place BasicInsertEntity() here

    public static void BasicInsertEntity(String course, String notes, 
                   HttpServletResponse res) throws StorageException, Throwable {
        // Create a new customer entity.
        
        String courseNumber = new String();
        String courseTitle = new String();
        
        // Grab the course number and title that was submitted by EntryForm.html
        // "course" contains both the courseNumber and courseTitle
        StringTokenizer tokens = new StringTokenizer(course, " ");
        String[] splitted = new String[tokens.countTokens()];
        
        // Separate courseNumber from couseTitle with typical java
        // parsing techniques
        int index = 0;
        while(tokens.hasMoreTokens()){
            splitted[index] = tokens.nextToken();
            if(index == 0)
                courseNumber = splitted[index];
            else if(index > 0)
                courseTitle += splitted[index] + " ";
            ++index;
        }
        
        // Clean up course title. Azure Tables doesn't
        // like these guys.
        courseTitle = courseTitle.replace("\n","");
        courseTitle = courseTitle.replace("\r","");
        courseTitle = courseTitle.replace("\t","");
        courseTitle = courseTitle.replace("-","");
        courseTitle = courseTitle.replace("\\","");
        courseTitle = courseTitle.replace("/","");
        courseTitle = courseTitle.replace("#","");
        courseTitle = courseTitle.replace("?","");
        
        // Prepare our entity that will be inserted
        AzureCourseEntity azureCourse =
                new AzureCourseEntity(courseNumber, courseTitle);
        azureCourse.setNotes(notes);
        
        // Insert new azure course
        TableOperation insertCourse = TableOperation.insert(azureCourse);
        
        // Submit the operation to the table service.
        table.execute(insertCourse);
        
        // Prepare to send results back to the browser (EntryForm.html)
        // PrintWriter is used to send HTML to the browser
        res.setContentType("text/html");
        PrintWriter printWriter  = res.getWriter();
        
        printWriter.println("Course Number: " +
                  azureCourse.getPartitionKey() + "<br>");
        printWriter.println("Course Title: " +
                  azureCourse.getRowKey() + "<br>");
        printWriter.println("Course Notes: " +
                  azureCourse.getNotes() + "<br>");
    }
    
	// Insert ListData() here
	
    public void ListData(HttpServletRequest req, HttpServletResponse res) 
         throws InvalidKeyException,   URISyntaxException {
        // Setup the cloud storage account.
        CloudStorageAccount account =
              CloudStorageAccount.parse(storageConnectionString);
              
        // Create a table service client.
        tableClient = account.createCloudTableClient();
        
        try {
            // Get a reference to the "azurecourse" table
            table = tableClient.getTableReference(tableName);
            
            // Create the table if it doesn't already exist.
            table.createIfNotExists();
            
            // Get the search string that the user typed in
            String search = req.getParameter("search");
            
            // User-defined function - to be defined later
            BasicQuery(search, res);
        }
        catch (Throwable t) {
            Utility.printException(t);
        }
    }
     public static void BasicQuery(String courseNumber, HttpServletResponse res) 
        throws StorageException, Throwable {
        // Retrieve a single entity.
        // Retrieve the entity with partition key
        TableOperation retrieveSearch = TableOperation.retrieve(courseNumber, "",
                                                        AzureCourseEntity.class);
                                                        
        // Submit the operation to the table service and get the specific entity.
        @SuppressWarnings("unused")
        AzureCourseEntity specificEntity =
                             table.execute(retrieveSearch).getResultAsType();
                             
        // Retrieve all entities in a partition.
        // Create a filter condition where the partition key is the
        // course number passed in
        String partitionFilter = TableQuery.generateFilterCondition("PartitionKey",
                                             QueryComparisons.EQUAL, courseNumber);
                                             
        // Specify a partition query, in this case it is the course number
        TableQuery<AzureCourseEntity> partitionQuery =
                   TableQuery.from(AzureCourseEntity.class).where(partitionFilter);
        res.setContentType("text/html");
        
        // PrintWriter is used to send HTML to the browser
        PrintWriter printWriter  = res.getWriter();
        // Loop through the results, displaying information about the entity.
        for (AzureCourseEntity entity : table.execute(partitionQuery)) {
            printWriter.println("Course Number: " + entity.getPartitionKey() + "<br>");
            printWriter.println("Course Title: " + entity.getRowKey() + "<br>");
            printWriter.println("Course Notes: " + entity.getNotes() + "<br>");
        }
        
    }
    
     

                         
                
                
                
               
}





















