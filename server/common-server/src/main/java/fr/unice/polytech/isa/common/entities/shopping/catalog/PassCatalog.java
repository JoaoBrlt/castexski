package fr.unice.polytech.isa.common.entities.shopping.catalog;

import fr.unice.polytech.isa.common.entities.items.ItemTypeName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;

/**
 * Pass catalog.
 */
@Entity
@DiscriminatorValue(value = "pass")
public class PassCatalog extends ItemCatalog implements Serializable {
    /**
     * The maximum duration of use of the pass.
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
    public PassCatalog() {}

    /**
     * Constructs a pass.
     *
     * @param name The name of the pass.
     * @param price The price of the pass.
     * @param childrenPrice The price of the pass for the children.
     * @param duration The maximum duration of use of the pass.
     */
    public PassCatalog(String name, double price, double childrenPrice, Duration duration, boolean isPrivateItem) {
        super(name, price, childrenPrice, ItemTypeName.PASS, isPrivateItem);
        this.maxDuration = duration;
        this.maxDurationRaw = duration.toString();
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
     * Returns the maximum duration of use of the pass.
     *
     * @return The maximum duration of use of the pass.
     */
    public Duration getMaxDuration() {
        return maxDuration;
    }

    /**
     * Sets the maximum duration of use of the pass.
     *
     * @param maxDuration The new maximum duration of use of the pass.
     */
    public void setMaxDuration(Duration maxDuration) {
        this.maxDuration = maxDuration;
    }
}
