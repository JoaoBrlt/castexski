package fr.unice.polytech.isa.accounts.business;

import arquillian.AbstractAccountTest;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
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
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CustomerLinkerBeanTest extends AbstractAccountTest {

    @EJB private CustomerRegistration registry;
    @EJB private CustomerFinder finder;
    @EJB private CustomerCardLinker cardLinker;
    @EJB private PassRegistration passRegistration;
    @EJB private CustomerPassFinder passFinder;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private final String firstName = "Marcel";
    private final String lastName = "Eats";
    private final String marcelMail = "marcel@mail.mail";
    private final String PHYSICAL_CARD_ID = "0123456789101121";
    private Customer marcel;
    private Card card;
    private Pass pass;
    private PassType passType;

    @Before
    public void setUpContext(){
        LocalDate date1 = LocalDate.of(2021,5,1);
        LocalDate date2 = date1.plusDays(5);
        Date startDate = new Date(date1.toEpochDay());
        Date endDate = new Date(date2.toEpochDay());
        Duration duration = Duration.ZERO;
        passType = new PassType("Ski en Folie", false, duration, 10.0);
        pass = new Pass(startDate,endDate,true,passType);
        marcel = new Customer(firstName, lastName, marcelMail);
        card = new Card(new ItemType("Oh ouais", 0.0, ItemTypeName.CARD));
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        List<Customer> toDispose = finder.findByFirstName(marcel.getFirstName());
        for(Customer cust : toDispose){
            Customer ct = entityManager.merge(cust);
            entityManager.remove(ct);
        }
        utx.commit();
    }

    @Test
    public void addCardToRegisteredCustomer() throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(firstName,lastName,marcelMail);
        Customer retrieved = finder.findByMail(marcelMail);
        assertTrue(retrieved.getCards().isEmpty());
        cardLinker.addCardToCustomer(marcelMail,card);
        assertEquals(1, retrieved.getCards().size());
    }

    @Test(expected = CustomerNotFoundException.class)
    public void addCardToUnregisteredCustomer() throws CustomerNotFoundException {
        finder.findByMail(marcelMail);
        exceptionRule.expect(CustomerNotFoundException.class);
        cardLinker.addCardToCustomer(marcelMail,card);
        exceptionRule.expect(CustomerNotFoundException.class);
    }

    @Test
    public void linkPassToCardOnline() throws CustomerNotFoundException, CardNotFoundException, PassNotFoundException, UnavailableEmailException {
        registry.register(firstName,lastName,marcelMail);
        passRegistration.registerPass(marcelMail, passType);
        card.setPhysicalId(PHYSICAL_CARD_ID);
        cardLinker.addCardToCustomer(marcelMail, card);
        assertTrue(passFinder.findLinkedPass(marcelMail).isEmpty());
        cardLinker.linkPassToCardOnline(marcelMail, PHYSICAL_CARD_ID, passFinder.findNotLinkedPass(marcelMail).get(0).getId());
        assertFalse(passFinder.findLinkedPass(marcelMail).isEmpty());
    }
}
