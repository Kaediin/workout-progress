package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.api.graphql.WorkoutMutationResolver;
import com.daiken.workoutprogress.api.graphql.input.ScheduledProgramInput;
import com.daiken.workoutprogress.exceptions.NotFoundException;
import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.*;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Service for Program operations.
 */
@Service
@Slf4j
public class ProgramService {

    private final ProgramLogRepository programLogRepository;
    private final ProgramLogGroupRepository programLogGroupRepository;
    private final ProgramWorkoutRepository programWorkoutRepository;
    private final ProgramWorkoutGroupRepository programWorkoutGroupRepository;
    private final ProgramWorkoutLogRepository programWorkoutLogRepository;
    private final ScheduledProgramRepository scheduledProgramRepository;
    private final WorkoutMutationResolver workoutMutationResolver;
    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;

    @Autowired
    public ProgramService(
            ProgramLogRepository programLogRepository,
            ProgramLogGroupRepository programLogGroupRepository,
            ProgramWorkoutRepository programWorkoutRepository,
            ProgramWorkoutGroupRepository programWorkoutGroupRepository,
            ProgramWorkoutLogRepository programWorkoutLogRepository,
            ScheduledProgramRepository scheduledProgramRepository,
            WorkoutMutationResolver workoutMutationResolver,
            WorkoutRepository workoutRepository,
            WorkoutService workoutService
    ) {
        this.programLogRepository = programLogRepository;
        this.programLogGroupRepository = programLogGroupRepository;
        this.programWorkoutRepository = programWorkoutRepository;
        this.programWorkoutGroupRepository = programWorkoutGroupRepository;
        this.programWorkoutLogRepository = programWorkoutLogRepository;
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.workoutMutationResolver = workoutMutationResolver;
        this.workoutRepository = workoutRepository;
        this.workoutService = workoutService;
    }

    public Boolean deleteScheduledProgram(String scheduledProgramId, String workoutId) {
        scheduledProgramRepository.deleteById(scheduledProgramId);
        workoutMutationResolver.deleteWorkout(workoutId);
        return true;
    }

    public Boolean startScheduledProgram(ScheduledProgram scheduledProgram, LocalDateTime startDateTime) {
        scheduledProgram.start(startDateTime);

        Workout workout = workoutRepository.findWorkoutByIdAndUserId(scheduledProgram.getProgramWorkout().getWorkout().getId(), scheduledProgram.getUser().getId()).orElse(null);
        if (workout == null) {
            Sentry.captureMessage("Workout not found for scheduled program: " + scheduledProgram.getId());
            return false;
        }
        workout.start(startDateTime);


        scheduledProgramRepository.save(scheduledProgram);
        workoutRepository.save(workout);
        return true;
    }

    public Boolean endScheduledProgram(ScheduledProgram scheduledProgram, LocalDateTime endDateTime) {
        scheduledProgram.end(endDateTime);

        Workout workout = workoutRepository.findWorkoutByIdAndUserId(scheduledProgram.getProgramWorkout().getWorkout().getId(), scheduledProgram.getUser().getId()).orElse(null);
        if (workout == null) {
            Sentry.captureMessage("Workout not found for scheduled program: " + scheduledProgram.getId());
            return false;
        }
        workout.endWorkout(endDateTime);


        scheduledProgramRepository.save(scheduledProgram);
        workoutRepository.save(workout);
        return true;
    }

    public ProgramWorkout createProgramWorkout(Program program, ScheduledProgramInput input, User me) {
        // Get all program logs
        List<ProgramLog> programLogs = programLogRepository.findAllByProgramId(program.getId());
        if (programLogs.isEmpty()) {
            Sentry.captureException(new NotFoundException("[scheduledProgram] Program logs not found! programId: " + program.getId()));
            log.error("[scheduledProgram] Program logs not found! programId: {}", program.getId());
            return null;
        }

        // Get all exercises
        List<Exercise> exercises =
                programLogs.stream()
                        .flatMap(programLog -> programLog.getExercises().stream())
                        .filter(Objects::nonNull)
                        .toList();

        // Create workout
        Workout workout = new Workout();
        workout.setName(input.workoutName());
        workout.setMuscleGroups(workoutService.getMuscleGroupsByExercises(exercises));
        workout.setUser(me);
        workout.setActive(false);
        workout.setStatus(WorkoutStatus.SCHEDULED);
        workout.setRemark(input.remark());

        // Save workout to create id and thus a reference
        workout = workoutRepository.save(workout);

        // Save program workout
        ProgramWorkout programWorkout = programWorkoutRepository.save(new ProgramWorkout(program, workout));

        // Create workout groups
        programLogGroupRepository.findAllByProgramId(program.getId())
                .forEach(programLogGroup -> {
                    // Create workout group
                    ProgramWorkoutGroup workoutGroup = programWorkoutGroupRepository.save(new ProgramWorkoutGroup(programWorkout, programLogGroup.getType()));

                    // Get all logs for this group
                    programLogs.stream()
                            // Only convert logs for this group
                            .filter(programLog -> programLog.getProgramLogGroup().getId().equals(programLogGroup.getId()))
                            // Create new program log
                            .forEach(programLog -> {
                                // Deep clone program log
                                ProgramLog newProgramLog = new ProgramLog(programLog);
                                newProgramLog.setProgramLogGroup(null);
                                newProgramLog.setProgram(null);
                                // Save copy log
                                newProgramLog = programLogRepository.save(newProgramLog);

                                // Create program workout log
                                programWorkoutLogRepository.save(
                                        new ProgramWorkoutLog(
                                                newProgramLog,
                                                programLog,
                                                programWorkout,
                                                workoutGroup
                                        )
                                );
                            });
                });

        return programWorkout;
    }
}
