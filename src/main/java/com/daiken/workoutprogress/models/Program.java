package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExternalHealthProviderData;
import com.daiken.workoutprogress.api.graphql.input.ProgramInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Document
@NoArgsConstructor
public class Program {

    @Id
    private String id;
    private String name;
    private String remark;

    @Indexed(unique = true)
    @DBRef(lazy = true)
    private User user;

    private LocalDateTime createdDateTime;

    private boolean active;

    private ExternalHealthProviderData externalHealthProviderData;

    private int estimatedCaloriesBurned;

    public Program(ProgramInput input, User user) {
        this.name = input.name();
        this.remark = input.remark();
        this.createdDateTime = ZonedDateTime.parse(input.zonedDateTime()).toLocalDateTime();
        this.active = false;
        this.user = user;
    }

    public void update(ProgramInput input) {
        this.name = input.name() != null && !input.name().isEmpty() ? input.name() : this.name;
        this.remark = input.remark() != null && !input.remark().isEmpty() ? input.remark() : this.remark;
    }
}
