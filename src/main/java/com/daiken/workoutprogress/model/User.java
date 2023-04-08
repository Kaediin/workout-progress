package com.daiken.workoutprogress.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    public String id;
    public String firstName;
    public String lastName;
    public String infix;

    @Indexed(unique = true)
    public String email;

    public String password;

    public User() {
    }

    public User(UserInput userInput) {
        this.firstName = userInput.firstName;
        this.lastName = userInput.lastName;
        this.infix = userInput.infix;
        this.email = userInput.email;
        this.password = userInput.password;
    }
}
