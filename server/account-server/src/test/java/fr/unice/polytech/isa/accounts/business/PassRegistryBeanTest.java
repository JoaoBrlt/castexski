package fr.unice.polytech.isa.accounts.business;

import arquillian.AbstractAccountTest;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Optional;

import static fr.unice.polytech.isa.accounts.AccountTestUtil.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class PassRegistryBeanTest extends AbstractAccountTest {
    @EJB
    private CustomerRegistration registry;
    @EJB private CustomerFinder finder;
    @EJB private CustomerCardLinker cardLinker;
    @EJB private PassRegistration passRegistration;
    @EJB private CustomerPassFinder passFinder;

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    @Before
    public void setUp() throws Exception {
        registry.register(MARCEL_FIRSTNAME, MARCEL_LASTNAME, MARCEL_EMAIL);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        registry.deleteCustomer(MARCEL_EMAIL);
        utx.commit();
    }

    @Test
    public void registerPass() throws CustomerNotFoundException {
        assertTrue(passFinder.findNotLinkedPass(MARCEL_EMAIL).isEmpty());
        assertTrue(passFinder.findLinkedPass(MARCEL_EMAIL).isEmpty());
        passRegistration.registerPass(MARCEL_EMAIL, DISCOVERY_CHILD_DAYS_3_PRICE_10);
        assertFalse(passFinder.findNotLinkedPass(MARCEL_EMAIL).isEmpty());
        assertTrue(passFinder.findLinkedPass(MARCEL_EMAIL).isEmpty());
        assertEquals(DISCOVERY_CHILD_DAYS_3_PRICE_10, passFinder.findNotLinkedPass(MARCEL_EMAIL).get(0).getType());
    }

    @Test
    public void findPassById() throws CustomerNotFoundException {
        passRegistration.registerPass(MARCEL_EMAIL, DISCOVERY_CHILD_DAYS_3_PRICE_10);
        Pass pass = passFinder.findNotLinkedPass(MARCEL_EMAIL).get(0);
        Optional<Pass> expected = passFinder.findPassById(MARCEL_EMAIL, pass.getId());
        assert expected.isPresent();
        assertEquals(pass, expected.get());
    }

    @Test
    public void findCustomerPasses() throws CustomerNotFoundException {
        assertTrue(passFinder.findCustomerPasses(MARCEL_EMAIL).isEmpty());
        passRegistration.registerPass(MARCEL_EMAIL, DISCOVERY_CHILD_DAYS_3_PRICE_10);
        assertEquals(DISCOVERY_CHILD_DAYS_3_PRICE_10, passFinder.findCustomerPasses(MARCEL_EMAIL).get(0).getType());
    }

    @Test
    public void findNotLinkedPass() throws CustomerNotFoundException {
        assertTrue(passFinder.findNotLinkedPass(MARCEL_EMAIL).isEmpty());
        passRegistration.registerPass(MARCEL_EMAIL, DISCOVERY_CHILD_DAYS_3_PRICE_10);
        assertEquals(DISCOVERY_CHILD_DAYS_3_PRICE_10, passFinder.findNotLinkedPass(MARCEL_EMAIL).get(0).getType());
    }

    @Test
    public void findLinkedPass() throws CustomerNotFoundException, CardNotFoundException, PassNotFoundException {
        assertTrue(passFinder.findLinkedPass(MARCEL_EMAIL).isEmpty());
        cardLinker.addCardToCustomer(MARCEL_EMAIL, CARD_CARTEX);
        passRegistration.registerPass(MARCEL_EMAIL, DISCOVERY_CHILD_DAYS_3_PRICE_10);
        Pass pass = passFinder.findCustomerPasses(MARCEL_EMAIL).get(0);
        assertTrue(passFinder.findLinkedPass(MARCEL_EMAIL).isEmpty());
        cardLinker.linkPassToCardOnline(MARCEL_EMAIL, PHYSICAL_ID1, pass.getId());
        assertEquals(DISCOVERY_CHILD_DAYS_3_PRICE_10, pass.getType());
    }
}
