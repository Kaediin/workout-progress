package com.daiken.workoutprogress.models;

import com.daiken.workoutprogress.api.graphql.input.ExternalHealthProviderData;
import com.daiken.workoutprogress.api.graphql.input.ScheduledProgramInput;
import com.daiken.workoutprogress.api.graphql.input.WorkoutInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Workout implements Comparable<Workout> {

    @Id
    private String id;

    private String name;
    private List<MuscleGroup> muscleGroups;

    @DBRef(lazy = true)
    private User user;
    @DBRef(lazy = true)
    private Program program;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Deprecated
    private boolean active;

    private String remark;

    private ExternalHealthProviderData externalHealthProviderData;

    private Long estimatedCaloriesBurned;

    private WorkoutStatus status;

    public Workout(String id, String name, List<MuscleGroup> muscleGroups) {
        this.id = id;
        this.name = name;
        this.muscleGroups = muscleGroups;
    }

    public Workout(WorkoutInput input, User user, boolean active) {
        this.user = user;
        this.active = active;
        update(input);
    }

    public Workout update(WorkoutInput input) {
        this.name = input.name();
        this.muscleGroups = input.muscleGroups();
        this.startDateTime = ZonedDateTime.parse(input.zonedDateTime()).toLocalDateTime();
        this.remark = input.remark();
        return this;
    }

    public Workout updateFromScheduledProgram(ScheduledProgramInput input) {
        if (input.workoutName() != null) {
            this.name = input.workoutName();
        }
        if (input.remark() != null) {
            this.remark = input.remark();
        }
        return this;
    }

    public Workout endWorkout(LocalDateTime localDateTime) {
        this.endDateTime = localDateTime;
        this.active = false;
        this.status = WorkoutStatus.ENDED;
        return this;
    }

    public Workout start(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        this.active = true;
        this.status = WorkoutStatus.STARTED;
        return this;
    }

    @Override
    public int compareTo(@NotNull Workout o) {
        return o.startDateTime.compareTo(startDateTime);
    }
}
