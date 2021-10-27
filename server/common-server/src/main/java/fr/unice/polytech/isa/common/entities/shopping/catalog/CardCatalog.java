package fr.unice.polytech.isa.common.entities.shopping.catalog;

import fr.unice.polytech.isa.common.entities.items.ItemTypeName;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Card catalog.
 */
@Entity
@DiscriminatorValue(value = "card")
public class CardCatalog extends ItemCatalog implements Serializable {
    /**
     * The maximum duration of use of the pass.
     */
    @NotNull
    private boolean isSuperCartex;

    /**
     * Default constructor.
     */
    public CardCatalog() {}

    /**
     * Constructs a card.
     *
     * @param name The name of the card.
     * @param price The price of the card.
     * @param isSuperCartex The maximum duration of use of the pass.
     */
    public CardCatalog(String name, double price, boolean isSuperCartex, boolean isPrivateItem) {
        super(name, price, price, isSuperCartex ? ItemTypeName.SUPERCARTEX : ItemTypeName.CARD, isPrivateItem);
        this.isSuperCartex = isSuperCartex;
    }

    /**
     * Returns the maximum duration of use of the pass.
     *
     * @return The maximum duration of use of the pass.
     */
    public boolean isSuperCartex() {
        return isSuperCartex;
    }

    /**
     * Sets the maximum duration of use of the pass.
     *
     * @param superCartex The maximum duration of use of the pass.
     */
    public void setSuperCartex(boolean superCartex) {
        isSuperCartex = superCartex;
    }
}
