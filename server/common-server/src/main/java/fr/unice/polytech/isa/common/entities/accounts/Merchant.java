package fr.unice.polytech.isa.common.entities.accounts;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Merchant.
 * <p>
 * Represents a merchant subscribed to some resort statistics.
 * </p>
 */
@Entity
@DiscriminatorValue(value = "merchant")
public class Merchant extends User implements Serializable {
    /**
     * The business of the merchant.
     */
    private String business;

    /**
     * The resorts the merchant is subscribed to.
     */
    @ElementCollection
    private List<String> resorts = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Merchant() {}

    /**
     * Constructs a merchant.
     *
     * @param firstName The first name of the merchant.
     * @param lastName The last name of the merchant.
     * @param email The email of the merchant.
     * @param business The business of the merchant.
     * @param resorts The resorts the merchant is subscribed to.
     */
    public Merchant(String firstName, String lastName, String email, String business, List<String> resorts) {
        super(firstName, lastName, email);
        this.business = business;
        this.resorts = resorts;
    }

    /**
     * Constructs a merchant.
     *
     * @param firstName The first name of the merchant.
     * @param lastName The last name of the merchant.
     * @param email The email of the merchant.
     * @param business The business of the merchant.
     */
    public Merchant(String firstName, String lastName, String email, String business) {
        this(firstName, lastName, email, business, new ArrayList<>());
    }

    /**
     * Returns the user type of the merchant.
     *
     * @return The user type of the merchant.
     */
    @Override
    public UserType getType() {
        return UserType.MERCHANT;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public List<String> getResorts() {
        return resorts;
    }

    public void setResorts(List<String> resorts) {
        this.resorts = resorts;
    }

    public void addResort(String resort) {
        this.resorts.add(resort);
    }

    public void removeResort(String resort) {
        this.resorts.remove(resort);
    }

    /**
     * Indicates whether another object is equal to this merchant.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this merchant is the same as the object argument; <code>false</code> otherwise.
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

        // Not same according to super class.
        if (!super.equals(object)) {
            return false;
        }

        Merchant merchant = (Merchant) object;
        return (
            // Same business.
            Objects.equals(getBusiness(), merchant.getBusiness())
        );
    }

    /**
     * Returns a hash code value for the merchant.
     *
     * @return A hash code value for the merchant.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            getBusiness()
        );
    }

    /**
     * Returns a string representation of the merchant.
     *
     * @return A string representation of the merchant.
     */
    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", business='" + getBusiness() + '\'' +
            '}';
    }
}
