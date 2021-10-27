package fr.unice.polytech.isa.common.entities.shopping.catalog;

import fr.unice.polytech.isa.common.entities.items.ItemTypeName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Type of pass.
 * <p>
 * Represents the type of a customer pass.
 * </p>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TypeOfItemType")
public class ItemType implements Serializable {
    /**
     * The pass type identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The name of the item type.
     */
    @NotNull
    private String name;

    /**
     * The price of the item type.
     */
    @NotNull
    private double price;

    /**
     * The type of the item type.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemTypeName type;

    /**
     * Default constructor.
     */
    public ItemType() {}

    /**
     * Constructs a item type.
     *
     * @param name The name of the item type.
     * @param price The price of the item type.
     * @param type The type of the item
     */
    public ItemType(String name, double price, ItemTypeName type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    /**
     * Returns the item type identifier.
     *
     * @return The item type identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the item type identifier.
     *
     * @param id The new identifier of the item type.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the item type.
     *
     * @return The name of the item type.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the pass type.
     *
     * @param name The new name of the pass type.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the item type.
     *
     * @return The type of the item type.
     */

    public ItemTypeName getType() {
        return type;
    }

    /**
     * Sets the type of the pass type.
     *
     * @param type The new type of the pass type.
     */
    public void setType(ItemTypeName type) {
        this.type = type;
    }

    /**
     * Returns the price of the item type.
     *
     * @return The price of the item type.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the pass type.
     *
     * @param price The new price of the pass type.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Indicates whether another object is equal to this pass type.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this pass type is the same as the object argument; <code>false</code> otherwise.
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

        ItemType itemType = (ItemType) object;
        return (
            // Same name.
            Objects.equals(getName(), itemType.getName()) &&

            // Same type.
            getType() == itemType.getType() &&

            // Same price.
            Double.compare(itemType.getPrice(), getPrice()) == 0
        );
    }

    /**
     * Returns a hash code value for the pass type.
     *
     * @return A hash code value for the pass type.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getName(),
            getType(),
            getPrice()
        );
    }

    /**
     * Returns a string representation of the pass type.
     *
     * @return A string representation of the pass type.
     */
    @Override
    public String toString() {
        return "ItemType{" +
            "name='" + name + '\'' +
            ", type=" + type.name() +
            ", price=" + price +
            '}';
    }
}
