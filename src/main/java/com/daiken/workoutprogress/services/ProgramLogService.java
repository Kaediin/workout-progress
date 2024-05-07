package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.api.graphql.input.ProgramLogInput;
import com.daiken.workoutprogress.models.*;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.repositories.ProgramLogGroupRepository;
import com.daiken.workoutprogress.repositories.ProgramLogRepository;
import com.daiken.workoutprogress.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for ProgramLog operations.
 */
@Service
public class ProgramLogService {

    private final ExerciseRepository exerciseRepository;
    private final ProgramRepository programRepository;
    private final ProgramLogGroupRepository programLogGroupRepository;
    private final ProgramLogRepository programLogRepository;
    private final UserService userService;

    @Autowired
    public ProgramLogService(
            ExerciseRepository exerciseRepository,
            ProgramRepository programRepository,
            ProgramLogGroupRepository programLogGroupRepository,
            ProgramLogRepository programLogRepository,
            UserService userService
    ) {
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
        this.programLogGroupRepository = programLogGroupRepository;
        this.programLogRepository = programLogRepository;
        this.userService = userService;
    }


    public ProgramLog createProgramLog(ProgramLogInput programLogInput) {
        ProgramLog programLog = makeProgramLogFromInput(programLogInput);

        if (programLogInput.subdivisions() != null && !programLogInput.subdivisions().isEmpty()) {
            for (ProgramLogInput subdivision : programLogInput.subdivisions()) {
                ProgramLog subdivisionProgramLog = makeProgramLogFromInput(subdivision);
                programLog.addSubdivision(subdivisionProgramLog);
            }
        }

        return programLog;
    }

    private ProgramLog makeProgramLogFromInput(ProgramLogInput programLogInput) {
        User user = userService.getContextUser();

        ProgramLogGroup programLogGroup = programLogInput.programLogGroupId() == null || programLogInput.programLogGroupId().isBlank() ? null : programLogGroupRepository.findById(programLogInput.programLogGroupId())
                .orElseThrow(() -> new IllegalArgumentException("ProgramLogGroup not found for id: " + programLogInput.programLogGroupId()));

        Program program = programLogGroup == null ? null : programRepository.findByIdAndUserId(programLogGroup.getProgram().getId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Program not found for id: " + programLogGroup.getProgram().getId()));

        Exercise exercise = programLogInput.exerciseId() == null ? null : exerciseRepository.findExerciseByIdAndUserId(programLogInput.exerciseId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found for id: " + programLogInput.exerciseId()));

        return new ProgramLog(programLogInput, programLogGroup, program, exercise);
    }
}
