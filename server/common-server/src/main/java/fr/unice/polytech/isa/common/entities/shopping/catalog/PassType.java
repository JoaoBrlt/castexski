package fr.unice.polytech.isa.common.entities.shopping.catalog;

import fr.unice.polytech.isa.common.entities.items.ItemTypeName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * Pass type.
 * <p>
 * Represents the type of a customer pass.
 * </p>
 */
@Entity
@DiscriminatorValue(value = "pass_item_type")
public class PassType extends ItemType implements Serializable {
    /**
     * Whether the pass type is for children.
     */
    @NotNull
    private boolean isChildPass;

    /**
     * The maximum duration of use of the pass type.
     */
    @Transient
    private Duration maxDuration;

    /**
     * The raw maximum duration of use of the pass.
     */
    @NotNull
    private String maxDurationRaw;

    /**
     * Default constructor.
     */
    public PassType() {}

    /**
     * Constructs a pass type.
     *
     * @param name The name of the pass type.
     * @param isChildPass Whether the pass type is for children.
     * @param maxDuration The maximum duration of use of the pass type.
     * @param price The price of the pass type.
     */
    public PassType(String name, boolean isChildPass, Duration maxDuration, double price) {
        super(name, price, ItemTypeName.PASS);
        this.isChildPass = isChildPass;
        this.maxDuration = maxDuration;
        this.maxDurationRaw = maxDuration.toString();
    }

    /**
     * Decodes the maximum duration from the persistence unit.
     */
    @PostLoad
    public void init() {
        this.maxDuration = this.maxDurationRaw == null ? null : Duration.parse(this.maxDurationRaw);
    };

    /**
     * Sets the raw maximum duration of use of the pass.
     *
     * @param duration The maximum duration of use of the pass.
     */
    public void setMaxDurationRaw(String duration) {
        this.maxDurationRaw = duration;
    }

    /**
     * Returns the raw maximum duration of use of the pass.
     *
     * @return The raw maximum duration of use of the pass.
     */
    public String getMaxDurationRaw() {
        return maxDurationRaw;
    }

    /**
     * Indicates whether the pass type is for children.
     *
     * @return <code>true</code> if the pass type if for children; <code>false</code> otherwise.
     */
    public boolean isChildPass() {
        return isChildPass;
    }

    /**
     * Sets whether the pass type is for children.
     *
     * @param childPass Whether the pass type is for children.
     */
    public void setChildPass(boolean childPass) {
        isChildPass = childPass;
    }

    /**
     * Returns the maximum duration of use of the pass type.
     *
     * @return The maximum duration of use of the pass type.
     */
    public Duration getMaxDuration() {
        return maxDuration;
    }

    /**
     * Sets the maximum duration of use of the pass type.
     *
     * @param maxDuration The new maximum duration of use of the pass type.
     */
    public void setMaxDuration(Duration maxDuration) {
        this.maxDuration = maxDuration;
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
        // Not equal for parent.
        if (!super.equals(object)) {
            return false;
        }

        PassType passType = (PassType) object;
        return (
            // Same child status.
            isChildPass() == passType.isChildPass() &&

            // Same price.
            Double.compare(passType.getPrice(), getPrice()) == 0 &&

            // Same duration.
            Objects.equals(getMaxDurationRaw(), passType.getMaxDurationRaw())
        );
    }

    /**
     * Returns a hash code value for the pass type.
     *
     * @return A hash code value for the pass type.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isChildPass(), getMaxDurationRaw());
    }

    /**
     * Returns a string representation of the pass type.
     *
     * @return A string representation of the pass type.
     */
    @Override
    public String toString() {
        return "PassType{" +
            " isChildPass=" + isChildPass +
            ", maxDuration=" + maxDurationRaw +
            '}';
    }
}
