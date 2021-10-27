package fr.unice.polytech.isa.shopping.business;

import arquillian.AbstractShoppingTest;
import fr.unice.polytech.isa.common.entities.shopping.catalog.*;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CatalogTest extends AbstractShoppingTest {
    @EJB private CatalogModifier modifier;
    @EJB private CatalogExplorer explorer;
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;
    List<ItemCatalog> catalogList;


    @Before
    public void setUp(){
        catalogList = new ArrayList<>();
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void addCard() throws ItemAlreadyExistException {
        //Card adding
        assertFalse(explorer.findCard(CARTEX, NORMAL_CARD).isPresent());
        modifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        assertTrue(explorer.findCard(CARTEX, NORMAL_CARD).isPresent());
        exceptionRule.expect(ItemAlreadyExistException.class);
        exceptionRule.expectMessage(CARTEX + ITEM_ALREADY_EXIST_EXCEPTION);
        modifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
    }
    @Test
    public void addPass() throws ItemAlreadyExistException {
        //Pass adding
        assertFalse(explorer.findPass(DISCOVERY_PASS, DAYS_3).isPresent());
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_3, PUBLIC_ITEM);
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_3).isPresent());
        exceptionRule.expect(ItemAlreadyExistException.class);
        exceptionRule.expectMessage(DISCOVERY_PASS+ITEM_ALREADY_EXIST_EXCEPTION);
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_3, PUBLIC_ITEM);
        //Adding the same pass with a different duration
        assertFalse(explorer.findPass(DISCOVERY_PASS, DAYS_5).isPresent());
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_5, PUBLIC_ITEM);
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_5).isPresent());
    }

    @Test
    public void deleteCard() throws ItemAlreadyExistException, UnknownCatalogEntryException {
        //Card deletion
        modifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        assertTrue(explorer.findCard(CARTEX, NORMAL_CARD).isPresent());
        modifier.deleteCard(CARTEX, NORMAL_CARD);
        assertFalse(explorer.findCard(CARTEX, NORMAL_CARD).isPresent());
        exceptionRule.expect(UnknownCatalogEntryException.class);
        exceptionRule.expectMessage(CARTEX + UNKNOWN_CATALOG_ENTRY_EXCEPTION);
        modifier.deleteCard(CARTEX, NORMAL_CARD);
    }

    @Test
    public void deletePass() throws ItemAlreadyExistException, UnknownCatalogEntryException {
        //Pass deletion
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_3, PUBLIC_ITEM);
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_5, PUBLIC_ITEM);
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_3).isPresent());
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_5).isPresent());
        modifier.deletePass(DISCOVERY_PASS, DAYS_3);
        //Add Pass with a different duration
        assertFalse(explorer.findPass(DISCOVERY_PASS, DAYS_3).isPresent());
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_5).isPresent());
        exceptionRule.expect(UnknownCatalogEntryException.class);
        exceptionRule.expectMessage(DISCOVERY_PASS+ UNKNOWN_CATALOG_ENTRY_EXCEPTION);
        modifier.deletePass(DISCOVERY_PASS, DAYS_3);
    }

    @Test
    public void findPass() throws ItemAlreadyExistException {
        assertEquals(Optional.empty(), explorer.findPass(DISCOVERY_PASS, DAYS_3));
        modifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_5, PUBLIC_ITEM);
        assertEquals(Optional.empty(), explorer.findPass(DISCOVERY_PASS, DAYS_3));
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_5).isPresent());
        modifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
        assertTrue(explorer.findPass(DISCOVERY_PASS, DAYS_3).isPresent());
    }

    @Test
    public void pickPass() throws ItemAlreadyExistException, UnknownCatalogEntryException, NullQuantityException {
        modifier.addPass(DISCOVERY_PASS, PRICE_15, PRICE_10, DAYS_3, PUBLIC_ITEM);
        modifier.addPass(DISCOVERY_PASS, PRICE_25, PRICE_15, DAYS_5, PUBLIC_ITEM);
        Item twoChildDiscovery3Days = new Item(new PassType(DISCOVERY_PASS, CHILD_PASS, DAYS_3, PRICE_10), QTY_2);
        Item oneChildDiscovery5Days = new Item(new PassType(DISCOVERY_PASS, CHILD_PASS, DAYS_5, PRICE_15), QTY_1);
        assertEquals(twoChildDiscovery3Days, explorer.pickPass(DISCOVERY_PASS, DAYS_3, CHILD_PASS, QTY_2));
        assertEquals(oneChildDiscovery5Days, explorer.pickPass(DISCOVERY_PASS, DAYS_5, CHILD_PASS, QTY_1));

        //Different quantity
        assertNotEquals(twoChildDiscovery3Days, explorer.pickPass(DISCOVERY_PASS, DAYS_3, CHILD_PASS, QTY_1));
        //Different duration
        assertNotEquals(twoChildDiscovery3Days, explorer.pickPass(DISCOVERY_PASS, DAYS_5, CHILD_PASS, QTY_2));
    }

    @Test
    public void pickCard() throws ItemAlreadyExistException, UnknownCatalogEntryException, NullQuantityException {
        modifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        modifier.addCard(CARTEX, SUPERCARTEX_CARD, PRICE_2, PUBLIC_ITEM);
        Item oneCartex = new Item(new ItemType(CARTEX, PRICE_2, ItemTypeName.CARD), QTY_1);
        Item oneSuperCartex = new Item(new ItemType(CARTEX, PRICE_2, ItemTypeName.SUPERCARTEX), QTY_1);
        assertEquals(oneCartex, explorer.pickCard(CARTEX, NORMAL_CARD, QTY_1));
        assertEquals(oneSuperCartex, explorer.pickCard(CARTEX, SUPERCARTEX_CARD, QTY_1));

        //Different type of card (toll mode versus normal)
        assertNotEquals(oneCartex, explorer.pickCard(CARTEX, SUPERCARTEX_CARD, QTY_1));
        //Different quantity
        assertNotEquals(oneCartex, explorer.pickCard(CARTEX, NORMAL_CARD, QTY_2));
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        catalogList = explorer.displayCatalog();
        for (ItemCatalog c : catalogList){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        catalogList = null;
        utx.commit();
    }

}
