package fr.unice.polytech.isa.common.entities.shopping;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Cart.
 * <p>
 * Represents a customer cart.
 * </p>
 */
@Entity
public class Cart implements Serializable {
    /**
     * The cart identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The associated customer.
     */
    @OneToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    /**
     * The items of the cart.
     */
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();

    /**
     * The creation date of the cart.
     */
    @NotNull
    private LocalDateTime creationDate;

    /**
     * The last update of the cart.
     */
    @NotNull
    private LocalDateTime lastUpdate;

    /**
     * Default constructor.
     */
    public Cart() {}

    /**
     * Constructs a cart.
     *
     * @param customer The associated customer.
     */
    public Cart(Customer customer) {
        this.customer = customer;
        this.creationDate = LocalDateTime.now();
        updateLastChange();
    }

    /**
     * Returns the cart identifier.
     *
     * @return The cart identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the cart identifier.
     *
     * @param id The new cart identifier.
     */
    public void setId(int id) {
        this.id = id;
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
     * Returns the associated customer.
     *
     * @return The associated customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the items of the cart.
     *
     * @return The items of the cart.
     */
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Sets the items of the cart.
     *
     * @param items The new items of the cart.
     */
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Returns the number of items in the cart.
     *
     * @return The number of items in the cart.
     */
    public int numberOfItems(){
        return items.size();
    }

    /**
     * Adds an item to the cart.
     *
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        items.add(item);
        updateLastChange();
    }

    /**
     * Deletes an item to the cart.
     *
     * @param item The item to be deleted.
     */
    public void deleteItem(Item item) {
        items.remove(item);
        updateLastChange();
    }

    /**
     * Empties the cart.
     */
    public void clearCart() {
        items.clear();
        updateLastChange();
    }

    /**
     * Returns the total price of the items.
     *
     * @return The total price of the items.
     */
    public double getAmount() {
        return items
            .stream()
            .mapToDouble(Item::getTotalPrice)
            .sum();
    }

    /**
     * Sets the creation date of the cart.
     *
     * @param creationDate The new creation date of the cart.
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the creation date of the cart.
     *
     * @return The creation date of the cart.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the last update of the cart.
     *
     * @param lastUpdate The new last update of the cart.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Returns the last update of the cart.
     *
     * @return The last update of the cart.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Updates the last change of the cart.
     */
    public void updateLastChange() {
        lastUpdate = LocalDateTime.now();
    }

    /**
     * Indicates whether another object is equal to this cart.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this cart is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Cart cart = (Cart) object;
        return (
            // Same customer.
            Objects.equals(getCustomer(), cart.getCustomer()) &&

            // Same items.
            Objects.equals(getItems(), cart.getItems()) &&

            // Same creation date.
            Objects.equals(getCreationDate(), cart.getCreationDate()) &&

            // Same last update.
            Objects.equals(getLastUpdate(), cart.getLastUpdate())
        );
    }

    /**
     * Returns a hash code value for the cart.
     *
     * @return A hash code value for the cart.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getCustomer(),
            getItems(),
            getCreationDate(),
            getLastUpdate()
        );
    }

    /**
     * Returns a string representation of the cart.
     *
     * @return A string representation of the cart.
     */
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + id +
            ", customer=" + customer +
            ", items=" + items +
            ", creationDate=" + creationDate +
            ", lastUpdate=" + lastUpdate +
            '}';
    }
}
