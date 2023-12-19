package com.daiken.workoutprogress.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document
public class MigrationRecord {

    @Id
    public String id;

    @Indexed
    public String key;

    public Integer order;

    @Indexed
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public Date createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public Date updatedDate;

    public MigrationRecord(String key, Integer order) {
        this.createdDate = new Date();
        this.updatedDate = this.createdDate;

        this.key = key;
        this.order = order;
    }

}
