package fr.unice.polytech.isa.statistics.business;

import arquillian.AbstractStatisticsTest;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;
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
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class InterceptorsTest extends AbstractStatisticsTest {

    @EJB private CardRegistration cardRegistration;
    @EJB private CustomerRegistration customerRegistration;
    @EJB private CardFinder cardFinder;
    @EJB private PassRegistration passRegistration;
    @EJB private CustomerPassFinder customerPassFinder;
    @EJB private CustomerCardLinker customerCardLinker;
    @EJB private CustomerFinder customerFinder;
    @EJB private PurchaseStatisticsFinder purchaseStatisticsFinder;
   // @EJB private PurchaseStatisticsWebService purchaseStatisticsWebService;

    @EJB private StatelessBeanToTestInterception bean;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    public static final String JOHN_FIRSTNAME = "John";
    public static final String JOHN_LASTNAME = "Doe";
    public static final String JOHN_EMAIL = "john@doe.com";

    public static final String DISCOVERY_PASS = "Discovery";
    public static final String REGULAR = "regular";
    public static final String PHYSICAL_CARD_ID = "0123456789101121";
    private static final String PASS_NAME = "PassName";

    private Card card;
    private Pass pass;

    @Before
    public void setUpContext() throws Exception {
        settingUpResort();
        settingUpSkiLift();

        //Setup customer, card and pass
        customerRegistration.register(JOHN_FIRSTNAME, JOHN_LASTNAME, JOHN_EMAIL);
        cardRegistration.addCard(JOHN_EMAIL, new ItemType(REGULAR, 0.0, ItemTypeName.CARD), PHYSICAL_CARD_ID);
        String passId = passRegistration.registerPass(JOHN_EMAIL,
                new PassType(DISCOVERY_PASS, false, Duration.ofMillis(1000), 0.0));
        pass = customerPassFinder.findPassById(JOHN_EMAIL, passId).get();
        addPass(DISCOVERY_PASS, 0.0, 0.0, Duration.ofMillis(1000), false);
        customerCardLinker.linkPassToCardOnline(JOHN_EMAIL, PHYSICAL_CARD_ID, pass.getId());
        card = cardFinder.findCardByPhysicalId(PHYSICAL_CARD_ID);
        card = entityManager.merge(card);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        //Delete resort, skiLift and access
        try {
            deleteResort(getResortId());
            removeDailyStats();
        } catch (Exception e) {
            //This shouldn't happen
        }
        //Delete customer, card and pass
        try {
            customerRegistration.deleteCustomer(JOHN_EMAIL);
        } catch (CustomerNotFoundException e) {
            //This shouldn't happen
        }
        try {
            deletePass(DISCOVERY_PASS, Duration.ofMillis(1000));
        } catch (UnknownCatalogEntryException e) {
            //This shouldn't happen
        }
        utx.commit();
    }

    @Test
    public void addingCard() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        assertTrue(retrievedSkiLift.getDailyStatistics().isEmpty());
        bean.methodToInterceptTrue(retrievedSkiLift,card);
        retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
    }

    @Test
    public void notAddingCard() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        assertTrue(retrievedSkiLift.getDailyStatistics().isEmpty());
        bean.methodToInterceptFalse(retrievedSkiLift,card);
        retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        assertTrue(retrievedSkiLift.getDailyStatistics().isEmpty());
    }

    @Test
    public void buyingCard() throws Exception {
        LocalDate date = LocalDate.now();
        Customer customer = customerFinder.findByMail(JOHN_EMAIL);
        getCustomerCart(customer);
        ItemType itemType = new ItemType("Card",5, ItemTypeName.CARD);
        Item item = new Item(itemType,5);
        addToCart(customer,item);
        bean.methodToInterceptCustomerCart(customer);
        DailyBuyingStatistics daily = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
        assertEquals(daily.getHourlyBuyingStatistics().size(),1);
        HourlyBuyingStatistics hourly = daily.getHourlyBuyingStatistics().get(0);
        assertEquals(hourly.getNbOfCards(),5);
        assertEquals(hourly.getNbOfChildPasses(),0);
        assertEquals(hourly.getNbOfCartex(),0);
        //assertEquals(5,purchaseStatisticsWebService.getNumberOfCardsBoughtOnDate(date.toString()));
    }

    @Test
    public void buyingPass() throws Exception {
        LocalDate date = LocalDate.now();
        Customer customer = customerFinder.findByMail(JOHN_EMAIL);
        getCustomerCart(customer);
        PassType itemType = new PassType(PASS_NAME,false, Duration.ZERO, 5);
        Item item = new Item(itemType,5);
        addToCart(customer,item);
        bean.methodToInterceptCustomerCart(customer);
        DailyBuyingStatistics daily = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
        assertEquals(daily.getHourlyBuyingStatistics().size(),1);
        HourlyBuyingStatistics hourly = daily.getHourlyBuyingStatistics().get(0);
        assertEquals(0,hourly.getNbOfCards());
        assertEquals(0,hourly.getNbOfChildPasses());
        assertEquals(0,hourly.getNbOfCartex());
        HashMap<String, Integer> passes = hourly.getNbOfPasses();
        assertEquals(1,passes.size());
        assertTrue(passes.containsKey(PASS_NAME));
        int res = passes.get(PASS_NAME);
        assertEquals(5,res);
        //assertEquals(5,purchaseStatisticsWebService.getNumberOfAllPassesBoughtOnDate(date.toString()));

    }

    private void removeDailyStats() throws Exception {
        DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(LocalDate.now());
        DailyBuyingStatistics d = entityManager.merge(dailyBuyingStatistics);
        entityManager.remove(d);
    }
}
