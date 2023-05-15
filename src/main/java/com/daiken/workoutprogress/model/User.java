package com.daiken.workoutprogress.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class User {

    @Id
    public String id;
    public String firstName;
    public String middleName;
    public String lastName;

    public String fid;

    @Indexed(unique = true)
    public String email;

    public String password;

    public Date createdDate;
    public Date updatedDate;

    public User() {
    }

    public User(UserInput userInput) {
        this.firstName = userInput.firstName;
        this.lastName = userInput.lastName;
        this.middleName = userInput.middleName;
        this.email = userInput.email;
        this.password = userInput.password;
    }

    public User(String fid) {
        this.createdDate = new Date();
        this.updatedDate = this.createdDate;

        this.fid = fid;
    }
}
