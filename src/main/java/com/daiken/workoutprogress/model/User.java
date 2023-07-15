package com.daiken.workoutprogress.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    public String id;

    @Indexed(unique = true)
    public String fid;

    public User(String fid) {
        this.fid = fid;
    }

    public User() {
    }
}
