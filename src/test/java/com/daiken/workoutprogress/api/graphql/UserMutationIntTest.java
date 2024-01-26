//package com.daiken.workoutprogress.api.graphql;
//
//import com.daiken.workoutprogress.model.User;
//import com.daiken.workoutprogress.services.UserService;
//import com.graphql.spring.boot.test.GraphQLResponse;
//import com.graphql.spring.boot.test.GraphQLTestTemplate;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class UserMutationIntTest {
//
//    @Autowired
//    public GraphQLTestTemplate graphQLTestTemplate;
//
//    @MockBean
//    UserService userServiceMock;
//
//    static User user = new User();
//
//    @BeforeAll
//    static void setUp() {
//        user.id = "someUniqueId";
//        user.fid = "someUniqueFriendlyId";
//    }
//
//    @Test
//    public void createUser() throws IOException {
////        doReturn(user).when(userServiceMock).createUser(user);
//        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/schema.graphql");
//        assertThat(response.isOk()).isTrue();
//        assertThat(response.get("$.data.createUser.id")).isNotNull();
//        assertThat(response.get("$.data.createUser.fid")).isEqualTo(user.fid);
//    }
//
//    @Test
//    @WithMockUser(username = "someUniqueId")
//    public void deleteUser() throws IOException {
////        doReturn(user).when(userServiceMock).deleteUser(user);
//        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/schema.graphql");
//        assertThat(response.isOk()).isTrue();
//        assertThat(response.get("$.data.deleteUser.id")).isNotNull();
//        assertThat(response.get("$.data.deleteUser.fid")).isEqualTo(user.fid);
//    }
//}
