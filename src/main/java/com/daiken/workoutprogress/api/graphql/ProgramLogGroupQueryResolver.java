package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.models.ProgramLogGroup;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ProgramLogGroupRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ProgramLogGroupQueryResolver implements GraphQLQueryResolver {

    private final ProgramLogGroupRepository programLogGroupRepository;
    private final UserService userService;

    @Autowired
    public ProgramLogGroupQueryResolver(
            ProgramLogGroupRepository programLogGroupRepository,
            UserService userService
    ) {
        this.programLogGroupRepository = programLogGroupRepository;
        this.userService = userService;
    }

    public List<ProgramLogGroup> programLogGroupsByProgramId(String programId) {
        User me = userService.getContextUser();
        return programLogGroupRepository.findAllByProgramId(programId);
    }
}
