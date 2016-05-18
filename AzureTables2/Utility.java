// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly;
import java.io.PrintWriter;
import java.io.StringWriter;


public final class Utility {

    public static void printException(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        System.out.println(String.format("Got an exception from running samples. Exception details:\n%s\n",
                stringWriter.toString()));
    }
}
