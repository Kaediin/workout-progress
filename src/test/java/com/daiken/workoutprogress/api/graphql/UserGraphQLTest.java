//package com.daiken.workoutprogress.api.graphql;
//
//import com.daiken.workoutprogress.models.User;
//import com.daiken.workoutprogress.repositories.UserRepository;
//import com.graphql.spring.boot.test.GraphQLResponse;
//import com.graphql.spring.boot.test.GraphQLTest;
//import com.graphql.spring.boot.test.GraphQLTestTemplate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.doReturn;
//
//@GraphQLTest
//@Import({GraphQLConfig.class, GraphQLTestContext.class})
//public class UserGraphQLTest {
//
//    private User user;
//
//    @Autowired
//    private GraphQLTestTemplate graphQLTestTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp(){
//        user = new User("fid-1");
//        user.setId("user-1");
//    }
//
//    @Test
//    void queryFindUserById() throws IOException {
//        doReturn(Optional.of(user)).when(userRepository).findOneByFid("fid-1");
//
//        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/findUserById.graphql");
//
//        assertTrue(response.isOk());
//    }
//}
