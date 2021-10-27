package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.ejb.Local;

@Local
public interface CustomerCardLinker {

    void addCardToCustomer(String email, Card card) throws CustomerNotFoundException;
    void linkPassToCardOnline(String email, String physicalCardId, String passId) throws CustomerNotFoundException, PassNotFoundException, CardNotFoundException;
}
