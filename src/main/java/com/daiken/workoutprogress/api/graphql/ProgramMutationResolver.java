package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ProgramInput;
import com.daiken.workoutprogress.api.graphql.input.ScheduledProgramInput;
import com.daiken.workoutprogress.exceptions.NotFoundException;
import com.daiken.workoutprogress.exceptions.UnauthorizedException;
import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.*;
import com.daiken.workoutprogress.services.ProgramService;
import com.daiken.workoutprogress.services.UserService;
import com.daiken.workoutprogress.services.WorkoutService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Mutation resolver for the Program entity.
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramMutationResolver implements GraphQLMutationResolver {

    private final ProgramLogGroupRepository programLogGroupRepository;
    private final ProgramLogRepository programLogRepository;
    private final ProgramRepository programRepository;
    private final ProgramService programService;
    private final ScheduledProgramRepository scheduledProgramRepository;
    private final UserService userService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;

    @Autowired
    public ProgramMutationResolver(
            ProgramRepository programRepository,
            ProgramLogGroupRepository programLogGroupRepository,
            ProgramLogRepository programLogRepository,
            ProgramService programService,
            ScheduledProgramRepository scheduledProgramRepository,
            UserService userService,
            WorkoutRepository workoutRepository,
            WorkoutService workoutService
    ) {
        this.programRepository = programRepository;
        this.programLogGroupRepository = programLogGroupRepository;
        this.programLogRepository = programLogRepository;
        this.programService = programService;
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.userService = userService;
        this.workoutRepository = workoutRepository;
        this.workoutService = workoutService;
    }

    public Program createProgram(ProgramInput input) {
        User me = userService.getContextUser();
        Program program = new Program(input, me);
        return programRepository.save(program);
    }

    public Program updateProgram(String id, ProgramInput input) {
        User me = userService.getContextUser();
        Program program = programRepository.findById(id).orElse(null);
        if (program == null) {
            Sentry.captureException(new NotFoundException("[updateProgram] Program not found! id: " + id));
            log.error("[updateProgram] Program not found! id: {}", id);
            return null;
        }
        if (!program.getUser().getId().equals(me.getId())) {
            Sentry.captureException(new UnauthorizedException("[updateProgram] User is not authorized to update the program! id: " + id));
            log.error("[updateProgram] User is not authorized to update the program! id: {}", id);
            return null;
        }
        program.update(input);

        return programRepository.save(program);
    }

    public Boolean deleteProgram(String id) {
        User me = userService.getContextUser();
        // Check if the program exists
        Program program = programRepository.findById(id).orElse(null);
        if (program == null) {
            Sentry.captureException(new NotFoundException("[deleteProgram] Program not found! id: " + id));
            log.error("[deleteProgram] Program not found! id: {}", id);
            return false;
        }
        if (!program.getUser().getId().equals(me.getId())) {
            Sentry.captureException(new UnauthorizedException("[deleteProgram] User is not authorized to delete the program! id: " + id));
            log.error("[deleteProgram] User is not authorized to delete the program! id: {}", id);
            return false;
        }

        // Delete all groups of the program
        programLogGroupRepository.deleteAll(programLogGroupRepository.findAllByProgramId(program.getId()));

        // Delete all logs of the program
        programLogRepository.deleteAll(programLogRepository.findAllByProgramId(program.getId()));

        // Delete the program
        programRepository.delete(program);
        return true;
    }

    public ScheduledProgram scheduleProgram(ScheduledProgramInput input) {
        User me = userService.getContextUser();
        Program program = programRepository.findById(input.programId()).orElse(null);
        if (program == null) {
            Sentry.captureException(new NotFoundException("[scheduledProgram] Program not found! id: " + input.programId()));
            log.error("[scheduledProgram] Program not found! id: {}", input.programId());
            return null;
        }
        List<ProgramLog> programLogs = programLogRepository.findAllByProgramId(program.getId());
        if (programLogs.isEmpty()) {
            Sentry.captureException(new NotFoundException("[scheduledProgram] Program logs not found! programId: " + program.getId()));
            log.error("[scheduledProgram] Program logs not found! programId: {}", program.getId());
            return null;
        }

        // Create program
        ScheduledProgram scheduledProgram = new ScheduledProgram(input, program, me);

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
        workout.setProgram(program);

        scheduledProgram.setWorkout(workoutRepository.save(workout));

        return scheduledProgramRepository.save(scheduledProgram);
    }

    public ScheduledProgram updateScheduledProgram(String id, ScheduledProgramInput input) {
        User me = userService.getContextUser();
        ScheduledProgram scheduledProgram = scheduledProgramRepository.findById(id).orElse(null);
        if (scheduledProgram == null) {
            Sentry.captureException(new NotFoundException("[updateScheduledProgram] Scheduled program not found! id: " + id));
            log.error("[updateScheduledProgram] Scheduled program not found! id: {}", id);
            return null;
        }
        if (!scheduledProgram.getUser().getId().equals(me.getId())) {
            Sentry.captureException(new UnauthorizedException("[updateScheduledProgram] User is not authorized to update the scheduled program! id: " + id));
            log.error("[updateScheduledProgram] User is not authorized to update the scheduled program! id: {}", id);
            return null;
        }

        scheduledProgram.update(input);

        Workout workout = workoutRepository.findById(scheduledProgram.getWorkout().getId()).orElse(null);
        if (workout == null) {
            Sentry.captureException(new NotFoundException("[updateScheduledProgram] Workout not found! id: " + scheduledProgram.getWorkout().getId()));
            log.error("[updateScheduledProgram] Workout not found! id: {}", scheduledProgram.getWorkout().getId());
            return null;
        }

        workoutRepository.save(workout.updateFromScheduledProgram(input));

        return scheduledProgramRepository.save(scheduledProgram);
    }

    public Boolean deleteScheduledProgram(String id) {
        User me = userService.getContextUser();
        // Check if the scheduled program exists
        ScheduledProgram scheduledProgram = scheduledProgramRepository.findById(id).orElse(null);
        if (scheduledProgram == null) {
            Sentry.captureException(new NotFoundException("[deleteScheduledProgram] Scheduled program not found! id: " + id));
            log.error("[deleteScheduledProgram] Scheduled program not found! id: {}", id);
            return false;
        }
        if (!scheduledProgram.getUser().getId().equals(me.getId())) {
            Sentry.captureException(new UnauthorizedException("[deleteScheduledProgram] User is not authorized to delete the scheduled program! id: " + id));
            log.error("[deleteScheduledProgram] User is not authorized to delete the scheduled program! id: {}", id);
            return false;
        }

        return programService.deleteScheduledProgram(scheduledProgram.getId(), scheduledProgram.getWorkout().getId());
    }
}
