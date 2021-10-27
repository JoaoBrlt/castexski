package fr.unice.polytech.isa.shopping.business;

import arquillian.AbstractShoppingTest;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.shopping.interfaces.*;
import fr.unice.polytech.isa.common.exceptions.ItemNotFoundException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
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

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CartTest extends AbstractShoppingTest {

    @EJB private CartModifier modifier;
    @EJB private CartProcessor processor;

    @EJB private CatalogModifier catalogModifier;
    @EJB private CatalogExplorer catalogExplorer;

    @PersistenceContext private EntityManager entityManager;
    @Inject
    private UserTransaction utx;
    private Customer marcel_account;
    private Item ONE_SUPER_CASTEX;
    private Item TWO_SUPER_CASTEX;
    private Item ONE_NORMAL_CASTEX;
    private Item ONE_DISCOVERY_PASS;

    @Before
    public void setUpContext() throws ItemAlreadyExistException, NullQuantityException, UnknownCatalogEntryException {
        marcel_account = new Customer(MARCEL_FIRSTNAME, MARCEL_LASTNAME, MARCEL_EMAIL);
        entityManager.persist(marcel_account);
        initCatalog();
        ONE_SUPER_CASTEX = catalogExplorer.pickCard(SUPER_CARTEX, SUPERCARTEX_CARD, QTY_1);
        TWO_SUPER_CASTEX = catalogExplorer.pickCard(SUPER_CARTEX, SUPERCARTEX_CARD, QTY_2);
        ONE_NORMAL_CASTEX = catalogExplorer.pickCard(CARTEX, NORMAL_CARD, QTY_1);
        ONE_DISCOVERY_PASS = catalogExplorer.pickPass(DISCOVERY_PASS, DAYS_3, ADULT_PASS, QTY_1);
    }

    public void initCatalog() throws ItemAlreadyExistException {
        catalogModifier.addCard(SUPER_CARTEX, SUPERCARTEX_CARD, PRICE_10, PUBLIC_ITEM);
        catalogModifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_10, DAYS_1, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_25, PRICE_20, DAYS_5, PUBLIC_ITEM);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void add() throws NullQuantityException {
        Cart marcel_cart = processor.getCustomerCart(marcel_account);
        assertEquals(QTY_0, marcel_cart.numberOfItems());
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        Cart cart_found = processor.getCustomerCart(marcel_account);
        assertEquals(QTY_1, cart_found.numberOfItems());
        assertTrue(cart_found.getItems().contains(ONE_SUPER_CASTEX));
        //Add another SUPER_CASTEX
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        assertFalse(processor.displayContents(marcel_account).contains(ONE_SUPER_CASTEX));
        assertTrue(processor.displayContents(marcel_account).contains(TWO_SUPER_CASTEX));
        modifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        assertEquals(QTY_2, cart_found.numberOfItems());
        assertTrue(processor.displayContents(marcel_account).contains(TWO_SUPER_CASTEX));
        assertTrue(processor.displayContents(marcel_account).contains(ONE_NORMAL_CASTEX));
    }

    @Test
    public void delete() throws NullQuantityException, ItemNotFoundException {
        Cart marcel_cart = processor.getCustomerCart(marcel_account);
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        modifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        assertEquals(QTY_2, marcel_cart.numberOfItems());
        modifier.removeFromCart(marcel_account, ONE_SUPER_CASTEX);
        assertEquals(QTY_1, marcel_cart.numberOfItems());
        assertTrue(processor.displayContents(marcel_account).contains(ONE_NORMAL_CASTEX));
        assertFalse(processor.displayContents(marcel_account).contains(ONE_SUPER_CASTEX));
        exceptionRule.expect(ItemNotFoundException.class);
        exceptionRule.expectMessage(marcel_account.getEmail()+": ItemNotFoundException");
        modifier.removeFromCart(marcel_account, ONE_SUPER_CASTEX);
    }

    @Test
    public void clear() throws NullQuantityException {
        Cart marcel_cart = processor.getCustomerCart(marcel_account);
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        modifier.addToCart(marcel_account, ONE_NORMAL_CASTEX);
        assertEquals(QTY_2,marcel_cart.numberOfItems());
        modifier.clearCart(marcel_account);
        assertEquals(QTY_0, marcel_cart.numberOfItems());
    }

    @Test
    public void priceCart() throws NullQuantityException {
        assertEquals(PRICE_0, processor.priceCart(marcel_account), DELTA);
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        assertEquals(PRICE_10, processor.priceCart(marcel_account), DELTA);
        modifier.addToCart(marcel_account, ONE_SUPER_CASTEX);
        assertEquals(PRICE_20, processor.priceCart(marcel_account), DELTA);
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
