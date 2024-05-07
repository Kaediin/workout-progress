package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.Program;
import com.daiken.workoutprogress.models.ProgramLogGroup;
import com.daiken.workoutprogress.repositories.ProgramLogGroupRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Resolver for Program
 */
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramResolver implements GraphQLResolver<Program> {

    private final ProgramLogGroupRepository programLogGroupRepository;
    private final UserService userService;

    @Autowired
    public ProgramResolver(
            ProgramLogGroupRepository programLogGroupRepository,
            UserService userService
    ) {
        this.programLogGroupRepository = programLogGroupRepository;
        this.userService = userService;
    }

    /**
     * Get log groups of the program
     *
     * @param program Program
     * @return Log groups of the program
     */
    public List<ProgramLogGroup> logGroups(Program program) {
        // Validate that the user is authorized to view the program
        if (!program.getUser().getId().equals(userService.getContextUser().getId())) {
            return null;
        }

        return programLogGroupRepository.findAllByProgramId(program.getId());
    }
}
