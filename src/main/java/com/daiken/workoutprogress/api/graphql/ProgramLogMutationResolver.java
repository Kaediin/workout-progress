package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ProgramLogInput;
import com.daiken.workoutprogress.exceptions.NotFoundException;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.ProgramLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.models.Workout;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ProgramLogRepository;
import com.daiken.workoutprogress.repositories.WorkoutRepository;
import com.daiken.workoutprogress.services.ProgramLogService;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Mutation resolver for the ProgramLog entity.
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramLogMutationResolver implements GraphQLMutationResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ProgramLogRepository programLogRepository;
    private final ProgramLogService programLogService;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ProgramLogMutationResolver(
            ExerciseLogRepository exerciseLogRepository,
            ProgramLogRepository programLogRepository,
            ProgramLogService programLogService,
            UserService userService,
            WorkoutRepository workoutRepository
    ) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.programLogRepository = programLogRepository;
        this.programLogService = programLogService;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
    }

    public ProgramLog createProgramLog(ProgramLogInput programLogInput) {
        return programLogRepository.save(programLogService.createProgramLog(programLogInput));
    }

    public ProgramLog updateProgramLog(String id, ProgramLogInput programLogInput) {
        ProgramLog programLog = programLogService.createProgramLog(programLogInput);
        programLog.setId(id);

        return programLogRepository.save(programLog);
    }

    public Boolean deleteProgramLog(String id) {
        programLogRepository.deleteById(id);
        return true;
    }

    public ProgramLog markLogAsCompleted(String id, String workoutId, String zonedDateTimeString) {
        User me = userService.getContextUser();
        ProgramLog programLog = programLogRepository.findById(id).orElse(null);
        if (programLog == null) {
            Sentry.captureException(new NotFoundException("[markLogAsCompleted] Program log not found! id: " + id));
            log.error("[markLogAsCompleted] Program log not found! id: {}", id);
            return null;
        }
        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElse(null);
        if (workout == null) {
            Sentry.captureException(new NotFoundException("[markLogAsCompleted] Workout not found! id: " + workoutId));
            log.error("[markLogAsCompleted] Workout not found! id: {}", workoutId);
            return null;
        }


        if (programLog.getExercise() != null) {
            ExerciseLog log = new ExerciseLog(me, programLog, workout, zonedDateTimeString);
            programLog.setExerciseLog(exerciseLogRepository.save(log));
        } else if (programLog.getSubdivisions() != null && !programLog.getSubdivisions().isEmpty()) {
            for (ProgramLog subdivision : programLog.getSubdivisions()) {
                ExerciseLog log = new ExerciseLog(me, subdivision, workout, zonedDateTimeString);
                subdivision.setExerciseLog(exerciseLogRepository.save(log));
            }
        }

        return programLogRepository.save(programLog);
    }

//    public ProgramLog adjustLogAndMarkAsCompleted(String id, ProgramLogInput input, String workoutId, String zonedDateTimeString) {
//        User me = userService.getContextUser();
//        ProgramLog originalProgramLog = programLogRepository.findById(id).orElse(null);
//        if (originalProgramLog == null) {
//            Sentry.captureException(new NotFoundException("[markLogAsCompleted] Program log not found! id: " + id));
//            log.error("[markLogAsCompleted] Program log not found! id: {}", id);
//            return null;
//        }
//        Workout workout = workoutRepository.findWorkoutByIdAndUserId(workoutId, me.getId()).orElse(null);
//        if (workout == null) {
//            Sentry.captureException(new NotFoundException("[markLogAsCompleted] Workout not found! id: " + workoutId));
//            log.error("[markLogAsCompleted] Workout not found! id: {}", workoutId);
//            return null;
//        }
//
//        ProgramLog programLog = programLogService.createProgramLog(input);
//
//        if (originalProgramLog.getExercise() != null) {
//            ExerciseLog log = new ExerciseLog(me, programLog, workout, zonedDateTimeString);
//            originalProgramLog.setExerciseLog(exerciseLogRepository.save(log));
//        } else if (input.subdivisions() != null && !input.subdivisions().isEmpty()) {
////            for (ProgramLogInput subdivision : input.subdivisions()) {
////                ExerciseLog log = new ExerciseLog(me, subdivision, workout, zonedDateTimeString);
////                subdivision.setExerciseLog(exerciseLogRepository.save(log));
////            }
//        }
//
//        return null;
//    }
}
