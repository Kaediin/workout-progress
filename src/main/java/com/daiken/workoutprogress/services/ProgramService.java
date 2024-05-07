package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.api.graphql.WorkoutMutationResolver;
import com.daiken.workoutprogress.repositories.ScheduledProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Program operations.
 */
@Service
public class ProgramService {

    private final ScheduledProgramRepository scheduledProgramRepository;
    private final WorkoutMutationResolver workoutMutationResolver;

    @Autowired
    public ProgramService(ScheduledProgramRepository scheduledProgramRepository, WorkoutMutationResolver workoutMutationResolver) {
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.workoutMutationResolver = workoutMutationResolver;
    }

    public Boolean deleteScheduledProgram(String scheduledProgramId, String workoutId) {
        scheduledProgramRepository.deleteById(scheduledProgramId);
        workoutMutationResolver.deleteWorkout(workoutId);
        return true;
    }
}
