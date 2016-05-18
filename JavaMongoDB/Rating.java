// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaMongoDB;

public class Rating {
    public Rating(int numberratings, int averagerating) {
        super();
        this.numberratings = numberratings;
        this.averagerating = averagerating;
    }
    private int numberratings;
    private int averagerating;
}
