package fr.unice.polytech.isa.common.entities.notifications;

import java.util.Objects;

/**
 * SMS.
 * <p>
 * Represents a text message.
 * </p>
 */
public class SMS {
    /**
     * The phone number of the sender.
     */
    private String from;

    /**
     * The phone number of the recipient.
     */
    private String to;

    /**
     * The text message.
     */
    private String message;

    /**
     * Default constructor.
     */
    public SMS() {}

    /**
     * Constructs a text message.
     *
     * @param from The phone number of the sender.
     * @param to The phone number of the recipient.
     * @param message The text message.
     */
    public SMS(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    /**
     * Returns the phone number of the sender.
     *
     * @return The phone number of the sender.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the phone number of the sender.
     *
     * @param from The new phone number of the sender.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the phone number of the recipient.
     *
     * @return The phone number of the recipient.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the phone number of the recipient.
     *
     * @param to The new phone number of the recipient.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the text message.
     *
     * @return The text message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the text message.
     *
     * @param message The new text message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Indicates whether another object is equal to this text message.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this text message is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // Same instance.
        if (this == object) {
            return true;
        }

        // Not same class.
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        // Same attributes.
        SMS text = (SMS) object;
        return (
            Objects.equals(getFrom(), text.getFrom()) &&
            Objects.equals(getTo(), text.getTo()) &&
            Objects.equals(getMessage(), text.getMessage())
        );
    }

    /**
     * Returns a hash code value for the text message.
     *
     * @return A hash code value for the text message.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getFrom(),
            getTo(),
            getMessage()
        );
    }

    /**
     * Returns a string representation of the text message.
     *
     * @return A string representation of the text message.
     */
    @Override
    public String toString() {
        return "SMS{" +
            ", from='" + from + '\'' +
            ", to='" + to + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
