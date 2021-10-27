package fr.unice.polytech.isa.accounts.exceptions;

import java.io.Serializable;

/**
 * Customer not found exception.
 * <p>
 * Indicates that a customer was not found.
 * </p>
 */
public class CustomerNotFoundException extends Exception implements Serializable {
    /**
     * The email address that was not found.
     */
    private String email;

    /**
     * Default constructor.
     */
    public CustomerNotFoundException() {
        super("The requested customer was not found.");
    }

    /**
     * Constructs a customer not found exception.
     *
     * @param email The email address that was not found.
     */
    public CustomerNotFoundException(String email) {
        super("The customer with email = " + email + " was not found.");
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
