package fr.unice.polytech.isa.merchants.exceptions;

import java.io.Serializable;

/**
 * Merchant not found exception.
 * <p>
 * Indicates that a merchant was not found.
 * </p>
 */
public class MerchantNotFoundException extends Exception implements Serializable {
    /**
     * The email address that was not found.
     */
    private String email;

    /**
     * Default constructor.
     */
    public MerchantNotFoundException() {
        super("The requested merchant was not found.");
    }

    /**
     * Constructs a merchant not found exception.
     *
     * @param email The email address that was not found.
     */
    public MerchantNotFoundException(String email) {
        super("The merchant with email = " + email + " was not found.");
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
