package com.commercehub.user;

import java.util.UUID;
import java.util.regex.Pattern;

public record User(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z]{3,20}@[A-Za-z]{3,6}\\.[a-z]{2,4}$");

    public User {
        validateId(id);
        validateFirstName(firstName);
        validateLastName(lastName);
        validateEmail(email);

    }

    public User(String firstName, String lastName, String email) {
        this(UUID.randomUUID(), firstName, lastName, email);
    }

    private static void validateId(UUID userId) {
        if (userId == null)
            throw new IllegalArgumentException("userId must be not null");
    }
    private static void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("user email must be not null or blank");
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("email should match the following pattern: body@domain");
    }
    private static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("user first name must be not null or blank");
    }
    private static void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("user last name must be not null or blank");
    }
}
