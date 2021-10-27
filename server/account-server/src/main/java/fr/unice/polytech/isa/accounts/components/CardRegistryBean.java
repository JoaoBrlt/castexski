package fr.unice.polytech.isa.accounts.components;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class CardRegistryBean implements CardRegistration, CardFinder {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB private CustomerFinder customerFinder;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void addCard(String email, ItemType type) throws CustomerNotFoundException {
       addCard(email, type, null);
    }

    @Override
    public void addCard(String email, ItemType type, String physicalId) throws CustomerNotFoundException {
        Customer customer = customerFinder.findByMail(email);
        Card c = type.getType().equals(ItemTypeName.SUPERCARTEX) ?
            new SuperCartex(customer, type, physicalId, LocalDateTime.now()):
            new Card(type);
        c.setPhysicalId(physicalId);
        manager.persist(c);
        c = manager.merge(c);
        customer = manager.merge(customer);
        customer.addCard(c);
    }

    @Override
    public void deleteCard(String physicalId) throws CardNotFoundException {
        Card card = findCardByPhysicalId(physicalId);
        manager.remove(card);
    }

    @Override
    public void updatePhysicalId(Card card, String physicalId) {
        Card c = manager.merge(card);
        c.setPhysicalId(physicalId);
    }

    @Override
    public Card findCardByPhysicalId(String id) throws CardNotFoundException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Card> criteria = builder.createQuery(Card.class);
        Root<Card> root = criteria.from(Card.class);
        criteria.select(root).where(builder.equal(root.get("physicalId"), id));
        TypedQuery<Card> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            log.log(Level.FINEST, "No result for [" + id + "]", nre);
            throw new CardNotFoundException(id);
        }
    }

    @Override
    public Card findCardById(String id) throws CardNotFoundException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Card> criteria = builder.createQuery(Card.class);
        Root<Card> root = criteria.from(Card.class);
        criteria.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<Card> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            log.log(Level.FINEST, "No result for [" + id + "]", nre);
            throw new CardNotFoundException(""+id);
        }
    }

    @Override
    public List<Card> findCardsNotPhysicallyLinked(String email) throws CustomerNotFoundException {
        return findCustomerCards(email).stream().filter(c-> Objects.isNull(c.getPhysicalId())).collect(Collectors.toList());
    }

    @Override
    public List<Card> findCardsPhysicallyLinked(String email) throws CustomerNotFoundException {
        return findCustomerCards(email).stream().filter(c-> Objects.nonNull(c.getPhysicalId())).collect(Collectors.toList());
    }

    @Override
    public List<Card> findSuperCartexCards() {
        String itemTypeName = ItemTypeName.SUPERCARTEX.name();
        return findCardByType(itemTypeName);
    }

    private List<Card> findCardByType(String itemTypeName){
        List<Card> cards = getAllCards();
        List<Card> resultingCards = new ArrayList<>();
        for(Card card : cards){
            String type = card.getType().getType().name();
            if(type.equals(itemTypeName)){
                resultingCards.add(card);
            }
        }
        return resultingCards;
    }

    private List<Card> getAllCards(){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Card> criteria = builder.createQuery(Card.class);
        Root<Card> root =  criteria.from(Card.class);
        criteria.select(root);
        TypedQuery<Card> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<Card> findNormalCards() {
        String itemTypeName = ItemTypeName.CARD.name();
        return findCardByType(itemTypeName);
    }

    @Override
    public List<Card> findCustomerCards(String email) throws CustomerNotFoundException {
        Customer c = customerFinder.findByMail(email);
        return c.getCards();
    }

}
