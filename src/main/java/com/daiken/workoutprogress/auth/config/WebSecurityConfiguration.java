package com.daiken.workoutprogress.auth.config;

import com.daiken.workoutprogress.services.CognitoService;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration {

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.region}")
    private String region;

    @Value("${graphiql.enabled}")
    private Boolean graphiqlEnabled;

    @Value("${whitelist.allowed_remotes}")
    private String allowedRemotes;

    @Value("${whitelist.allowed_client_id}")
    private String allowedClientId;

    @Value("${whitelist.allowed_client_secret}")
    private String allowedClientSecret;

    @Autowired
    private CognitoService cognitoService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * - enable Cache-Control headers
     * - enable Strict-Transport-Security header on HTTPS with a max age of a year, including subdomains
     * - enable Content-Security-Policy to block everything since we're just an API
     * - enable Referrer-Policy, so we get full referrer on HTTPS
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.cacheControl(Customizer.withDefaults())
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig.includeSubDomains(true).maxAgeInSeconds(31536000))
                .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives(Boolean.TRUE.equals(graphiqlEnabled) ? "default-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:" : "default-src 'self'"))
                .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE))
        );
        http.csrf(httpSecurityCsrfConfigurer -> {
            try {
                httpSecurityCsrfConfigurer.disable()
                        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                // Need to permit all requests for graphql for the inspection (npm run generate:gql)
                                .requestMatchers("/graphql")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                        )
                        .addFilterBefore(new WhitelistAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), allowedClientId,
                                allowedClientSecret, allowedRemotes), UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(new CognitoAuthorizationFilter(cognitoService, userPoolId, region,
                                authenticationConfiguration.getAuthenticationManager()), CognitoAuthorizationFilter.class)
                        .addFilterAfter(new SecureCookieFilter(), CognitoAuthorizationFilter.class);
            } catch (Exception e) {
                Sentry.captureException(e);
            }
        });

        return http.build();
    }

}
