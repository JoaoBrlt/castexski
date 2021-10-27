package fr.unice.polytech.isa.common.entities.notifications;

import javax.persistence.Entity;
import java.util.Objects;
import java.util.Optional;

/**
 * Contact details.
 * <p>
 * Represents the contact details of a person.
 * </p>
 */
@Entity
public class Contact {
    /**
     * The email address of the contact.
     */
    private String email;

    /**
     * The phone number of the contact.
     */
    private String phoneNumber;

    /**
     * The instagram account of the contact.
     */
    private String instagramAccount;

    /**
     * Constructs a contact.
     *
     * @param email The email address of the contact.
     * @param phoneNumber The phone number of the contact.
     * @param instagramAccount The instagram account of the contact.
     */
    public Contact(String email, String phoneNumber, String instagramAccount) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.instagramAccount = instagramAccount;
    }

    /**
     * Constructs a contact.
     *
     * @param email The email address of the contact.
     * @param phoneNumber The phone number of the contact.
     */
    public Contact(String email, String phoneNumber) {
        this(email, phoneNumber, null);
    }

    /**
     * Constructs a contact.
     *
     * @param email The email address of the contact.
     */
    public Contact(String email) {
        this(email, null, null);
    }

    /**
     * Default constructor.
     */
    public Contact() {
        this(null, null, null);
    }

    /**
     * Returns the email address of the contact.
     *
     * @return The email address of the contact (if any).
     */
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    /**
     * Sets the email address of the contact.
     *
     * @param email The new email address of the contact.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the phone number of the contact.
     *
     * @return The phone number of the contact (if any).
     */
    public Optional<String> getPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    /**
     * Sets the phone number of the contact.
     *
     * @param phoneNumber The new phone number of the contact.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the instagram account of the contact.
     *
     * @return The phone number of the contact (if any).
     */
    public Optional<String> getInstagramAccount() {
        return Optional.ofNullable(instagramAccount);
    }

    /**
     * Sets the Instagram account of the contact.
     *
     * @param instagramAccount The new Instagram account of the contact.
     */
    public void setInstagramAccount(String instagramAccount) {
        this.instagramAccount = instagramAccount;
    }

    /**
     * Indicates whether another object is equal to this contact.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this contact is the same as the object argument; <code>false</code> otherwise.
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

        Contact contact = (Contact) object;
        return (
            // Same email address.
            Objects.equals(getEmail(), contact.getEmail()) &&

            // Same phone number.
            Objects.equals(getPhoneNumber(), contact.getPhoneNumber()) &&

            // Same Instagram account.
            Objects.equals(getInstagramAccount(), contact.getInstagramAccount())
        );
    }

    /**
     * Returns a hash code value for the contact.
     *
     * @return A hash code value for the contact.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getEmail(),
            getPhoneNumber(),
            getInstagramAccount()
        );
    }

    /**
     * Returns a string representation of the contact.
     *
     * @return A string representation of the contact.
     */
    @Override
    public String toString() {
        return "Contact{" +
            "email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", instagramAccount='" + instagramAccount + '\'' +
            '}';
    }
}
