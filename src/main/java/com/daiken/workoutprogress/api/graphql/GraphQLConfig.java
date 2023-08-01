package com.daiken.workoutprogress.api.graphql;

import graphql.kickstart.tools.SchemaParserDictionary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {

    @Bean
    public SchemaParserDictionary schemaParserDictionary() {
        return new SchemaParserDictionary();
    }
}
