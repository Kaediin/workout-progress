package com.daiken.workoutprogress.api.graphql;

import org.springframework.boot.test.mock.mockito.MockBean;

public class GraphqlTestContext {

    @MockBean
    ExerciseLogMutationResolver exerciseLogMutationResolver;
}
