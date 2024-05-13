package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ProgramLogInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
@NoArgsConstructor
public class ProgramLog {

    @Id
    private String id;

    @DBRef(lazy = true)
    private ProgramLogGroup programLogGroup;

    @DBRef(lazy = true)
    private Program program;

    private int repetitions;
    private int intervalSeconds;
    private int cooldownSeconds;
    private int effort;

    private LogValue logValue;

    @DBRef(lazy = true)
    private Exercise exercise;

    @DBRef(lazy = true)
    private ExerciseLog exerciseLog;

    private List<ProgramLog> subdivisions;

    public ProgramLog(ProgramLogInput programLogInput, ProgramLogGroup programLogGroup, Program program, Exercise exercise) {
        this.programLogGroup = programLogGroup;
        this.program = program;
        this.repetitions = programLogInput.repetitions();
        this.intervalSeconds = programLogInput.intervalSeconds();
        this.cooldownSeconds = programLogInput.cooldownSeconds();
        this.effort = programLogInput.effort();
        this.logValue = new LogValue(programLogInput.logValue());
        this.exercise = exercise;
    }

    public void addSubdivision(ProgramLog programLog) {
        if (subdivisions == null) {
            subdivisions = new ArrayList<>();
        }
        subdivisions.add(programLog);
    }

    public void removeSubdivision(ProgramLog programLog) {
        subdivisions.remove(programLog);
        if (subdivisions.isEmpty()) {
            subdivisions = null;
        }
    }

    public List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        if (subdivisions != null) {
            for (ProgramLog programLog : subdivisions) {
                exercises.addAll(programLog.getExercises());
            }
        }
        return exercises;
    }

}
