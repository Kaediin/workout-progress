package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ProgramWorkout;
import com.daiken.workoutprogress.models.ProgramWorkoutGroup;
import com.daiken.workoutprogress.repositories.ProgramWorkoutGroupRepository;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@Component
public class ProgramWorkoutResolver implements GraphQLResolver<ProgramWorkout> {

    private final ProgramWorkoutGroupRepository programWorkoutGroupRepository;

    @Autowired
    public ProgramWorkoutResolver(ProgramWorkoutGroupRepository programWorkoutGroupRepository) {
        this.programWorkoutGroupRepository = programWorkoutGroupRepository;
    }

    public List<ProgramWorkoutGroup> groups(ProgramWorkout programWorkout) {
        return programWorkoutGroupRepository.findAllByProgramWorkout(programWorkout);
    }
}
