package fr.unice.polytech.isa.common.entities.items;

import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Pass.
 * <p>
 *      Represents a custom pass.
 * </p>
 */
@Entity
public class Pass {
    /**
     * The pass identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The associated card.
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Card card;

    /**
     * The type of the pass.
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PassType type;

    /**
     * The date from which the pass is usable.
     */
    private Date startDate;

    /**
     * The date from which the pass becomes unusable.
     */
    private Date endDate;

    /**
     * Whether the pass is active.
     */
    private boolean activated;

    /**
     * Default constructor.
     */
    public Pass() {}

    /**
     * Constructs a pass.
     *
     * @param startDate The date from which the pass is usable.
     * @param endDate The date from which the pass becomes unusable.
     * @param activated Whether the pass is active.
     * @param type The pass type.
     */
    public Pass(Date startDate, Date endDate, boolean activated, PassType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.activated = activated;
        this.type = type;
    }

    /**
     * Constructs a pass without dates.

     * @param type The pass type.
     */
    public Pass(PassType type) {
        this(null, null, false, type);
    }

    /**
     * Returns the pass identifier.
     *
     * @return The pass identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the pass identifier.
     *
     * @param id The new pass identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the type of the pass.
     *
     * @return The type of the pass.
     */
    public PassType getType() {
        return type;
    }

    /**
     * Sets the type of the pass.
     *
     * @param type The new type of the pass.
     */
    public void setType(PassType type) {
        this.type = type;
    }

    /**
     * Returns the start date.
     *
     * @return The date from which the pass is usable.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate The new date from which the pass is usable.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date.
     *
     * @return The date from which the pass becomes unusable.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate The new date from which the pass becomes unusable.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the linked card.
     *
     * @return The card linked to this pass.
     */
    public Card getCard() {
        return card;
    }


    /**
     * Sets the linked card.
     *
     * @param card The new linked card to this pass.
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Indicates whether the pass is active.
     *
     * @return <code>true</code> if the pass is active; <code>false</code> otherwise.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the activation state of the pass.
     *
     * @param activated Whether the pass is active.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Indicates whether another object is equal to this pass.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this pass is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Pass pass = (Pass) object;
        return (
            // Same type.
            Objects.equals(getType(), pass.getType()) &&

            // Same start date.
            Objects.equals(getStartDate(), pass.getStartDate()) &&

            // Same end date.
            Objects.equals(getEndDate(), pass.getEndDate()) &&

            // Same status.
            isActivated() == pass.isActivated()
        );
    }

    /**
     * Returns the hash code value for the pass
     * @return The hash code value for the pass
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getType(),
            getStartDate(),
            getEndDate(),
            isActivated()
        );
    }

    /**
     * Returns a string representation of the pass
     * @return a string representation of the pass
     */
    @Override
    public String toString() {
        return "Pass{" +
            "id='" + id + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", activated=" + activated +
            ", type=" + type +
            '}';
    }
}
