package com.daiken.workoutprogress.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@NoArgsConstructor
public class ProgramLogGroup {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Program program;

    private ProgramLogGroupType type;

    public ProgramLogGroup(Program program, ProgramLogGroupType type) {
        this.program = program;
        this.type = type;
    }

}
