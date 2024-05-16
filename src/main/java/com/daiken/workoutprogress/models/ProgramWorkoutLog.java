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
public class ProgramWorkoutLog {

    @Id
    private String id;

    @DBRef(lazy = true)
    private ProgramLog programLog;

    @DBRef(lazy = true)
    private ProgramLog originalProgramLog;

    @DBRef(lazy = true)
    private ProgramWorkout programWorkout;

    @DBRef(lazy = true)
    private ProgramWorkoutGroup programWorkoutGroup;

    public ProgramWorkoutLog(
            ProgramLog programLog,
            ProgramLog originalProgramLog,
            ProgramWorkout programWorkout,
            ProgramWorkoutGroup programWorkoutGroup
    ) {
        this.programLog = programLog;
        this.originalProgramLog = originalProgramLog;
        this.programWorkout = programWorkout;
        this.programWorkoutGroup = programWorkoutGroup;
    }
}
