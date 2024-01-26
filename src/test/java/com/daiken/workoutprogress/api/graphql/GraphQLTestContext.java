//package com.daiken.workoutprogress.api.graphql;
//
//import com.daiken.workoutprogress.repositories.*;
//import com.daiken.workoutprogress.services.CognitoService;
//import com.daiken.workoutprogress.services.MigrationService;
//import com.daiken.workoutprogress.services.UserService;
//import com.daiken.workoutprogress.services.WorkoutService;
//import com.daiken.workoutprogress.tasks.FetchWorkoutsTask;
//import com.zhokhov.graphql.datetime.GraphQLDate;
//import com.zhokhov.graphql.datetime.GraphQLLocalDate;
//import com.zhokhov.graphql.datetime.GraphQLLocalDateTime;
//import com.zhokhov.graphql.datetime.GraphQLLocalTime;
//import graphql.schema.GraphQLScalarType;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//
//public class GraphQLTestContext {
//
//    @MockBean
//    ExerciseLogRepository exerciseLogRepository;
//
//    @MockBean
//    ExerciseRepository exerciseRepository;
//
//    @MockBean
//    PreferenceRepository preferenceRepository;
//
//    @MockBean
//    UserRepository userRepository;
//
//    @MockBean
//    WorkoutRepository workoutRepository;
//
//    @MockBean
//    CognitoService cognitoService;
//
//    @MockBean
//    MigrationService migrationService;
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    WorkoutService workoutService;
//
//    @MockBean
//    FetchWorkoutsTask fetchWorkoutsTask;
//
//    @Bean
//    GraphQLScalarType date() {
//        return new GraphQLDate();
//    }
//
//    @Bean
//    GraphQLScalarType localDateTime() {
//        return new GraphQLLocalDateTime();
//    }
//
//    @Bean
//    GraphQLScalarType localTime() {
//        return new GraphQLLocalTime();
//    }
//
//    @Bean
//    GraphQLScalarType localDate() {
//        return new GraphQLLocalDate();
//    }
//}
