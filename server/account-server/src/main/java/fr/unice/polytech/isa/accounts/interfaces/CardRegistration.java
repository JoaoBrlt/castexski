package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;

import javax.ejb.Local;

@Local
public interface CardRegistration {

    void addCard(String email, ItemType type) throws CustomerNotFoundException;
    void addCard(String email, ItemType type, String physicalId) throws CustomerNotFoundException;
    void deleteCard(String physicalId) throws CardNotFoundException;
    void updatePhysicalId(Card card, String physicalId);

}
