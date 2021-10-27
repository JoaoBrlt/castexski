package fr.unice.polytech.isa.common.entities.shopping.catalog;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Item.
 * <p>
 * Represents an item of the order.
 * </p>
 */
@Entity
public class Item implements Serializable {
    /**
     * The item identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The type of the item.
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private ItemType type;


    /**
     * The quantity of the item.
     */
    @NotNull
    private int quantity;

    /**
     * Default constructor.
     */
    public Item() {}

    /**
     * Constructs an item.
     *
     * @param type The type of the item.
     */
    public Item(ItemType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Represents the item identifier.
     *
     * @return The item identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier of the item.
     *
     * @param id The new identifier of the item.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the type of the item.
     *
     * @param type The new type of the item.
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Returns the type of the item.
     *
     * @return The type of the item.
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Returns the price of the item.
     *
     * @return The price of the item.
     */
    public double getTotalPrice() {
        return type.getPrice() * quantity;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param quantity The new quantity of the item.
     */

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the quantity of the item.
     *
     * @return The quantity of the item.
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * Indicates whether another object is equal to this item.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this item is the same as the object argument; <code>false</code> otherwise.
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

        Item item = (Item) object;
        return (
            // Same type.
            getType().equals(item.getType()) &&

            // Same quantity
            getQuantity() == item.getQuantity()
        );
    }

    /**
     * Returns a hash code value for the item.
     *
     * @return A hash code value for the item.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getType(), getQuantity());
    }

    /**
     * Returns a string representation of the item.
     *
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", type=" + type +
            '}';
    }

}
