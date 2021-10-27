package fr.unice.polytech.isa.common.entities.items;

import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * Card
 * <p>
 * Represents a customer card.
 * </p>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TypeOfCard")
public class Card implements Serializable {
    /**
     * The card identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The type of the card.
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ItemType type;

    /**
     * The associated pass.
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Pass pass;

    /**
     * The card's physical number.
     */
    @Pattern(regexp = "^\\d{16}$")
    private String physicalId;

    /**
     * Default constructor.
     */
    public Card() {}

    /**
     * Constructs a card (method for the cashier).
     *
     * @param type The type of the card.
     * @param pass The associated pass.
     * @param physicalId The physical id of the card.
     */
    public Card(ItemType type, Pass pass, String physicalId) {
        this.type = type;
        this.pass = pass;
        this.physicalId = physicalId;
    }

    /**
     * Constructs a card without a pass.
     *
     * @param type The type of the card.
     */
    public Card(ItemType type) {
        this(type, null, null);
    }

    /**
     * Constructs a card without a pass and with a physical id.
     *
     * @param type The type of the card.
     * @param physicalId The physical id of the card.
     */

    public Card(ItemType type, String physicalId) {
        this(type, null, physicalId);
    }

    /**
     * Returns the card identifier.
     *
     * @return The card identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the card identifier.
     *
     * @param id The new card identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the type of the card.
     *
     * @return The type of the card.
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Sets the type of the card.
     *
     * @param type The new type of the card.
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Returns the associated pass.
     *
     * @return The associated pass.
     */
    public Pass getPass() {
        return pass;
    }

    /**
     * Returns the physical id.
     *
     * @return The physical id.
     */
    public String getPhysicalId() {
        return physicalId;
    }

    /**
     * Sets the card's physical identifier.
     *
     * @param physicalId The new physical card identifier.
     */
    public void setPhysicalId(String physicalId) {
        this.physicalId = physicalId;
    }

    /**
     * Sets the associated pass.
     *
     * @param pass The new associated pass.
     */

    public void setPass(Pass pass) {
        this.pass = pass;
    }

    /**
     * Indicates whether another object is equal to this card.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this card is the same as the object argument; <code>false</code> otherwise.
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

        Card card = (Card) object;
        return (
            // Same identifier.
            Objects.equals(getPhysicalId(), card.getPhysicalId()) &&

            // Same type.
            getType() == card.getType() &&

            // Same pass.
            Objects.equals(getPass(), card.getPass())
        );
    }

    /**
     * Returns a hash code value for the card.
     *
     * @return A hash code value for the card.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getPhysicalId(),
            getType(),
            getPass()
        );
    }

    /**
     * Returns a string representation of the card.
     *
     * @return A string representation of the card.
     */
    @Override
    public String toString() {
        return "Card{" +
            "id=" + id +
            ", type=" + type +
            ", pass=" + pass +
            '}';
    }
}
