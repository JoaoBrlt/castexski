package fr.unice.polytech.isa.common.entities.accounts;

import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.entities.notifications.Contact;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.entities.shopping.CreditCard;
import fr.unice.polytech.isa.common.entities.shopping.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Customer.
 * <p>
 * Stores the information about a customer.
 * </p>
 */
@Entity
@DiscriminatorValue(value = "customer")
public class Customer extends User implements Serializable {
    /**
     * The phone number of the customer.
     */
    private String phoneNumber;

    /**
     * The instagram account of the customer.
     */
    private String instagramAccount;

    /**
     * The credit card of the customer.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CreditCard creditCard;

    /**
     * The current cart of the customer.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
    private Cart cart;

    /**
     * The orders of the customer.
     */
    @NotNull
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    /**
     * The cards of the customer.
     */
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards = new ArrayList<>();

    /**
     * The super cartex of the customer.
     */
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SuperCartex> superCartexs = new ArrayList<>();

    /**
     * The valid passes of the customer.
     */
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pass> passes = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Customer() {}

    /**
     * Constructs a customer.
     *
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @param email The email of the customer.
     */
    public Customer(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        this.cards = new ArrayList<>();
        this.passes = new ArrayList<>();
        this.orders = new HashSet<>();
        this.superCartexs = new ArrayList<>();
    }

    /**
     * Returns the phone number of the customer.
     *
     * @return The phone number of the customer.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the user type of the customer.
     *
     * @return The user type of the customer.
     */
    @Override
    public UserType getType() {
        return UserType.CUSTOMER;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phoneNumber The new phone number of the customer.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the Instagram account of the customer.
     *
     * @return The Instagram account of the customer.
     */
    public String getInstagramAccount() {
        return instagramAccount;
    }

    /**
     * Sets the Instagram account of the customer.
     *
     * @param instagramAccount The new Instagram account of the customer.
     */
    public void setInstagramAccount(String instagramAccount) {
        this.instagramAccount = instagramAccount;
    }

    /**
     * Returns the contact details of the customer.
     *
     * @return The contact details of the customer.
     */
    @Override
    public Contact getContact() {
        return new Contact(
            getEmail(),
            getPhoneNumber(),
            getInstagramAccount()
        );
    }

    /**
     * Sets the current cart of the customer.
     *
     * @param cart The new cart of the customer.
     */
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * Returns the current cart of the customer.
     *
     * @return The current cart of the customer.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Sets the credit card of the customer.
     *
     * @param creditCard The new credit card of the customer.
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Returns the credit card of the customer.
     *
     * @return The credit card of the customer.
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Indicates whether the customer has a credit card.
     *
     * @return <code>true</code> if the customer has a credit card; <code>false</code> otherwise.
     */
    public boolean hasCreditCard() {
        return creditCard != null;
    }

    /**
     * Returns the orders of the customer.
     *
     * @return The orders of the customer.
     */
    public Set<Order> getOrders() {
        return orders;
    }

    /**
     * Adds an order to the customer.
     *
     * @param order The order to be added.
     */
    public void addOrder(Order order) {
        this.orders.add(order);
    }


    /**
     * Adds a list of orders to the customer.
     *
     * @param orders The new list of orders.
     */
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    /**
     * Removes an order from the customer.
     *
     * @param order The order to be removed.
     */
    public void removeOrder(Order order) {
        this.orders.remove(order);
    }

    /**
     * Returns the cards of the customer.
     *
     * @return The cards of the customer.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Adds a list of cards to the customer.
     *
     * @param cards The cards to be added.
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    /**
     * Adds a card to the customer.
     *
     * @param card The card to be added.
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Removes a card from the customer.
     *
     * @param card The card to be removed.
     */
    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    /**
     * Returns the super cartex list owned by the customer.
     *
     * @return The list of super cartex owned by the customer.
     */
    public List<SuperCartex> getSuperCartexs() {
        return superCartexs;
    }
    /**
     * Adds a list of super cartex to the customer.
     *
     * @param superCartexs The new list of super cartex to be added.
     */
    public void setSuperCartexs(List<SuperCartex> superCartexs) {
        this.superCartexs = superCartexs;
    }
    /**
     * Adds a super cartex to the customer account.
     *
     * @param superCartex The supercartex to be added.
     */
    public void addSuperCartex(SuperCartex superCartex) {
        this.superCartexs.add(superCartex);
    }

    /**
     * Removes a super cartex from the customer.
     *
     * @param superCartex The card to be removed.
     */
    public void removeSuperCartex(SuperCartex superCartex) {
        this.superCartexs.remove(superCartex);
    }

    /**
     * Returns the passes of the customer.
     *
     * @return The passes of the customer.
     */
    public List<Pass> getPasses() {
        return passes;
    }

    /**
     * Adds a list of passes to the customer.
     *
     * @param passes The passes to be added.
     */
    public void setPasses(List<Pass> passes) {
        this.passes = passes;
    }

    public void addPass(Pass pass) {
        this.passes.add(pass);
    }

    public void removePass(Pass pass) {
        this.passes.remove(pass);
    }

    /**
     * Indicates whether another object is equal to this customer.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this customer is the same as the object argument; <code>false</code> otherwise.
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

        Customer customer = (Customer) object;
        return (
            // Same phone number.
            Objects.equals(getPhoneNumber(), customer.getPhoneNumber()) &&

            // Same Instagram account.
            Objects.equals(getInstagramAccount(), customer.getInstagramAccount()) &&

            // Same credit card.
            Objects.equals(getCreditCard(), customer.getCreditCard())
        );
    }

    /**
     * Returns a hash code value for the customer.
     *
     * @return A hash code value for the customer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            getPhoneNumber(),
            getInstagramAccount(),
            getCreditCard()
        );
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return A string representation of the customer.
     */
    @Override
    public String toString() {
        return "Customer{" +
            ", firstName='" + getFirstName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", instagramAccount='" + instagramAccount + '\'' +
            ", creditCard=" + creditCard +
            '}';
    }
}
