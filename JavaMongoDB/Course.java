// MIT License Copyright (c) 2016 Bruno Terkaly
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaMongoDB;

public class Course {
    public Course(int id, String coursenumber, String coursetitle, Rating rating) {
        super();
        this.id = id;
        this.coursenumber = coursenumber;
        this.coursetitle = coursetitle;
        this.rating = rating;
    }
    private int id;
    private String coursenumber;
    private String coursetitle;
    public int getCourseId() {
        return id;
    }
    private Rating rating;
}