package com.daiken.workoutprogress.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
public class ProgramWorkoutGroup {

    @Id
    private String id;

    @DBRef(lazy = true)
    private ProgramWorkout programWorkout;

    private ProgramLogGroupType type;

    public ProgramWorkoutGroup(ProgramWorkout programWorkout, ProgramLogGroupType type) {
        this.programWorkout = programWorkout;
        this.type = type;
    }
}
