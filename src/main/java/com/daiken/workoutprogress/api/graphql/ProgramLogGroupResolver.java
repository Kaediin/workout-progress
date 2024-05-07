package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ProgramLog;
import com.daiken.workoutprogress.models.ProgramLogGroup;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.ProgramLogRepository;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Resolver for ProgramLogGroup
 */
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramLogGroupResolver implements GraphQLResolver<ProgramLogGroup> {

    private final ExerciseRepository exerciseRepository;
    private final ProgramLogRepository programLogRepository;

    @Autowired
    public ProgramLogGroupResolver(
            ExerciseRepository exerciseRepository,
            ProgramLogRepository programLogRepository
    ) {
        this.exerciseRepository = exerciseRepository;
        this.programLogRepository = programLogRepository;
    }

    public List<ProgramLog> logs(ProgramLogGroup programLogGroup) {
        // Fetch all logs for the given program log group
        List<ProgramLog> logs = programLogRepository.findAllByProgramLogGroupId(programLogGroup.getId());

        // Resolve all exercises for the logs
        for (ProgramLog log : logs) {

            // Base case
            if (log.getSubdivisions() == null || log.getSubdivisions().isEmpty()) {
                continue;
            }

            for (ProgramLog subdivision : log.getSubdivisions()) {
                subdivision.setExercise(exerciseRepository.findById(subdivision.getExercise().getId()).orElse(null));
            }
        }

        return logs;
    }
}
