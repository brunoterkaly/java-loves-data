// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaDocumentDB;



import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.SqlQuerySpec;
import com.microsoft.azure.documentdb.SqlParameter;
import com.microsoft.azure.documentdb.SqlParameterCollection;



public class App 
{
    // Replace with your DocumentDB end point and master key.
    private static final String END_POINT = "https://azurecourse.documents.azure.com:443/";
    private static final String MASTER_KEY =
        "deFkkgiY2QniP0S46mpVM9ye9WQOSYT1OkYuFTgZ0YDxjHlNRvyXCLnfQryFe2neslPU7Z5+rOGCOrWW4r+FIA==";
        
    // Define an id for your database and collection
    private static final String DATABASE_ID = "AzureCourse";
    private static final String COLLECTION_ID = "Courses";
    
    private static Gson gson = new Gson();
    
    public static void main( String[] args )  throws DocumentClientException
    {
        // Instantiate a DocumentClient w/ your DocumentDB Endpoint and AuthKey.
        DocumentClient documentClient = new DocumentClient(END_POINT,
                MASTER_KEY, ConnectionPolicy.GetDefault(),
                ConsistencyLevel.Session);
                
        // Instantiate a database object
        Database myDatabase = new Database();
        myDatabase.setId(DATABASE_ID);
        
        // Query for a database with the ID of database = AzureCourse
        List<Database> databases = documentClient.queryDatabases(
            new SqlQuerySpec("SELECT * FROM root r WHERE r.id=@id",
                new SqlParameterCollection(new SqlParameter(
                        "@id", DATABASE_ID))), null).getQueryIterable().toList();
        // If does not exist, then create. Else get a reference to it.
        if(databases.size() == 0)
            myDatabase = documentClient.createDatabase(myDatabase,
                                                        null).getResource();
        else
            myDatabase = databases.get(0);
		
		// COLLECTION CODE
        // Define a new collection using the id above.
        DocumentCollection myCollection = new DocumentCollection();
        myCollection.setId(COLLECTION_ID);
        
        // Configure the new collection performance tier to S1.
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setOfferType("S1");
        
        String databaseLink = myDatabase.getSelfLink();
        SqlQuerySpec sqlQuerySpec =
            new SqlQuerySpec("SELECT * FROM root r WHERE r.id=@id",
                new SqlParameterCollection(new SqlParameter(
                        "@id", COLLECTION_ID)));
                        
        // Query for a collection called Courses
        List<DocumentCollection> collections =
                documentClient.queryCollections(databaseLink,
                    sqlQuerySpec, null).getQueryIterable().toList();
                    
        // If collection not found, create
        if(collections.size() == 0)
            // Create a new collection.
            myCollection = documentClient.createCollection(
                myDatabase.getSelfLink(), myCollection,
                requestOptions).getResource();
        else
            myCollection = collections.get(0);  // if collection exists, get it
            
        // Create an object, serialize it in to JSON, and wrap it in to a
        // document.
        Course[] courses = new Course[] {
            new Course(1, "401", "Intro To Data", new Rating(5, 8)),
            new Course(2, "402", "Intro To Relational Data", new Rating(49, 7)),
            new Course(3, "403", "Intro To NoSQL Data", new Rating(55, 9))
        };
        
      Gson gson = new Gson();
      
      for (Course p : courses)
      {
          String json = gson.toJson(p);
          Document myDocument = new Document(json);
          // Insert document into document collection
          myDocument = documentClient.createDocument(
              myCollection.getSelfLink(),
              myDocument, null, false).getResource();
      }
      
      // Retrieve all inserted documents
      List<Document> documentList = documentClient
              .queryDocuments(myCollection.getSelfLink(),
                      "SELECT * FROM root r ", null)
              .getQueryIterable().toList();
              
      Document myDocument = null;
      // If find at least one, retrieve the first one
      if (documentList.size() > 0)
          myDocument = documentList.get(0);
          
      // Convert to a real object
      Course course = gson.fromJson(myDocument.toString(), Course.class);
      
      // Print out the first name
      System.out.println(course.getCoursetitle());            
            
            
            		
            
            
    }
}
