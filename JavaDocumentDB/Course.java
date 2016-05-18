// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java
package com.terkaly.JavaDocumentDB;


public class Course {
    public Course(int courseid, String coursenumber, String coursetitle, Rating rating) {
        super();
        this.courseid = courseid;
        this.coursenumber = coursenumber;
        this.coursetitle = coursetitle;
        this.rating = rating;
    }
    public String getCoursetitle() {
        return coursetitle;
    }
    public void setCoursetitle(String coursetitle) {
        this.coursetitle = coursetitle;
    }
    private int courseid;
    private String coursenumber;
    private String coursetitle;
    public int getCourseId() {
        return courseid;
    }
    private Rating rating;
}