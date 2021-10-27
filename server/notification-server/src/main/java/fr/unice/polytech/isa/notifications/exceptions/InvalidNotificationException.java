package fr.unice.polytech.isa.notifications.exceptions;

import java.io.Serializable;

/**
 * Invalid notification exception.
 * <p>
 * Indicates that a notification cannot be processed.
 * </p>
 */
public class InvalidNotificationException extends Exception implements Serializable {
    /**
     * Constructs an invalid notification exception.
     */
    public InvalidNotificationException() {
        super("The supplied notification cannot be processed.");
    }

    /**
     * Constructs an invalid notification exception.
     *
     * @param message The detail message.
     */
    public InvalidNotificationException(String message) {
        super(message);
    }
}
