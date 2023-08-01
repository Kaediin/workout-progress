package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.ExerciseLog;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.ExerciseLogRepository;
import com.daiken.workoutprogress.repository.ExerciseRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseLogQueryResolver implements GraphQLQueryResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;

    @Autowired
    public ExerciseLogQueryResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseRepository exerciseRepository,
                                    UserService userService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    public ExerciseLog latestLogByExerciseId(String exerciseLogId) {
        User me = userService.getContextUser();
        ExerciseLog log = exerciseLogRepository.findLastLogByUserIdAndExerciseId(me.id, exerciseLogId).orElseThrow(() -> new NullPointerException("Cant find exerciselog by id"));
        log.exercise = exerciseRepository.findById(log.exercise.id).orElseThrow(() -> new NullPointerException("Cant find exercise by id"));
        return log;
    }
}
