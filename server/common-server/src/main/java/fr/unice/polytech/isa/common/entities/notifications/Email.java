package fr.unice.polytech.isa.common.entities.notifications;

import java.util.Objects;

/**
 * Email.
 * <p>
 * Represents an email.
 * </p>
 */
public class Email {
    /**
     * The email address of the sender.
     */
    private String from;

    /**
     * The email address of the recipient.
     */
    private String to;

    /**
     * The subject of the email.
     */
    private String subject;

    /**
     * The body of the email.
     */
    private String body;

    /**
     * Default constructor.
     */
    public Email() {}

    /**
     * Constructs an email.
     *
     * @param from The email address of the sender.
     * @param to The email address of the recipient.
     * @param subject The subject of the email.
     * @param body The body of the email.
     */
    public Email(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Returns the email address of the sender.
     *
     * @return The email address of the sender.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the email address of the sender.
     *
     * @param from The new email address of the sender.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns the email address of the recipient.
     *
     * @return The email address of the recipient.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the email address of the recipient.
     *
     * @param to The new email address of the recipient.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the subject of the email.
     *
     * @return The subject of the email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email.
     *
     * @param subject The new subject of the email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the body of the email.
     *
     * @return The body of the email.
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the email.
     *
     * @param body The new body of the email.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Indicates whether another object is equal to this email.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this email is the same as the object argument; <code>false</code> otherwise.
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
        Email email = (Email) object;
        return (
            Objects.equals(getFrom(), email.getFrom()) &&
            Objects.equals(getTo(), email.getTo()) &&
            Objects.equals(getSubject(), email.getSubject()) &&
            Objects.equals(getBody(), email.getBody())
        );
    }

    /**
     * Returns a hash code value for the email.
     *
     * @return A hash code value for the email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getFrom(),
            getTo(),
            getSubject(),
            getBody()
        );
    }

    /**
     * Returns a string representation of the email.
     *
     * @return A string representation of the email.
     */
    @Override
    public String toString() {
        return "Email{" +
            ", from='" + from + '\'' +
            ", to='" + to + '\'' +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
