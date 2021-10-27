package fr.unice.polytech.isa.shopping.integration;

import arquillian.AbstractShoppingTest;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.shopping.interfaces.*;
import fr.unice.polytech.isa.common.exceptions.EmptyCartException;
import fr.unice.polytech.isa.common.exceptions.NoCreditCardException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
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
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CartIntegrationTest extends AbstractShoppingTest {

    @EJB private CartModifier modifier;
    @EJB private CartProcessor processor;
    @EJB private CreditCardRegistration creditCardRegistration;
    @EJB private CatalogModifier catalogModifier;
    @EJB private CatalogExplorer catalogExplorer;
    @EJB private SuperCartexProcessor superCartexProcessor;
    @EJB private CardRegistration cardRegistration;
    @EJB private CardFinder cardFinder;
    @PersistenceContext private EntityManager entityManager;
    @Inject
    private UserTransaction utx;
    private Customer marcel_account;
    private Item ONE_SUPER_CASTEX;
    private Item ONE_NORMAL_CASTEX;
    private Item ONE_DISCOVERY_PASS;
    private SuperCartex superCartex;

    @Before
    public void setUpContext() throws ItemAlreadyExistException, NullQuantityException, UnknownCatalogEntryException {
        marcel_account = new Customer(MARCEL_FIRSTNAME, MARCEL_LASTNAME, MARCEL_EMAIL);
        entityManager.persist(marcel_account);
        initCatalog();
        ONE_SUPER_CASTEX = catalogExplorer.pickCard(SUPER_CARTEX, SUPERCARTEX_CARD, QTY_1);
        ONE_NORMAL_CASTEX = catalogExplorer.pickCard(CARTEX, NORMAL_CARD, QTY_1);
        ONE_DISCOVERY_PASS = catalogExplorer.pickPass(DISCOVERY_PASS, DAYS_3, ADULT_PASS, QTY_1);
    }

    public void initCatalog() throws ItemAlreadyExistException {
        catalogModifier.addCard(SUPER_CARTEX, SUPERCARTEX_CARD, PRICE_10, PUBLIC_ITEM);
        catalogModifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_10, DAYS_1, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_25, PRICE_20, DAYS_5, PUBLIC_ITEM);
        catalogModifier.addPass(SUPER_ORIGINAL_PASS, PRICE_10, PRICE_10, DAYS_1, PRIVATE_ITEM);
        catalogModifier.addPass(SUPER_FREE_HOUR_PASS, PRICE_0, PRICE_0, HOURS_1, PRIVATE_ITEM);
    }

    @Test
    public void validateCart() throws Exception {
        marcel_account = entityManager.merge(marcel_account);
        assertTrue(marcel_account.getOrders().isEmpty());
        modifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        modifier.addToCart(marcel_account, ONE_DISCOVERY_PASS);
        creditCardRegistration.creditCardRegistry(marcel_account, MARCEL_LASTNAME, CREDIT_CARD_NO, CVV, EXPIRY_DATE, SAVE);
        processor.validateCart(marcel_account);
        assertFalse(marcel_account.getOrders().isEmpty());
        assertTrue(marcel_account.getCards().stream().anyMatch(c -> c.getType().equals(ONE_NORMAL_CASTEX.getType())));
        assertTrue(marcel_account.getPasses().stream().anyMatch(p -> p.getType().equals(ONE_DISCOVERY_PASS.getType())));
    }

    @Test(expected = NoCreditCardException.class)
    public void validateCartCardException() throws Exception {
        assertTrue(marcel_account.getOrders().isEmpty());
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        modifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        processor.validateCart(marcel_account);
        assertFalse(marcel_account.getOrders().isEmpty());
    }

    @Test(expected = EmptyCartException.class)
    public void validateCartException() throws Exception {
        assertTrue(processor.getCustomerCart(marcel_account).getItems().isEmpty());
        processor.validateCart(marcel_account);
    }

    @Test
    public void processSuperCartex() throws CustomerNotFoundException, PaymentException, PassNotFoundException, NoCreditCardException, CardNotFoundException, EmptyCartException, UnknownCatalogEntryException, NullQuantityException {
        cardRegistration.addCard(MARCEL_EMAIL, new ItemType(SUPER_CARTEX, PRICE_10, ItemTypeName.SUPERCARTEX));
        superCartex = (SuperCartex) cardFinder.findSuperCartexCards().get(0);
        assertTrue(superCartexProcessor.processSuperCartex(superCartex)); //first hour
        assertEquals(SUPER_FREE_HOUR_PASS, superCartex.getPass().getType().getName());
        superCartex.setFirstSwipe(DATE_NOW);
        superCartex.setLastSwipe(DATE_NOW);
        creditCardRegistration.creditCardRegistry(marcel_account, MARCEL_LASTNAME, CREDIT_CARD_NO, CVV, EXPIRY_DATE, SAVE);
        assertTrue(superCartexProcessor.processSuperCartex(superCartex)); //pay pass
        assertEquals(SUPER_ORIGINAL_PASS, superCartex.getPass().getType().getName());
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
        for (ItemCatalog c : catalogExplorer.displayPrivateCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        marcel_account = null;
        utx.commit();
    }
}
