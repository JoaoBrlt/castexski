package fr.unice.polytech.isa.accounts.exceptions;

import java.io.Serializable;

/**
 * User not found exception.
 * <p>
 * Indicates that a user was not found.
 * </p>
 */
public class UserNotFoundException extends Exception implements Serializable {
    /**
     * The email address that was not found.
     */
    private String email;

    /**
     * Default constructor.
     */
    public UserNotFoundException() {
        super("The requested user was not found.");
    }

    /**
     * Constructs a user not found exception.
     *
     * @param email The email address that was not found.
     */
    public UserNotFoundException(String email) {
        super("The user with email = " + email + " was not found.");
        this.email = email;
    }

    /**
     * Returns the email address that was not found.
     *
     * @return The email address that was not found.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address that was not found.
     *
     * @param email The email address that was not found.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
