package com.daiken.workoutprogress.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String fid;

    public User(String fid) {
        this.fid = fid;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        if (((User) obj).getId() == null && getId() == null && ((User) obj).getFid() == null && getFid() == null) {
            return true;
        }

        if (((User) obj).getFid() == null && getFid() == null && ((User) obj).getId().equals(getId())) {
            return true;
        }

        if (((User) obj).getId() == null && getId() == null && ((User) obj).getFid().equals(getFid())) {
            return true;
        }

        return ((User) obj).getFid().equals(getFid()) &&
                ((User) obj).getId().equals(getId());
    }
}
