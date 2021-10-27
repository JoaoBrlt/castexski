package fr.unice.polytech.isa.accounts.components;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class CustomerLinkerBean implements CustomerCardLinker {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB private CustomerFinder finder;
    @EJB private CardFinder cardFinder;
    @EJB private CustomerPassFinder passFinder;

    @PersistenceContext private EntityManager manager;


    @Override
    public void addCardToCustomer(String email, Card card) throws CustomerNotFoundException {
        readCustomer(email).addCard(card);
    }

    @Override
    public void linkPassToCardOnline(String email, String physicalCardId, String passId) throws CustomerNotFoundException, PassNotFoundException, CardNotFoundException {
        Optional<Pass> pass = passFinder.findPassById(email, passId);
        if (pass.isPresent()){
            Pass p = manager.merge(pass.get());
            Card card = manager.merge(cardFinder.findCardByPhysicalId(physicalCardId));
            card.setPass(p);
            p.setCard(card);
        } else {
            throw new PassNotFoundException(email, passId);
        }
    }

    Customer readCustomer(String email) throws CustomerNotFoundException {
        return manager.merge(finder.findByMail(email));
    }
}
