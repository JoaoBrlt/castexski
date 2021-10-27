package fr.unice.polytech.isa.notifications.exceptions;

import java.io.Serializable;

/**
 * Notification not found exception.
 * <p>
 * Indicates that a notification was not found.
 * </p>
 */
public class NotificationNotFoundException extends Exception implements Serializable {
    /**
     * The identifier that was not found.
     */
    private int identifier;

    /**
     * Default constructor.
     */
    public NotificationNotFoundException() {
        super("The requested notification was not found.");
    }

    /**
     * Constructs a notification not found exception.
     *
     * @param identifier The identifier of the notification.
     */
    public NotificationNotFoundException(int identifier) {
        super("The notification with id = " + identifier + " was not found.");
    }

    /**
     * Returns the identifier that was not found.
     *
     * @return The identifier that was not found.
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier that was not found.
     *
     * @param identifier The new identifier that was not found.
     */
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
