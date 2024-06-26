package com.daiken.workoutprogress.services;

import com.daiken.workoutprogress.models.CognitoUser;
import com.devskiller.friendly_id.FriendlyId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;

import java.util.Collection;
import java.util.Optional;

/**
 * Service for interacting with AWS Cognito.
 */
@Slf4j
@Service
public class CognitoService {

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.cognitoId}")
    private String cognitoId;

    @Value("${aws.cognito.cognitoKey}")
    private String cognitoKey;

    @Value("${aws.cognito.region}")
    private String region;

    private CognitoIdentityProviderClient identityProvider;

    public CognitoService() {
    }

    /**
     * Get the CognitoIdentityProviderClient for the given Cognito ID, Cognito Key, and region.
     *
     * @return CognitoIdentityProviderClient
     */
    private CognitoIdentityProviderClient getIdentityProvider() {
        if (identityProvider == null) {
            identityProvider = getIdentityProvider(cognitoId, cognitoKey, region);
        }
        return identityProvider;
    }

    /**
     * Get the CognitoIdentityProviderClient for the given Cognito ID, Cognito Key, and region.
     * @param cognitoId Cognito ID
     * @param cognitoKey Cognito Key
     * @param region Region
     * @return CognitoIdentityProviderClient
     */
    private CognitoIdentityProviderClient getIdentityProvider(String cognitoId, String cognitoKey, String region) {
        AwsCredentials credentials = AwsBasicCredentials.create(cognitoId, cognitoKey);
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        return CognitoIdentityProviderClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region))
                .build();
    }

    /**
     * Find a user by their friendly ID.
     * @param fid Friendly ID
     * @return Optional of CognitoUser
     */
    public Optional<CognitoUser> findUser(String fid) {
        if (fid == null) {
            log.error("[findUser] fid is null");
            return Optional.empty();
        }

        return findCognitoUserBySubjectFilter("sub=\"" + FriendlyId.toUuid(fid) + "\"");
    }

    /**
     * Find a user by their subject filter.
     * @param subjectFilter Subject filter
     * @return Optional of CognitoUser
     */
    public Optional<CognitoUser> findCognitoUserBySubjectFilter(String subjectFilter) {
        ListUsersRequest listUsersRequest = ListUsersRequest.builder()
                .limit(1)
                .filter(subjectFilter)
                .userPoolId(userPoolId)
                .build();
        ListUsersResponse listUsersResult = getIdentityProvider().listUsers(listUsersRequest);

        return Optional.ofNullable(listUsersResult.users()).stream().flatMap(Collection::stream)
                .map(CognitoUser::new)
                .findFirst();
    }

    /**
     * Return an Authentication object for the given JWT. Always returns null, but exists, so we can hook in our testing
     * framework to mock valid JWTs for Cognito for which we don't own the private key.
     */
    public UsernamePasswordAuthenticationToken processAuthenticationToken(String token) {
        // Always return null at runtime. This allows mocking to occur during integration tests.
        return null;
    }
}
