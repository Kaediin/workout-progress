package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.Program;
import com.daiken.workoutprogress.models.ScheduledProgram;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ProgramRepository;
import com.daiken.workoutprogress.repositories.ScheduledProgramRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Query resolver for the Program entity.
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramQueryResolver implements GraphQLQueryResolver {

    private final ProgramRepository programRepository;
    private final ScheduledProgramRepository scheduledProgramRepository;
    private final UserService userService;

    @Autowired
    public ProgramQueryResolver(
            ProgramRepository programRepository,
            ScheduledProgramRepository scheduledProgramRepository,
            UserService userService
    ) {
        this.programRepository = programRepository;
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.userService = userService;
    }

    public List<Program> myPrograms() {
        User me = userService.getContextUser();
        return programRepository.findAllByUserId(me.getId());
    }

    public List<ScheduledProgram> myScheduledPrograms() {
        User me = userService.getContextUser();
        return scheduledProgramRepository.findAllByUserId(me.getId());
    }

    public Program programById(String id) {
        User me = userService.getContextUser();
        return programRepository.findByIdAndUserId(id, me.getId()).orElse(null);
    }

    public ScheduledProgram scheduledProgramById(String id) {
        User me = userService.getContextUser();
        return scheduledProgramRepository.findById(id).orElse(null);
    }
}
