package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ProgramWorkoutGroup;
import com.daiken.workoutprogress.models.ProgramWorkoutLog;
import com.daiken.workoutprogress.repositories.ProgramWorkoutLogRepository;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@Component
public class ProgramWorkoutGroupResolver implements GraphQLResolver<ProgramWorkoutGroup> {
    private final ProgramWorkoutLogRepository programWorkoutLogRepository;

    @Autowired
    public ProgramWorkoutGroupResolver(ProgramWorkoutLogRepository programWorkoutLogRepository) {
        this.programWorkoutLogRepository = programWorkoutLogRepository;
    }

    public List<ProgramWorkoutLog> programWorkoutLogs(ProgramWorkoutGroup programWorkoutGroup) {
        return programWorkoutLogRepository.findAllByProgramWorkoutGroup(programWorkoutGroup);
    }
}
