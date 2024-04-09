package com.daiken.workoutprogress.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Document
@NoArgsConstructor
public class User {

    @Id
    public String id;

    @Indexed(unique = true)
    private String fid;

    private Boolean onboardingCompleted;

    public User(String fid) {
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

        if (((User) obj).getFid() == null && getFid() == null && Objects.equals(((User) obj).getId(), getId())) {
            return true;
        }

        if (((User) obj).getId() == null && getId() == null && Objects.equals(((User) obj).getFid(), getFid())) {
            return true;
        }

        return Objects.equals(((User) obj).getFid(), getFid()) &&
                Objects.equals(((User) obj).getId(), getId());
    }
}
