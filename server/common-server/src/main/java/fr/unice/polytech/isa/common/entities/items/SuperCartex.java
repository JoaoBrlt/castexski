package fr.unice.polytech.isa.common.entities.items;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = "super_cartex")
public class SuperCartex extends Card implements Serializable {
    /**
     * The first swipe, which is reset when a swipe occur at least one day after the lastSwipe
     */
    @Transient
    LocalDateTime firstSwipe;

    /**
     * The raw type of firstSwipe, used to store it with JPA
     */
    String firstSwipeRaw;

    /**
     * The date of the last swipe on a lift
     */
    @Transient
    LocalDateTime lastSwipe;

    /**
     * The raw type of lastSwipe, used to store it with JPA
     */
    String lastSwipeRaw;

    /**
     * The date until which the super cartex is valid
     */
    @Transient
    LocalDateTime membershipExpiry;

    /**
     * The raw type of membershipExpiry, used to store it with JPA
     */
    @NotNull
    String membershipExpiryRaw;

    /**
     * The associated customer
     */
    @NotNull
    @ManyToOne
    Customer customer;

    /**
     * Decodes the localDateTime types from the persistence unit with the raw values.
     */
    @PostLoad
    public void init() {
        this.membershipExpiry = LocalDateTime.parse(membershipExpiryRaw);
        this.firstSwipe = this.firstSwipeRaw == null ? null : LocalDateTime.parse(firstSwipeRaw);
        this.lastSwipe = this.lastSwipeRaw == null ? null : LocalDateTime.parse(lastSwipeRaw);
    }

    /**
     * Default constructor.
     */
    public SuperCartex() {}

    /**
     * Constructs a super cartex (method for the cashier).
     *
     * @param customer The customer affiliated to the super cartex
     * @param type The type of the card.
     * @param pass The associated pass.
     * @param physicalId The physical id of the card.
     * @param membershipExpiry The date until which the super cartex is valid.
     */
    public SuperCartex(Customer customer, ItemType type, Pass pass, String physicalId, LocalDateTime membershipExpiry) {
        super(type, pass, physicalId);
        this.customer = customer;
        this.membershipExpiry = membershipExpiry;
        this.membershipExpiryRaw = membershipExpiry.toString();
    }

    /**
     * Constructs a super cartex without a pass.
     * @param customer The customer affiliated to the super cartex
     * @param type The type of the card.
     * @param membershipExpiry The date until which the super cartex is valid.
     */
    public SuperCartex(Customer customer, ItemType type, LocalDateTime membershipExpiry) {
        this(customer, type, null, null, membershipExpiry);
    }

    /**
     * Constructs a super cartex without a pass and with a physical id.
     *
     * @param customer The customer affiliated to the super cartex
     * @param type The type of the card.
     * @param physicalId The physical id of the card.
     * @param membershipExpiry The date until which the super cartex is valid.
     */

    public SuperCartex(Customer customer, ItemType type, String physicalId, LocalDateTime membershipExpiry) {
        this(customer, type, null, physicalId, membershipExpiry);
    }


    /**
     * Sets the customer associated to the super cartex.
     *
     * @param customer The new customer associated to the super cartex.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the customer associated to the super cartex.
     *
     * @return The customer associated to the super cartex.
     */
    public Customer getCustomer() {
        return customer;
    }


    /**
     * Sets the first swipe date of the super cartex.
     *
     * @param firstSwipe The new first swipe date of the super cartex.
     */

    public void setFirstSwipe(LocalDateTime firstSwipe) {
        this.firstSwipe = firstSwipe;
        this.firstSwipeRaw = firstSwipe.toString();
    }


    /**
     * Returns the date of the first swipe.
     *
     * @return The first swipe date.
     */
    public LocalDateTime getFirstSwipe() {
        return firstSwipe;
    }

    /**
     * Sets the first swipe date by its raw format.
     *
     * @param firstSwipeRaw The new first swipe date of the super cartex.
     */

    public void setFirstSwipeRaw(String firstSwipeRaw) {
        this.firstSwipeRaw = firstSwipeRaw;
    }


    /**
     * Returns the raw format of the first swipe.
     *
     * @return Raw format of the swipe date.
     */
    public String getFirstSwipeRaw() {
        return firstSwipeRaw;
    }
    /**
     * Sets the last swipe date of the super cartex.
     *
     * @param lastSwipe The new last swipe date of the super cartex.
     */

    public void setLastSwipe(LocalDateTime lastSwipe) {
        this.lastSwipe = lastSwipe;
        this.lastSwipeRaw = lastSwipe.toString();
    }


    /**
     * Returns the date of the last swipe.
     *
     * @return The last swipe date.
     */
    public LocalDateTime getLastSwipe() {
        return lastSwipe;
    }

    /**
     * Sets the last swipe date by its raw format.
     *
     * @param lastSwipeRaw The new first last swipe date of the super cartex.
     */

    public void setLastSwipeRaw(String lastSwipeRaw) {
        this.lastSwipeRaw = lastSwipeRaw;
    }


    /**
     * Returns the raw format of the last swipe date.
     *
     * @return Raw format of the last swipe date.
     */
    public String getLastSwipeRaw() {
        return lastSwipeRaw;
    }

    /**
     * Sets the membership date of the super cartex.
     *
     * @param membershipExpiry The new membership date of the super cartex.
     */

    public void setMembershipExpiry(LocalDateTime membershipExpiry) {
        this.membershipExpiry = membershipExpiry;
    }


    /**
     * Returns the date of the membership expiry.
     *
     * @return The membership expiry date.
     */
    public LocalDateTime getMembershipExpiry() {
        return membershipExpiry;
    }

    /**
     * Sets the membership expiry date by its raw format.
     *
     * @param membershipExpiryRaw The new raw membership expiry date of the super cartex.
     */

    public void setMembershipExpiryRaw(String membershipExpiryRaw) {
        this.membershipExpiryRaw = membershipExpiryRaw;
    }


    /**
     * Returns the raw format of the membership expiry date.
     *
     * @return Raw format of the membership expiry date.
     */
    public String getMembershipExpiryRaw() {
        return membershipExpiryRaw;
    }

    /**
     * Indicates whether the SuperCartex has expired.
     *
     * @return <code>true</code> if the SuperCartex has expired.
     */
    public boolean hasExpired() {
        return membershipExpiry.isBefore(LocalDateTime.now());
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof SuperCartex)) return false;
//        if (!super.equals(o)) return false;
//        SuperCartex that = (SuperCartex) o;
//        return Objects.equals(getFirstSwipeRaw(), that.getFirstSwipeRaw()) &&
//            Objects.equals(getLastSwipeRaw(), that.getLastSwipeRaw()) &&
//            getMembershipExpiryRaw().equals(that.getMembershipExpiryRaw()) &&
//            getCustomer().equals(that.getCustomer());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), getFirstSwipeRaw(), getLastSwipeRaw(), getMembershipExpiryRaw(), getCustomer());
//    }
}
