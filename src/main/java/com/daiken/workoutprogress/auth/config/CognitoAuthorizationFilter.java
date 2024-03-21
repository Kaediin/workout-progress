package com.daiken.workoutprogress.auth.config;

import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.daiken.workoutprogress.services.CognitoService;
import com.devskiller.friendly_id.FriendlyId;
import io.sentry.Sentry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CognitoAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    public static final String COGNITO_IDENTITY_POOL_URL = "https://cognito-idp.%s.amazonaws.com/%s";
    public static final String JSON_WEB_TOKEN_SET_URL_SUFFIX = "/.well-known/jwks.json";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String COGNITO_GROUPS = "cognito:groups";
    public static final String COGNITO_SUB = "sub";
    private static final String COGNITO_USERNAME = "username";

    private final CognitoService cognitoService;
    private final String userPoolId;
    private final String region;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CognitoAuthorizationFilter(CognitoService cognitoService, String userPoolId, String region,
                                      AuthenticationManager authManager) {
        super(authManager);

        this.cognitoService = cognitoService;
        this.userPoolId = userPoolId;
        this.region = region;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            response.addHeader("Error", "[CognitoAuthorizationFilter] Access denied: The Authorization " +
                    "header is missing crucial information to be able to grant access.");
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TOKEN_PREFIX, "");

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token, response));
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token, HttpServletResponse response) {
        // See if the 'mock' of CognitoService wants to process this token.
        UsernamePasswordAuthenticationToken processedToken = cognitoService.processAuthenticationToken(token);
        if (processedToken != null) {
            return processedToken;
        }

        try {
            // Decode JWT token
            DecodedJWT decodedJWT = JWT.decode(token);
            String keyId = decodedJWT.getKeyId();

            // Retrieve (cached) public key
            String cognitoIdentityPoolUrl = String.format(COGNITO_IDENTITY_POOL_URL, region, userPoolId);
            String jwksKidStoreURL = cognitoIdentityPoolUrl + JSON_WEB_TOKEN_SET_URL_SUFFIX;
            GuavaCachedJwkProvider guavaCachedJwkProvider = new GuavaCachedJwkProvider(
                    new UrlJwkProvider(new URL(jwksKidStoreURL)));
            Jwk jwk = guavaCachedJwkProvider.get(keyId);

            // Verify public key of JWT token
            JWT.require(Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null))
                    .build()
                    .verify(token);

            // Check if issuer matches our Cognito pool
            String issuer = decodedJWT.getIssuer();
            if (!cognitoIdentityPoolUrl.equals(issuer)) {
                logger.debug(String.format("Issuer %s in JWT token doesn't match Cognito Identity Pool %s", issuer, cognitoIdentityPoolUrl));
                return null;
            }

            // Fetch username
            String username = decodedJWT.getClaim(COGNITO_USERNAME).asString();
            String sub = decodedJWT.getClaim(COGNITO_SUB).asString();
            if (username != null) {
                // Fetch user's authorities
                List<String> groups = decodedJWT.getClaim(COGNITO_GROUPS).asList(String.class);
                List<SimpleGrantedAuthority> grantedAuthorities = Optional.ofNullable(groups).stream().flatMap(Collection::stream)
                        .map(group -> new SimpleGrantedAuthority(ROLE_PREFIX + group.toUpperCase()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(username, token,
                        grantedAuthorities);

                if (sub != null) {
                    String asn = FriendlyId.toFriendlyId(UUID.fromString(sub));

                    upaToken.setDetails(asn);
                }

                return upaToken;
            }
        } catch (MalformedURLException | JwkException | JWTVerificationException e) {
            logger.debug("Unable to verify JWT token", e);
            response.addHeader("Error", "[CognitoAuthorizationFilter] Unable to verify JWT token: "
                    + e.getMessage());
            Sentry.captureException(e);
        }

        return null;
    }

}
