package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ProgramLogGroupInput;
import com.daiken.workoutprogress.exceptions.NotFoundException;
import com.daiken.workoutprogress.models.Program;
import com.daiken.workoutprogress.models.ProgramLogGroup;
import com.daiken.workoutprogress.models.ProgramLogGroupType;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ProgramLogGroupRepository;
import com.daiken.workoutprogress.repositories.ProgramLogRepository;
import com.daiken.workoutprogress.repositories.ProgramRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Mutation resolver for the Program entity.
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramLogGroupMutationResolver implements GraphQLMutationResolver {

    private final ProgramRepository programRepository;
    private final ProgramLogGroupRepository programLogGroupRepository;
    private final ProgramLogRepository programLogRepository;
    private final UserService userService;

    @Autowired
    public ProgramLogGroupMutationResolver(
            ProgramRepository programRepository,
            ProgramLogGroupRepository programLogGroupRepository,
            ProgramLogRepository programLogRepository,
            UserService userService) {
        this.programRepository = programRepository;
        this.programLogGroupRepository = programLogGroupRepository;
        this.programLogRepository = programLogRepository;
        this.userService = userService;
    }

    public ProgramLogGroup createProgramLogGroup(ProgramLogGroupInput input) {
        User me = userService.getContextUser();
        // Get program
        Program program = programRepository.findByIdAndUserId(input.programId(), me.getId()).orElse(null);
        if (program == null) {
            Sentry.captureException(new NotFoundException("[createProgramLogGroup] Program not found! id: " + input.programId()));
            log.error("[createProgramLogGroup] Program not found! id: {}", input.programId());
            return null;
        }

        ProgramLogGroup programLogGroup = new ProgramLogGroup(program, input.type());
        return programLogGroupRepository.save(programLogGroup);
    }

    public ProgramLogGroup updateProgramLogGroup(String id, ProgramLogGroupType type) {
        ProgramLogGroup programLogGroup = programLogGroupRepository.findById(id).orElse(null);
        if (programLogGroup == null) {
            Sentry.captureException(new NotFoundException("[updateProgramLogGroup] ProgramLogGroup not found! id: " + id));
            log.error("[updateProgram] Program not found! id: {}", id);
            return null;
        }

        programLogGroup.setType(type);

        return programLogGroupRepository.save(programLogGroup);
    }

    public Boolean deleteProgramLogGroup(String id) {
        // Check if the program exists
        ProgramLogGroup programLogGroup = programLogGroupRepository.findById(id).orElse(null);
        if (programLogGroup == null) {
            Sentry.captureException(new NotFoundException("[updateProgramLogGroup] ProgramLogGroup not found! id: " + id));
            log.error("[updateProgram] Program not found! id: {}", id);
            return null;
        }

        // Delete all logs of the program
        programLogRepository.deleteAll(programLogRepository.findAllByProgramLogGroupId(programLogGroup.getId()));

        // Delete group of the program
        programLogGroupRepository.delete(programLogGroup);
        return true;
    }
}
