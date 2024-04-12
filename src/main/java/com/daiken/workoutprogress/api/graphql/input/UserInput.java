package com.daiken.workoutprogress.api.graphql.input;


/**
 * @param firstName  The first name of the user
 * @param middleName The middle name of the user
 * @param lastName   The last name of the user
 * @param email      The email of the user
 * @param password   The password of the user
 * @deprecated This class is deprecated and will be removed in a future release.
 * Represents the input for creating a user.
 */
public record UserInput(String firstName, String middleName, String lastName, String email, String password) {
}
