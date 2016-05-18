// Copyright 2016, Bruno Terkaly, All Rights Reserved
// Open sourcing my code for my O'Reilly course.
// Cloud-Based Provisioning, Storage, and Data Retrieval with Java and Linux
// Learn more here http://bit.ly/bruno-does-linux-data-java

package com.terkaly;

import com.microsoft.azure.storage.table.*;

public class AzureCourseEntity extends TableServiceEntity {

    public AzureCourseEntity(String courseNumber, String courseTitle) {
        this.partitionKey = courseNumber;
        this.rowKey = courseTitle;
    }
    
    public AzureCourseEntity() { }
    
    public String Notes;
    
    public String getNotes() {
        return this.Notes;
    }
    
    public void setNotes(String notes) {
        this.Notes = notes;
    }
}
