package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.items.Card;

import javax.ejb.Local;
import java.util.List;

@Local
public interface CardFinder {

    Card findCardByPhysicalId(String id) throws CardNotFoundException;
    List<Card> findSuperCartexCards();
    List<Card> findNormalCards();
    List<Card> findCustomerCards(String email) throws CustomerNotFoundException;
    Card findCardById(String cardId) throws CardNotFoundException;
    List<Card> findCardsNotPhysicallyLinked(String email) throws CustomerNotFoundException;
    List<Card> findCardsPhysicallyLinked(String email) throws CustomerNotFoundException;
}
