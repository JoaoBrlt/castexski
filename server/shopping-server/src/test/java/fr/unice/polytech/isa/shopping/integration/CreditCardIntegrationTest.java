package fr.unice.polytech.isa.shopping.integration;

import arquillian.AbstractShoppingTest;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.shopping.interfaces.*;
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

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.SAVE;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CreditCardIntegrationTest extends AbstractShoppingTest {

    @EJB
    private CartModifier cartModifier;
    @EJB private CartProcessor cartProcessor;
    @EJB private CustomerFinder customerFinder;
    @EJB private CreditCardRegistration creditCardRegistration;
    @EJB private CatalogModifier catalogModifier;
    @EJB private CatalogExplorer catalogExplorer;

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;
    private Customer marcel_account;
    private Item ONE_NORMAL_CASTEX;
    private Item ONE_DISCOVERY_PASS;

    @Before
    public void setUpContext() throws ItemAlreadyExistException, NullQuantityException, UnknownCatalogEntryException {
        marcel_account = new Customer(MARCEL_FIRSTNAME, MARCEL_LASTNAME, MARCEL_EMAIL);
        entityManager.persist(marcel_account);
        initCatalog();
        ONE_NORMAL_CASTEX = catalogExplorer.pickCard(CARTEX, NORMAL_CARD, QTY_1);
        ONE_DISCOVERY_PASS = catalogExplorer.pickPass(DISCOVERY_PASS, DAYS_3, ADULT_PASS, QTY_1);
        cartModifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        cartModifier.addToCart(marcel_account, ONE_DISCOVERY_PASS);

    }

    public void initCatalog() throws ItemAlreadyExistException {
        catalogModifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
    }

    @Test
    public void payAndSaveCreditCard() throws CustomerNotFoundException, PaymentException, EmptyCartException, NoCreditCardException {
        assertFalse(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
        creditCardRegistration.creditCardRegistry(marcel_account, MARCEL_LASTNAME, CREDIT_CARD_NO, CVV, EXPIRY_DATE, SAVE);
        assertTrue(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
        cartProcessor.validateCart(marcel_account);
        //The customer still has a saved credit card
        assertTrue(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
    }

    @Test
    public void payWithoutSavingCreditCard() throws CustomerNotFoundException, PaymentException, EmptyCartException, NoCreditCardException {
        assertFalse(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
        creditCardRegistration.creditCardRegistry(marcel_account, MARCEL_LASTNAME, CREDIT_CARD_NO, CVV, EXPIRY_DATE, DONT_SAVE);
        assertTrue(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
        cartProcessor.validateCart(marcel_account);
        //The customer credit card has been used and deleted.
        assertFalse(customerFinder.findByMail(MARCEL_EMAIL).hasCreditCard());
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        marcel_account = entityManager.merge(marcel_account);
        entityManager.remove(marcel_account);
        for (ItemCatalog c : catalogExplorer.displayCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        marcel_account = null;
        utx.commit();
    }
}
