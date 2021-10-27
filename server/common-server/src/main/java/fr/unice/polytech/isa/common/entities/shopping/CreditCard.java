package fr.unice.polytech.isa.common.entities.shopping;

import fr.unice.polytech.isa.common.entities.accounts.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Credit card.
 * <p>
 * Represents a customer credit card.
 * </p>
 */
@Entity
public class CreditCard implements Serializable {
    /**
     * The credit card identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The affiliate customer.
     */
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;


    /**
     * The credit card owner.
     */
    @NotNull
    private String name;

    /**
     * The credit card number.
     */
    @NotNull
    @Pattern(regexp = "^\\d{16}$")
    private String number;

    /**
     * The credit card security code.
     */
    @NotNull
    @Pattern(regexp = "^\\d{3}$")
    private String securityCode;

    /**
     * The credit card expiry date.
     */
    @NotNull
    private YearMonth expiryDate;

    /**
     * Whether to save the credit card for future purchases.
     */
    @NotNull
    private boolean saveChoice;

    /**
     * Default constructor.
     */
    public CreditCard() {}

    /**
     * Constructs a credit card.
     *
     * @param name The credit card owner.
     * @param number The credit card number.
     * @param securityCode The credit card security code.
     * @param expiryDate The credit card expiry date.
     * @param saveChoice  Whether to save the credit card for future purchases.
     */
    public CreditCard(Customer customer, String name, String number, String securityCode, YearMonth expiryDate, boolean saveChoice) {
        this.customer = customer;
        this.name = name;
        this.number = number;
        this.securityCode = securityCode;
        this.expiryDate = expiryDate;
        this.saveChoice = saveChoice;
    }

    /**
     * Returns the credit card identifier.
     *
     * @return The credit card identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the credit card identifier.
     *
     * @param id The new credit card identifier.
     */
    public int setId(int id) {
        return this.id = id;
    }

    /**
     * Returns the associated customer.
     *
     * @return The associated customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the associated customer.
     *
     * @param customer The new associated customer.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the credit card owner.
     *
     * @return The credit card owner.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the credit card owner.
     *
     * @param name The new credit card owner.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the credit card number.
     *
     * @return The credit card number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the credit card number.
     *
     * @param number The new credit card number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Returns the credit card security code.
     *
     * @return The credit card security code.
     */
    public String getSecurityCode() {
        return securityCode;
    }

    /**
     * Sets the credit card security code.
     *
     * @param securityCode The new credit card security code.
     */
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    /**
     * Returns the credit card expiry date.
     *
     * @return The credit card expiry date.
     */
    public YearMonth getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the credit card expiry date.
     *
     * @param expiryDate The new credit card expiry date.
     */
    public void setExpiryDate(YearMonth expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the formatted credit card expiry date.
     *
     * @return The formatted credit card expiry date.
     */
    public String getFormattedExpiryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        return expiryDate.format(formatter);
    }

    /**
     * Indicates whether to save the credit card for future purchases.
     *
     * @return <code>true</code> if the credit card can be saved; <code>false</code> otherwise.
     */
    public boolean isSaveChoice() {
        return saveChoice;
    }

    /**
     * Sets the customer choice to save the card for future purchases.
     *
     * @param saveChoice Whether to save the credit card for future purchases.
     */
    public void setSaveChoice(boolean saveChoice) {
        this.saveChoice = saveChoice;
    }

    /**
     * Indicates whether another object is equal to this credit card.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this credit card is the same as the object argument; <code>false</code> otherwise.
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

        CreditCard creditCard = (CreditCard) object;
        return (
            // Same card number.
            getNumber().equals(creditCard.getNumber()) &&

            // Same security code.
            getSecurityCode().equals(creditCard.getSecurityCode()) &&

            // Same expiry date.
            Objects.equals(getExpiryDate(), creditCard.getExpiryDate())
        );
    }

    /**
     * Returns the hash code value for the credit card.
     *
     * @return The hash code value for the credit card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getNumber(),
            getSecurityCode(),
            getExpiryDate()
        );
    }

    /**
     * Returns a string representation of the credit card.
     *
     * @return A string representation of the credit card.
     */
    @Override
    public String toString() {
        return "CreditCard{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", number=" + number +
            ", securityCode=" + securityCode +
            ", expiryDate=" + expiryDate +
            '}';
    }
}
