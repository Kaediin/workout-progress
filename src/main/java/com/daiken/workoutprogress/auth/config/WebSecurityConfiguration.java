package com.daiken.workoutprogress.auth.config;

import com.daiken.workoutprogress.services.CognitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager();

        /**
         * - enable Cache-Control headers
         * - enable Strict-Transport-Security header on HTTPS with a max age of a year, including subdomains
         * - enable Content-Security-Policy to block everything since we're just an API
         * - enable Referrer-Policy, so we get full referrer on HTTPS
         */
        http.headers().cacheControl();
        http.headers().httpStrictTransportSecurity()
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000);
        if (Boolean.TRUE.equals(graphiqlEnabled)) {
            http.headers().contentSecurityPolicy(
                    "default-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:"
            );
        } else {
            http.headers().contentSecurityPolicy("default-src 'self'");
        }
        http.headers().referrerPolicy(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE);

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new WhitelistAuthorizationFilter(authenticationManager, allowedClientId,
                        allowedClientSecret, allowedRemotes), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CognitoAuthorizationFilter(cognitoService, userPoolId, region,
                        authenticationManager), CognitoAuthorizationFilter.class)
                .addFilterAfter(new SecureCookieFilter(), CognitoAuthorizationFilter.class);
    }

}
