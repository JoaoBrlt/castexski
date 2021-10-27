package fr.unice.polytech.isa.common.entities.shopping.catalog;

import fr.unice.polytech.isa.common.entities.items.ItemTypeName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Item catalog.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ItemCatalogType")
public class ItemCatalog implements Serializable {
    /**
     * The item identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The name of the item.
     */
    @NotNull
    private String name;

    /**
     * The price of the item.
     */
    @NotNull
    private double price;

    /**
     * The price of the item for the children.
     */
    @NotNull
    private double childrenPrice;

    @NotNull
    private ItemTypeName itemTypeName;

    /**
     * The status of an item in the catalog. If true, the item can't be displayed for sales.
     */
    @NotNull
    private boolean isPrivateItem;

    /**
     * Default constructor.
     */
    ItemCatalog() {}

    /**
     * Constructs an item.
     *
     * @param name The name of the item.
     * @param price The price of the item.
     * @param childrenPrice The price of the item for the children.
     */
    public ItemCatalog(String name, double price, double childrenPrice, ItemTypeName itemTypeName, boolean isPrivateItem) {
        this.name = name;
        this.price = price;
        this.childrenPrice = childrenPrice;
        this.itemTypeName = itemTypeName;
        this.isPrivateItem = isPrivateItem;
    }

    /**
     * Returns the item identifier.
     *
     * @return The item identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the item identifier.
     *
     * @param id The new identifier of the item.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The new name of the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the item.
     *
     * @return The price of the item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the item.
     *
     * @param price The new price of the item.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the price of the item for the children.
     *
     * @return The price of the item for the children.
     */
    public double getChildrenPrice() {
        return childrenPrice;
    }

    /**
     * Sets the price of the item for the children.
     *
     * @param price The new price for the children of the item.
     */
    public void setChildrenPrice(double price) {
        this.childrenPrice = price;
    }

    public ItemTypeName getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(ItemTypeName itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    /**
     * Sets the status private or public in the catalog.
     *
     * @param privateItem The new status for the item.
     */
    public void setPrivateItem(boolean privateItem) {
        isPrivateItem = privateItem;
    }

    /**
     * Returns the status private or public of the item for the catalog.
     *
     * @return The status of the item for the catalog.
     */
    public boolean isPrivateItem() {
        return isPrivateItem;
    }

    /**
     * Indicates whether another object is equal to this item.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this pass type is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ItemCatalog that = (ItemCatalog) object;
        return (
            // Same name.
            Objects.equals(getName(), that.getName()) &&

            // Same price.
            Double.compare(that.getPrice(), getPrice()) == 0 &&
            Double.compare(that.getChildrenPrice(), getChildrenPrice()) == 0 &&

            // Same privacy
                that.isPrivateItem() == isPrivateItem
        );
    }

    /**
     * Returns a hash code value for the item.
     *
     * @return A hash code value for the item.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getName(),
            getPrice(),
            getChildrenPrice(),
            isPrivateItem()
        );
    }

    /**
     * Returns a string representation of the item.
     *
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return "ItemCatalog{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", childrenPrice=" + childrenPrice +
            '}';
    }
}
