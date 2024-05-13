package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.api.graphql.WorkoutMutationResolver;
import com.daiken.workoutprogress.models.ScheduledProgram;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ScheduledProgramRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service for Program operations.
 */
@Service
public class ProgramService {

    private final ScheduledProgramRepository scheduledProgramRepository;
    private final WorkoutMutationResolver workoutMutationResolver;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ProgramService(ScheduledProgramRepository scheduledProgramRepository, WorkoutMutationResolver workoutMutationResolver, WorkoutRepository workoutRepository) {
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.workoutMutationResolver = workoutMutationResolver;
        this.workoutRepository = workoutRepository;
    }

    public Boolean deleteScheduledProgram(String scheduledProgramId, String workoutId) {
        scheduledProgramRepository.deleteById(scheduledProgramId);
        workoutMutationResolver.deleteWorkout(workoutId);
        return true;
    }

    public Boolean startScheduledProgram(ScheduledProgram scheduledProgram, LocalDateTime startDateTime) {
        scheduledProgram.start(startDateTime);

        Workout workout = workoutRepository.findWorkoutByIdAndUserId(scheduledProgram.getWorkout().getId(), scheduledProgram.getUser().getId()).orElse(null);
        if (workout == null) {
            Sentry.captureMessage("Workout not found for scheduled program: " + scheduledProgram.getId());
            return false;
        }
        workout.start(startDateTime);


        scheduledProgramRepository.save(scheduledProgram);
        workoutRepository.save(workout);
        return true;
    }
}
