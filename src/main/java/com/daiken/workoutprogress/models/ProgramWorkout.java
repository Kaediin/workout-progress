package com.daiken.workoutprogress.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
public class ProgramWorkout {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Program program;

    @DBRef(lazy = true)
    private Workout workout;

    public ProgramWorkout(Program program, Workout workout) {
        this.program = program;
        this.workout = workout;
    }
}
