package fr.unice.polytech.isa.accounts.exceptions;

import java.io.Serializable;

/**
 * Unavailable email exception
 * <p>
 * Indicates that an email address is already in use.
 * </p>
 */
public class UnavailableEmailException extends Exception implements Serializable {
    /**
     * The conflicting email address.
     */
    private String conflictingMail;

    /**
     * Default constructor.
     */
    public UnavailableEmailException() {
        super("The provided email address is already in use.");
    }

    /**
     * Constructs an unavailable email exception.
     *
     * @param email The conflicting email address.
     */
    public UnavailableEmailException(String email) {
        super("The email address = " + email + " is already in use.");
        conflictingMail = email;
    }

    /**
     * Returns the conflicting email address.
     *
     * @return The conflicting email address.
     */
    public String getConflictingMail() {
        return conflictingMail;
    }

    /**
     * Sets the conflicting email address.
     *
     * @param email The new conflicting email address.
     */
    public void setConflictingMail(String email) {
        this.conflictingMail = conflictingMail;
    }
}
