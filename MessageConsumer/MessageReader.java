// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.storage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.queue.*;
public class MessageReader {
    public static final String storageConnectionString =
                "DefaultEndpointsProtocol=https;"
                + "AccountName=[ the name of your Azure storage account ];"
                + "AccountKey=60v+ezCtCocUY8C4dpYOjsz0sI3CI+rVofVUugPUWKEbTJgJ88lIFurMYytKsMBkW+0kTWQLGq2vqbcT/Gm/PQ==";

	public static void main(String[] args) {
		
		try{
	        CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
	        CloudBlobClient serviceClient = account.createCloudBlobClient();
	        // Container name must be lower case.
	        CloudBlobContainer container = serviceClient.getContainerReference("myimages");
	        container.createIfNotExists();
	        
	        CloudQueueClient queueClient = account.createCloudQueueClient();
	        CloudQueue queue = queueClient.getQueueReference("queuepicture");
	        // Create the queue if it doesn't already exist
	        queue.createIfNotExists();

            // Loop through queue. Retrieve messages.
            // For each message, get URL out of message.
            // Download image using URL. Downloaded image renamed with "BlackWhite."
            // Convert local image to black and white.
            
            CloudQueueMessage retrievedMessage;
            while((retrievedMessage = queue.retrieveMessage(30, null /*options*/, null /*opContext*/)) != null) {
                // Extract url from message
                String imageUrl = retrievedMessage.getMessageContentAsString();
                // Extract filename from url 
                String imageName = getImageNameFromUrl(imageUrl);
                // Add "BlackWhite" to filename
                String blackWhiteFile = addBlackWhiteToName(imageName);
                // Save image to local file systemd
                saveImage(imageUrl, blackWhiteFile);
                File input = new File(blackWhiteFile);
                BufferedImage image = ImageIO.read(input);
                toGray(image);
                String grayFile = "tempBlackWhite.jpg";
                File output = new File(grayFile);
                ImageIO.write(image, "jpg", output);
                
                Path source = Paths.get(grayFile);
                Path target = Paths.get(blackWhiteFile);
                //overwrite existing file, if exists
                CopyOption[] options = new CopyOption[]{
                  StandardCopyOption.REPLACE_EXISTING,
                  StandardCopyOption.COPY_ATTRIBUTES};
                  Files.copy(source, target, options);
                  Files.delete(source);;
                  
                  queue.deleteMessage(retrievedMessage);;
                  
                            
            }
		
		}
        catch (Exception e) {
           System.out.print("Exception encountered: ");
           System.out.println(e.getMessage());
           System.exit(-1);
        }
	 }
    // Extract image name from URL
    public static String getImageNameFromUrl(String imageUrl) throws IOException {
      int lastSlashPos = imageUrl.lastIndexOf('/');
      if (lastSlashPos >= 0)
         return imageUrl.substring(lastSlashPos+1);
      else
          return "";
    }
    // Return string with filename modification
    public static String addBlackWhiteToName(String fileName) throws IOException {
       // image.jpg => becomes => imageBlackWhite.jpg
       int lastDotPos = fileName.lastIndexOf('.');
       if (lastDotPos >= 0)
          return fileName.substring(0, lastDotPos) + "BlackWhite" + ".jpg";
       else
          return "";
    }
     // Download image through URL
     public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);
        
        byte[] b = new byte[2048];
        int length;
        
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);;
        }
        is.close();
        os.close();
     }

     // Convert all the in memory pixels to gray
     public static void toGray(BufferedImage image) {
          int width = image.getWidth();
          int height = image.getHeight();
          for(int i=0; i < height; i++) {
            for(int j=0; j < width; j++) {
                  Color c = new Color(image.getRGB(j, i));
                  int red = (int)(c.getRed() * 0.299);
                  int green = (int)(c.getGreen() * 0.587);
                  int blue = (int)(c.getBlue() * 0.114);
                  int sum = red + green + blue;
                  Color newColor = new Color(sum, sum, sum);
                  image.setRGB(j, i, newColor.getRGB());
            }
          }
     }
     
















   
    

    	
}