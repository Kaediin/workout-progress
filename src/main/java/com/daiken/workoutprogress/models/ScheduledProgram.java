package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ScheduledProgramInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Document
@NoArgsConstructor
public class ScheduledProgram {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Program program;

    @DBRef(lazy = true)
    private User user;

    private LocalDateTime createdDateTime;
    private LocalDateTime scheduledDateTime;
    private LocalDateTime startDateTime;
    private LocalDateTime endedDateTime;

    private String remark;

    @DBRef(lazy = true)
    private Workout workout;

    public ScheduledProgram(ScheduledProgramInput input, Program program, User user) {
        this.program = program;
        this.user = user;
        this.scheduledDateTime = ZonedDateTime.parse(input.scheduleZonedDateTime()).toLocalDateTime();
        this.createdDateTime = ZonedDateTime.parse(input.zonedDateTime()).toLocalDateTime();
        this.remark = input.remark();
    }
}
