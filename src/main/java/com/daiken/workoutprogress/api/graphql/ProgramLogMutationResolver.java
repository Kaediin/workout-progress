package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ProgramLogInput;
import com.daiken.workoutprogress.models.ProgramLog;
import com.daiken.workoutprogress.repositories.ProgramLogRepository;
import com.daiken.workoutprogress.services.ProgramLogService;
import graphql.kickstart.tools.GraphQLMutationResolver;
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

    private final ProgramLogRepository programLogRepository;
    private final ProgramLogService programLogService;

    @Autowired
    public ProgramLogMutationResolver(
            ProgramLogRepository programLogRepository,
            ProgramLogService programLogService
    ) {
        this.programLogRepository = programLogRepository;
        this.programLogService = programLogService;
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
}
