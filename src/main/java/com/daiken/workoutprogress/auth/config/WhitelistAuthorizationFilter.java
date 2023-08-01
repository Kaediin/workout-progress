package com.daiken.workoutprogress.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class WhitelistAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String ROLE_WHITELIST_ONLY = "ROLE_WHITELIST_ONLY";
    private static final String TOKEN_PREFIX = "Basic ";
    private static final String AUTHORIZATION_HEADER_STRING = "Authorization";
    private static final String X_FORWARDED_FOR_HEADER_STRING = "X-Forwarded-For";
    private static final String COLON_DELIMITER = ":";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String allowedClientId;
    private final String allowedClientSecret;
    private final String allowedRemotes;

    public WhitelistAuthorizationFilter(
            AuthenticationManager authenticationManager,
            String allowedClientId,
            String allowedClientSecret,
            String allowedRemotes) {
        super(authenticationManager);

        this.allowedClientId = allowedClientId;
        this.allowedClientSecret = allowedClientSecret;
        this.allowedRemotes = allowedRemotes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR_HEADER_STRING);
        if (xForwardedFor == null) {
            chain.doFilter(request, response);
            return;
        }

        // Strip load balancer IP's
        String xForwardedForClient = Arrays.stream(xForwardedFor.split(","))
                .map(String::trim)
                .findFirst()
                .orElse(null);

        if (xForwardedForClient == null) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the remote client address is registerd as an allowed remote
        if (Arrays.stream(this.allowedRemotes.split(","))
                .map(String::trim)
                .noneMatch(xForwardedForClient::equals)) {
            chain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(AUTHORIZATION_HEADER_STRING);

        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // Decode basic auth ClientID:ClientSecret
        String token = authorization.replace(TOKEN_PREFIX, "");
        String decodedAuthorizationHeader = new String(Base64.getMimeDecoder().decode(token));

        int colonIndex = decodedAuthorizationHeader.indexOf(COLON_DELIMITER);
        if (colonIndex < 0) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the client matches
        String clientId = decodedAuthorizationHeader.substring(0, colonIndex);
        if (!allowedClientId.equals(clientId)) {
            chain.doFilter(request, response);
            return;
        }

        String clientSecret = decodedAuthorizationHeader.substring(colonIndex + 1);
        if (!allowedClientSecret.equals(clientSecret)) {
            chain.doFilter(request, response);
            return;
        }

        logger.info("Granted access for whitelisted IP: " + String.join(", ", xForwardedForClient));

        SecurityContextHolder.getContext().setAuthentication(getAuthentication());

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                "",
                "",
                Collections.singletonList(new SimpleGrantedAuthority(ROLE_WHITELIST_ONLY))
        );
    }

}
