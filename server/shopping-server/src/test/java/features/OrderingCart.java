package features;

import arquillian.AbstractShoppingTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.ItemNotFoundException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.*;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.time.Duration;
import java.time.YearMonth;

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static org.junit.Assert.*;

@RunWith(CukeSpace.class)
@CucumberOptions(features= "src/test/resources",tags = "@cart")
public class OrderingCart extends AbstractShoppingTest {

    @EJB
    private CustomerRegistration registration;
    @EJB
    private CustomerFinder finder;
    @EJB
    private CartModifier modifier;
    @EJB
    private CartProcessor processor;
    @EJB
    private CreditCardRegistration creditCardRegistration;
    @EJB
    private CatalogModifier catalogModifier;
    @EJB
    private CatalogExplorer catalogExplorer;

    private Customer customer;

    @Before
    public void initCatalog() throws ItemAlreadyExistException {
        catalogModifier.addCard(SUPER_CARTEX, SUPERCARTEX_CARD, PRICE_10, PUBLIC_ITEM);
        catalogModifier.addCard(CARTEX, NORMAL_CARD, PRICE_2, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_10, DAYS_1, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_10, PRICE_15, DAYS_3, PUBLIC_ITEM);
        catalogModifier.addPass(DISCOVERY_PASS, PRICE_25, PRICE_20, DAYS_5, PUBLIC_ITEM);
    }

    @Given("^a customer named (.*) (.*) logged with the email (.*), and his credit card saved into his account. Digits: (\\d{16}) Exp. month: (\\d{1,2}) Exp. year: (\\d{4}) CVV: (\\d{3})$")
    public void createCustomer(String firstname, String lastname,  String email, String digits, int monthExp, int yearExp, String cvv) throws UnavailableEmailException, CustomerNotFoundException {
        registration.register(firstname, lastname, email);
        customer = finder.findByMail(email);
        creditCardRegistration.creditCardRegistry(customer, lastname, digits, cvv, YearMonth.of(yearExp,monthExp), true);
    }


    @When("^(.*) wants to order (\\d+) (regular|special) (?:card|cards) (.*)$")
    public void addCardToCart(String email, int cardQty, String kindOfCard, String cardName) throws NullQuantityException, CustomerNotFoundException, UnknownCatalogEntryException {
        customer = finder.findByMail(email);
        modifier.addToCart(customer, catalogExplorer.pickCard(cardName, kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD, cardQty));
    }

    @When("^(.*) wants to order (\\d+) (adult|children) (?:pass|passes) (.*) for (\\d+) days$")
    public void addPassToCart(String email, int passQty, String kindOfPass, String passName, int durationInDays) throws NullQuantityException, CustomerNotFoundException, UnknownCatalogEntryException {
        customer = finder.findByMail(email);
        modifier.addToCart(customer, catalogExplorer.pickPass(passName,  Duration.ofDays(durationInDays), kindOfPass.equals(ADULT) ? ADULT_PASS : CHILD_PASS, passQty));
    }

    @When("^(.*) wants to remove (\\d+) (regular|special) (?:card|cards) (.*)$")
    public void removeCardFromCart(String email, int cardQty, String kindOfCard, String cardName) throws ItemNotFoundException, NullQuantityException, CustomerNotFoundException, UnknownCatalogEntryException {
        customer = finder.findByMail(email);
        modifier.removeFromCart(customer, catalogExplorer.pickCard(cardName, kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD, cardQty));
    }

    @When("^(.*) wants to remove (\\d+) (adult|children) (?:pass|passes) (.*) for (\\d+) days$")
    public void removePassFromCart(String email, int passQty, String kindOfPass, String passName, int durationInDays) throws NullQuantityException, CustomerNotFoundException, UnknownCatalogEntryException, ItemNotFoundException {
        customer = finder.findByMail(email);
        modifier.removeFromCart(customer, catalogExplorer.pickPass(passName,  Duration.ofDays(durationInDays), kindOfPass.equals(ADULT) ? ADULT_PASS : CHILD_PASS, passQty));
    }

    @Then("^the cart of (.*) contains (\\d+) (regular|special) (?:card|cards) (.*)$")
    public void checkCartContentForCards(String email, int cardQty, String kindOfCard, String cardName) throws CustomerNotFoundException, NullQuantityException, UnknownCatalogEntryException {
        customer = finder.findByMail(email);
        assertTrue(processor.displayContents(customer).contains(catalogExplorer.pickCard(cardName,  kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD, cardQty)));
    }
    @Then("^the cart of (.*) contains (\\d+) (adult|children) (?:pass|passes) (.*) for (\\d+) days$")
    public void checkCartContentForPasses(String email, int passQty, String kindOfPass, String passName, int durationInDays) throws CustomerNotFoundException, NullQuantityException, UnknownCatalogEntryException {
        customer = finder.findByMail(email);
        assertTrue(processor.displayContents(customer).contains(catalogExplorer.pickPass(passName,  Duration.ofDays(durationInDays), kindOfPass.equals(ADULT) ? ADULT_PASS : CHILD_PASS, passQty)));
    }

    @Then("^the cart of (.*) contains (\\d+) different (?:item|items)$")
    public void checkNumberOfItems(String email, int qty) throws CustomerNotFoundException {
        customer = finder.findByMail(email);
        assertEquals(qty, processor.displayContents(customer).size());
    }

    @Then("^the total price of the cart of (.*) is equal to (\\d+.\\d+?)$")
    public void checkCurrentPrice(String email, double expectedPrice) throws CustomerNotFoundException {
        customer = finder.findByMail(email);
        assertEquals(expectedPrice, processor.priceCart(customer), 0.01);
    }

    /**
     * Cleaning up the test context between each test cases
     */

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    UserTransaction utx;

    @After
    public void cleaningUpContext() throws Exception {
        utx.begin();
        customer = entityManager.merge(customer);
        entityManager.remove(customer);
        for (ItemCatalog c : catalogExplorer.displayCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        utx.commit();
        customer = null;
    }
}
