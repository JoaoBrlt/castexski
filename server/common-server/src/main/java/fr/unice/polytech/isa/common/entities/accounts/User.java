package fr.unice.polytech.isa.common.entities.accounts;

import fr.unice.polytech.isa.common.entities.notifications.Contact;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "type_id",
    discriminatorType = DiscriminatorType.STRING
)
public abstract class User implements Serializable {
    /**
     * The customer identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The first name of the user.
     */
    @NotNull
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotNull
    private String lastName;

    /**
     * The email of the user.
     */
    @NotNull
    @Pattern(regexp="^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")
    private String email;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructs a user.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email of the user.
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName= lastName;
        this.email = email;
    }

    /**
     * Returns the user identifier.
     *
     * @return The user identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user identifier.
     *
     * @param id The new user identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user type.
     *
     * @return The user type.
     */
    public abstract UserType getType();

    /**
     * Returns the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the entire name of the user.
     *
     * @return The entire name of the user.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Returns the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the contact details of the user.
     *
     * @return The contact details of the user.
     */
    public Contact getContact() {
        return new Contact(getEmail());
    }

    /**
     * Indicates whether another object is equal to this user.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this user is the same as the object argument; <code>false</code> otherwise.
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

        User user = (User) object;
        return (
            // Same first name.
            Objects.equals(getFirstName(), user.getFirstName()) &&

            // Same last name.
            Objects.equals(getLastName(), user.getLastName()) &&

            // Same email.
            Objects.equals(getEmail(), user.getEmail())
        );
    }

    /**
     * Returns a hash code value for the user.
     *
     * @return A hash code value for the user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getFirstName(),
            getLastName(),
            getEmail()
        );
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
