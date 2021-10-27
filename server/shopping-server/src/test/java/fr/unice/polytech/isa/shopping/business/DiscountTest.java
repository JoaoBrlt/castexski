package fr.unice.polytech.isa.shopping.business;

import arquillian.AbstractShoppingTest;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;
import fr.unice.polytech.isa.shopping.interfaces.SuperCartexDiscount;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DiscountTest extends AbstractShoppingTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    @EJB private SuperCartexDiscount superCartexDiscount;
    @EJB private CatalogExplorer catalogExplorer;
    @EJB private CatalogModifier catalogModifier;
    @EJB private CardRegistration cardRegistration;
    @EJB private CardFinder cardFinder;
    private SuperCartex superCartex;
    private Customer marcel_account;


    @Before
    public void setUp() throws Exception {
        marcel_account = new Customer(MARCEL_FIRSTNAME, MARCEL_LASTNAME, MARCEL_EMAIL);
        entityManager.persist(marcel_account);
        catalogModifier.addPass(SUPER_ORIGINAL_PASS, PRICE_10, PRICE_10, DAYS_1, PRIVATE_ITEM);
        catalogModifier.addPass(SUPER_FREE_HOUR_PASS, PRICE_0, PRICE_0, HOURS_1, PRIVATE_ITEM);
        catalogModifier.addPass(SUPER_FREE_EIGHTH, PRICE_10, PRICE_10, DAYS_1, PRIVATE_ITEM);
        cardRegistration.addCard(MARCEL_EMAIL, new ItemType(SUPER_CARTEX, PRICE_10, ItemTypeName.SUPERCARTEX));
        superCartex = (SuperCartex) cardFinder.findSuperCartexCards().get(0);
    }


    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        marcel_account = entityManager.merge(marcel_account);

        for (ItemCatalog c : catalogExplorer.displayPrivateCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        entityManager.remove(marcel_account);
        marcel_account = null;
        utx.commit();
    }

    @Test
    public void findSuperCartexPass() throws NullQuantityException, UnknownCatalogEntryException, CustomerNotFoundException, PassNotFoundException, CardNotFoundException, ItemAlreadyExistException {
        assertEquals(catalogExplorer.pickPass(SUPER_FREE_HOUR_PASS, HOURS_1, ADULT_PASS, QTY_1), superCartexDiscount.findSuperCartexPass(superCartex));
        superCartex.setLastSwipe(DATE_NOW);
        superCartex.setFirstSwipe(DATE_NOW_MINUS_7_DAYS);
        assertEquals(catalogExplorer.pickPass(SUPER_FREE_EIGHTH, DAYS_1, ADULT_PASS, QTY_1), superCartexDiscount.findSuperCartexPass(superCartex));
        superCartex.setLastSwipe(DATE_NOW);
        superCartex.setFirstSwipe(DATE_NOW);
        assertEquals(catalogExplorer.pickPass(SUPER_ORIGINAL_PASS, DAYS_1, ADULT_PASS, QTY_1), superCartexDiscount.findSuperCartexPass(superCartex));
        catalogModifier.addPass(SUPER_DISCOUNT_THIS_MONTH, PRICE_2, PRICE_2, DAYS_1, PRIVATE_ITEM);
        assertEquals(catalogExplorer.pickPass(SUPER_DISCOUNT_THIS_MONTH, DAYS_1, ADULT_PASS, QTY_1), superCartexDiscount.findSuperCartexPass(superCartex));
        catalogModifier.addPass(SUPER_DISCOUNT_TODAY, PRICE_2, PRICE_2, DAYS_1, PRIVATE_ITEM);
        assertEquals(catalogExplorer.pickPass(SUPER_DISCOUNT_TODAY, DAYS_1, ADULT_PASS, QTY_1), superCartexDiscount.findSuperCartexPass(superCartex));
    }
}
