// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.storage;


import java.io.*;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

//Two Parts: Part 1 - Upload image to blob storage. 
//Put message into queue with url of blob. 
//Part 2 - reads the message from the queue, downloads 
//the blob, and makes a thumbnail out of it. 
//Upload thumbnail to blob storage.


public class BlobManager {
    public static final String storageConnectionString =
                "DefaultEndpointsProtocol=https;"
                + "AccountName=[ the name of your Azure storage account ];"
                + "AccountKey=60v+ezCtCocUY8C4dpYOjsz0sI3CI+rVofVUugPUWKEbTJgJ88lIFurMYytKsMBkW+0kTWQLGq2vqbcT/Gm/PQ==";
	public static void main(String[] args) {
       
        try {
           CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
           CloudBlobClient serviceClient = account.createCloudBlobClient();
           // Container name must be lower case.
           CloudBlobContainer container = serviceClient.getContainerReference("myimages");
           container.createIfNotExists();
  
           // Upload an image file.
           CloudBlockBlob blob = container.getBlockBlobReference("image.jpg");
           File sourceFile = new File("image.jpg");
           blob.upload(new FileInputStream(sourceFile),  sourceFile.length());
           // Download the image file.
           File destinationFile = new File(sourceFile.getParentFile(), "image1Download.jpg");
           blob.downloadToFile(destinationFile.getAbsolutePath());
           // Make the container public
           // Create a permissions object
           BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
           // Include public access in the permissions object
           containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);;
           // Set the permissions on the container
           container.uploadPermissions(containerPermissions);

           // For each item in the container show it's url
           for (ListBlobItem blobItem : container.listBlobs()) {
               // If the item is a blob, not a virtual directory
               if (blobItem instanceof CloudBlockBlob) {
                // Convert blob (if it is CloudBlockBlob)
                CloudBlockBlob retrievedBlob = (CloudBlockBlob) blobItem;
                java.net.URI uri = retrievedBlob.getUri();
                System.out.println(uri.toString());
               }
           }
        }
        catch (Exception e) {
           System.out.print("Exception encountered: ");
           System.out.println(e.getMessage());
           System.exit(-1);
        }
    }


}

