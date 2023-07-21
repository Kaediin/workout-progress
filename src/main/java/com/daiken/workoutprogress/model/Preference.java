package com.daiken.workoutprogress.model;


import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Preference {

    @Id
    public String id;

    @Indexed(unique = true)
    @DBRef(lazy = true)
    public User user;

    public WeightUnit unit;
    public int defaultRepetitions;

    public Preference() {
    }

    public Preference(User user) {
        this.user = user;
        this.unit = WeightUnit.KG;
        this.defaultRepetitions = 10;
    }

    public Preference(User user, PreferenceInput input) {
        this.user = user;
        this.update(input);
    }

    public void update(PreferenceInput input) {
        this.unit = input.unit;
        this.defaultRepetitions = input.defaultRepetitions;
    }
}
