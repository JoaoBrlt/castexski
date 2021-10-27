package fr.unice.polytech.isa.common.entities.shopping;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Order.
 * <p>
 * Represents a customer order.
 * </p>
 */
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    /**
     * The order identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The associated customer.
     */
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    /**
     * The items of the order.
     */
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();

    /**
     * The creation date of the order.
     */
    @NotNull
    private LocalDateTime creationDate;

    /**
     * The delivery date of the order.
     */
    @NotNull
    private LocalDateTime deliveryDate;

    /**
     * Whether the order has been delivered.
     */
    @NotNull
    private boolean delivered;

    /**
     * Default constructor.
     */
    public Order() {}

    /**
     * Constructs an order.
     *
     * @param customer The associated customer.
     * @param items The items of the order.
     * @param creationDate The creation date of the order.
     * @param deliveryDate The delivery date of the order.
     * @param delivered Whether the order has been delivered.
     */
    public Order(
        Customer customer,
        Set<Item> items,
        LocalDateTime creationDate,
        LocalDateTime deliveryDate,
        boolean delivered
    ) {
        this.customer = customer;
        this.items = items;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.delivered = delivered;
    }

    /**
     * Returns the order identifier.
     *
     * @return The order identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the order identifier.
     *
     * @param id The new order identifier.
     */
    public void setId(int id) {
        this.id = id;
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
     * Sets the associated customer.
     *
     * @param customer The new associated customer.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the items of the order.
     *
     * @return The items of the order.
     */
    public Set<Item> getItems() {
        return items;
    }

    /**
     * Sets the items of the order.
     *
     * @param items The new items of the order.
     */
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    /**
     * Returns the total price of the order.
     *
     * @return The total price of the order.
     */
    public double getPrice() {
        return items
            .stream()
            .mapToDouble(Item::getTotalPrice)
            .sum();
    }

    /**
     * Returns the creation date of the order.
     *
     * @return The creation date of the order.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the order.
     *
     * @param creationDate The new creation date of the order.
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the delivery date of the order.
     *
     * @return The delivery date of the order.
     */
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the delivery date of the order.
     *
     * @param deliveryDate The new delivery date of the order.
     */
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Indicates whether the order has been delivered.
     *
     * @return <code>true</code> if the order has been delivered; <code>false</code> otherwise.
     */
    public boolean isDelivered() {
        return delivered;
    }

    /**
     * Sets whether the order has been delivered.
     *
     * @param delivered Whether the order has been delivered.
     */
    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    /**
     * Indicates whether another object is equal to this order.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this order is the same as the object argument; <code>false</code> otherwise.
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

        Order order = (Order) object;
        return (
            // Same customer.
            Objects.equals(getCustomer(), order.getCustomer()) &&

            // Same items.
            Objects.equals(getItems(), order.getItems()) &&

            // Same creation date.
            Objects.equals(getCreationDate(), order.getCreationDate()) &&

            // Same delivery date.
            Objects.equals(getDeliveryDate(), order.getDeliveryDate()) &&

            // Same status.
            isDelivered() == order.isDelivered()
        );
    }

    /**
     * Returns a hash code value for the order.
     *
     * @return A hash code value for the order.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getCustomer(),
            getItems(),
            getCreationDate(),
            getDeliveryDate(),
            isDelivered()
        );
    }

    /**
     * Returns a string representation of the order.
     *
     * @return A string representation of the order.
     */
    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", customer=" + customer +
            ", items=" + items +
            ", creationDate=" + creationDate +
            ", deliveryDate=" + deliveryDate +
            ", delivered=" + delivered +
            '}';
    }
}
