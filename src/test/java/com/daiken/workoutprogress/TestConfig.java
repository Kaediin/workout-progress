package com.daiken.workoutprogress;

import com.daiken.workoutprogress.api.graphql.UserMutationIntTest;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Create an instance of the services, because Spring just can't inject an instance
 * in the test class through the @Autowired annotation:
 */

@Configuration
public class TestConfig {

    @Bean
    public UserMutationIntTest userMutationIntTest() {
        return Mockito.mock(UserMutationIntTest.class);
    }
}
