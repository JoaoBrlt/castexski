package fr.unice.polytech.isa.accounts.business;

import arquillian.AbstractAccountTest;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.Card;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CardRegistryBeanTest extends AbstractAccountTest {
    @EJB private CustomerRegistration customerRegistration;
    @EJB private CardFinder finder;
    @EJB private CardRegistration registration;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Card card;
    private Card superCard;
    private ItemType itemType;
    private ItemType superItemType;
    private Customer customer;
    private String MARCEL_EMAIL = "marcel@eat.fr";

    @Before
    public void setUpContext() throws UnavailableEmailException {
       // LocalDate date1 = LocalDate.of(2021, 5, 1);
        //LocalDate date2 = date1.plusDays(5);
        //Date startDate = new Date(date1.toEpochDay());
        //Date endDate = new Date(date2.toEpochDay());
        //Duration duration = Duration.ZERO;
        customerRegistration.register("Marcel", "Eat", MARCEL_EMAIL);
        itemType = new ItemType("Ski en Folie", 10.0, ItemTypeName.CARD);
        superItemType = new ItemType("Ski en Folie", 10.0, ItemTypeName.SUPERCARTEX);
        //PassType passType = new PassType("Ski en Folie", false, duration, 10.0);
        //Pass pass = new Pass(startDate, endDate, true, passType);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        Card c = entityManager.merge(card);
        Card c2 = entityManager.merge(superCard);
        entityManager.remove(c);
        entityManager.remove(c2);
        customerRegistration.deleteCustomer(MARCEL_EMAIL);
        utx.commit();
    }

    @Test
    public void firstTestGet() throws CustomerNotFoundException {
        registration.addCard(MARCEL_EMAIL, itemType);
        List<Card> cardList = finder.findSuperCartexCards();
        assertTrue(cardList.isEmpty());
        cardList = finder.findNormalCards();
        assertEquals(cardList.size(),1);
        card = cardList.get(0);
        assertEquals(ItemTypeName.CARD, card.getType().getType());
    }

    @Test
    public void editCard() throws CustomerNotFoundException {
        registration.addCard(MARCEL_EMAIL, itemType);
        List<Card> cardList = finder.findSuperCartexCards();
        assertTrue(cardList.isEmpty());
        cardList = finder.findNormalCards();
        assertEquals(cardList.size(),1);
        card = cardList.get(0);
        card.getType().setType(ItemTypeName.SUPERCARTEX);
        cardList = finder.findSuperCartexCards();
        assertEquals(cardList.size(),1);
        cardList = finder.findNormalCards();
        assertTrue(cardList.isEmpty());
    }

    @Test
    public void updatePhysicalId() throws CustomerNotFoundException {
        registration.addCard(MARCEL_EMAIL, itemType);
        card = finder.findNormalCards().get(0);
        String id = "0123456789101111";
        card.setPhysicalId("0123456789101121");
        registration.updatePhysicalId(card,id);
        assertEquals(id,card.getPhysicalId());
    }

    @Test
    public void findCardByPhysicalId() throws CardNotFoundException, CustomerNotFoundException {
        registration.addCard(MARCEL_EMAIL, itemType);
        registration.addCard(MARCEL_EMAIL, superItemType);
        card = finder.findNormalCards().get(0);
        superCard = finder.findSuperCartexCards().get(0);
        String id1 = "0123456789101111";
        String id2 = "0123456789101121";
        registration.updatePhysicalId(card,id1);
        registration.updatePhysicalId(superCard,id2);
        Card test = finder.findCardByPhysicalId(id1);
        assertEquals(test.getPhysicalId(),id1);
        assertEquals(test.getType().getType(),ItemTypeName.CARD);
    }

    @Test(expected = CardNotFoundException.class)
    public void cantFindCardById() throws CardNotFoundException, CustomerNotFoundException {
        registration.addCard(MARCEL_EMAIL, itemType);
        card = finder.findNormalCards().get(0);
        String id1 = "0123456789101111";
        String id2 = "0123456789101121";
        registration.updatePhysicalId(card,id1);
        Card test = finder.findCardByPhysicalId(id1);
        assertEquals(test.getPhysicalId(),id1);
        assertEquals(test.getType().getType(),ItemTypeName.CARD);
        finder.findCardByPhysicalId(id2);
        exceptionRule.expect(CardNotFoundException.class);
    }
}
