// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.storage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.queue.*;




public class MessageWriter {

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
 
           // 3 steps: 
           // (1) Get Uri of blob
           // (2) Create a queue called "queuepicture"
           // (3) Create a queue message and add it to the queue
           
           // Get URI of uploaded blob
           java.net.URI uri = blob.getUri();
           // Create a queue service client
           CloudQueueClient queueClient = account.createCloudQueueClient();
           CloudQueue queue = queueClient.getQueueReference("queuepicture");
           // Create the queue if it doesn't already exist
           queue.createIfNotExists();
           // Create a message to put into queue
           CloudQueueMessage imageUri = new CloudQueueMessage(uri.toString());
           queue.addMessage(imageUri);
        }
        catch (Exception e) {
           System.out.print("Exception encountered: ");
           System.out.println(e.getMessage());
           System.exit(-1);
        }
	}

}
