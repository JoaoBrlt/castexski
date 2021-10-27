package features;

import arquillian.AbstractShoppingTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.shopping.interfaces.*;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CukeSpace.class)
@CucumberOptions(features= "src/test/resources",tags = "@super_cartex")
@Transactional(TransactionMode.COMMIT)
public class OrderingSuperCartex extends AbstractShoppingTest {

    @EJB
    private CustomerRegistration registration;
    @EJB
    private CustomerFinder finder;

    @EJB
    private CardFinder cardFinder;
    @EJB
    private CardRegistration cardRegistration;
    @EJB
    private CartModifier modifier;
    @EJB
    private PassRegistration passRegistration;
    @EJB
    private SuperCartexProcessor processor;
    @EJB
    private CustomerCardLinker cardLinker;
    @EJB
    private CreditCardRegistration creditCardRegistration;
    @EJB
    private CatalogModifier catalogModifier;
    @EJB
    private CatalogExplorer catalogExplorer;
    private boolean access;

    private Customer customer;
    private SuperCartex superCartex;

    @Before
    public void initCatalog() throws ItemAlreadyExistException, CustomerNotFoundException {
        catalogModifier.addCard(SUPER_CARTEX, SUPERCARTEX_CARD, PRICE_10, PUBLIC_ITEM);
        catalogModifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_10, DAYS_1, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_25, PRICE_20, DAYS_5, PUBLIC_ITEM);
        catalogModifier.addPass(SUPER_ORIGINAL_PASS, PRICE_10, PRICE_10, DAYS_1, PRIVATE_ITEM);
        catalogModifier.addPass(SUPER_FREE_HOUR_PASS, PRICE_0, PRICE_0, HOURS_1, PRIVATE_ITEM);
        catalogModifier.addPass(SUPER_FREE_EIGHTH, PRICE_10, PRICE_10, DAYS_1, PRIVATE_ITEM);
    }

    @Given("^a customer named (.*) (.*) logged with the email (.*), and his credit card saved into his account. Digits: (\\d{16}) Exp. month: (\\d{1,2}) Exp. year: (\\d{4}) CVV: (\\d{3})$")
    public void createCustomer(String firstname, String lastname,  String email, String digits, int monthExp, int yearExp, String cvv) throws UnavailableEmailException, CustomerNotFoundException {
        registration.register(firstname, lastname, email);
        customer = finder.findByMail(email);
        creditCardRegistration.creditCardRegistry(customer, lastname, digits, cvv, YearMonth.of(yearExp,monthExp), true);
    }


    @And("^the customer has a Super Cartex already linked with the physical card no (.*)$")
    public void createAndLinkSuperCartex(String physicalId) throws CustomerNotFoundException {
        cardRegistration.addCard(customer.getEmail(), new ItemType(SUPER_CARTEX, PRICE_10, ItemTypeName.SUPERCARTEX), physicalId);
        superCartex = (SuperCartex) cardFinder.findSuperCartexCards().get(0);
    }

    @Given("^an expired free hour pass is on the customer's supercartex$")
    public void loadExpiredFreeHourPass() throws CustomerNotFoundException, CardNotFoundException, PassNotFoundException {
        entityManager.merge(superCartex);
        superCartex.setPass(EXPIRED_SUPER_FREE_PASS);
        superCartex.setLastSwipe(LocalDateTime.now().minusHours(2));
        superCartex.setFirstSwipe(LocalDateTime.now().minusHours(2));
        cardLinker.linkPassToCardOnline(customer.getEmail(), superCartex.getPhysicalId(), passRegistration.registerPass(customer.getEmail(), SUPER_FREE_PASSTYPE));
    }

    @When("^the customer swipe his Super Cartex to a ski lift$")
    public void theCustomerSwipeHisSuperCartexToASkiLift() throws PaymentException, PassNotFoundException, NoCreditCardException, CustomerNotFoundException, CardNotFoundException, EmptyCartException, UnknownCatalogEntryException, NullQuantityException {
       access = processor.processSuperCartex(superCartex);
    }

    @Then("^a (.*) pass is loaded on his Super Cartex$")
    public void aPassAtThePriceOfIsLoadedOnHisSuperCartex(String passName) {
        superCartex = entityManager.merge(superCartex);
        if (passName.equals("SUPER_DISCOUNT_TODAY")) {
            passName = SUPER_DISCOUNT_TODAY;
        } else if (passName.equals("SUPER_DISCOUNT_THIS_MONTH")) {
            passName = SUPER_DISCOUNT_THIS_MONTH;
        }
        assertEquals(passName, superCartex.getPass().getType().getName());
    }

    @And("^the access is (granted|refused)$")
    public void accessResult(String access) {
        assertEquals(access.equals(GRANTED), this.access);
    }

    @And("^a first swipe (\\d+) days before$")
    public void aFirstSwipeDaysBefore(int day) {
        superCartex = entityManager.merge(superCartex);
        superCartex.setFirstSwipe(LocalDateTime.now().minusDays(7));
    }

    @And("^a new discount for the super cartex on (.*)$")
    public void newDiscount(String when) throws ItemAlreadyExistException {
        catalogModifier.addPass(when.equals("today") ? SUPER_DISCOUNT_TODAY : SUPER_DISCOUNT_THIS_MONTH, PRICE_2, PRICE_2, DAYS_1, PRIVATE_ITEM);
    }
    /**
     * Cleaning up the test context between each test cases
     */

    @PersistenceContext
    private EntityManager entityManager;


    @After
    public void cleaningUpContext() {
        customer = entityManager.merge(customer);
        entityManager.remove(customer);
        for (ItemCatalog c : catalogExplorer.displayCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        for (ItemCatalog c : catalogExplorer.displayPrivateCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        customer = null;
        superCartex = null;
    }


}
