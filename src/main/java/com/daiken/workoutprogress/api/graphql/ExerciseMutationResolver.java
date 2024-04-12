package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.ExerciseInput;
import com.daiken.workoutprogress.models.Exercise;
import com.daiken.workoutprogress.models.ExerciseLog;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.ExerciseLogRepository;
import com.daiken.workoutprogress.repositories.ExerciseRepository;
import com.daiken.workoutprogress.services.ExerciseService;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mutation resolver for Exercise
 */
@Slf4j
@PreAuthorize("isAuthenticated()")
@Component
public class ExerciseMutationResolver implements GraphQLMutationResolver {

    private final ExerciseLogRepository exerciseLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;

    @Autowired
    public ExerciseMutationResolver(ExerciseLogRepository exerciseLogRepository,
                                    ExerciseRepository exerciseRepository,
                                    ExerciseService exerciseService,
                                    UserService userService) {
        this.exerciseLogRepository = exerciseLogRepository;
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    /**
     * Create an exercise
     *
     * @param input Exercise input
     * @return Created exercise
     */
    public Exercise createExercise(ExerciseInput input) {
        User me = userService.getContextUser();
        return exerciseRepository.save(new Exercise(input, me));
    }

    /**
     * Add onboarding exercises
     * @param ids List of onboarding exercise ids
     * @return True if successful
     */
    public Boolean addOnboardingExercises(List<String> ids) {
        // Get the onboarding exercises that match the given ids
        List<Exercise> exercises = exerciseService.getOnboardingExercises()
                .stream()
                .filter(exercise -> ids.contains(exercise.getId()))
                .peek(exercise -> exercise.setId(null))
                .toList();
        exerciseRepository.saveAll(exercises);
        return true;
    }

    /**
     * Update an exercise
     * @param id Exercise id
     * @param input Exercise input
     * @return Updated exercise
     */
    public Exercise updateExercise(String id, ExerciseInput input) {
        User me = userService.getContextUser();
        Exercise exercise = exerciseRepository.findExerciseByIdAndUserId(id, me.getId()).orElseThrow(() -> {
            log.error("[updateExercise] Exercise not found with given id {}", id);
            return new NullPointerException("Exercise not found with given id");
        });
        exercise.update(input);
        return exerciseRepository.save(exercise);
    }

    /**
     * Delete an exercise and its logs
     * @param id Exercise id
     * @return True if successful
     */
    public Boolean deleteExercise(String id) {
        User me = userService.getContextUser();
        Exercise exercise = exerciseRepository.findExerciseByIdAndUserId(id, me.getId()).orElseThrow(() -> {
            log.error("[deleteExercise] Exercise not found with given id {}", id);
            return new NullPointerException("Exercise not found with given id");
        });
        List<ExerciseLog> logs = exerciseLogRepository.findAllByUserIdAndExerciseIdOrderByLogDateTimeDesc(me.getId(), exercise.getId());
        exerciseLogRepository.deleteAll(logs);
        exerciseRepository.delete(exercise);
        return true;
    }
}
