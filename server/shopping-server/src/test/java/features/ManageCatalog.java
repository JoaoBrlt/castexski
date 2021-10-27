package features;

import arquillian.AbstractShoppingTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;


import java.time.Duration;

import static fr.unice.polytech.isa.shopping.ShoppingTestUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(CukeSpace.class)
@CucumberOptions(features= "src/test/resources",tags = "@catalog")
public class ManageCatalog extends AbstractShoppingTest {
    @EJB
    private CatalogModifier modifier;
    @EJB
    private CatalogExplorer explorer;


    @Given("^a catalog with a (special|regular) card named (.*) at the price of (\\d+.\\d+?)$")
    @When("^an employee add to the catalog a (special|regular) card named (.*) at the price of (\\d+.\\d+?)$")
    public void addNewCard(String kindOfCard, String name, double price) throws ItemAlreadyExistException {
        modifier.addCard(name,kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD, price, PUBLIC_ITEM);
    }

    @Given("^a catalog with a pass named (.*) with a duration of (\\d+) (?:day|days) at the regular price of (\\d+.\\d+?) and (\\d+.\\d+?) for the children$")
    @When("^an employee add to the catalog a pass named (.*) with a duration of (\\d+) (?:day|days) at the regular price of (\\d+.\\d+?) and (\\d+.\\d+?) for the children$")
    public void addNewPass(String passName, int durationInDays, double price, double childrenPrice) throws ItemAlreadyExistException {
        modifier.addPass(passName, price, childrenPrice, Duration.ofDays(durationInDays), PUBLIC_ITEM);
    }

    @When("^an employee remove from the catalog the (special|regular) card named (.*)$")
    public void removeCard(String kindOfCard, String name) throws UnknownCatalogEntryException {
        modifier.deleteCard(name,kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD);
    }

    @When("^an employee remove from the catalog the pass named (.*) with a duration of (\\d+) (?:day|days)$")
    public void addNewPass(String passName, int durationInDays) throws UnknownCatalogEntryException {
        modifier.deletePass(passName, Duration.ofDays(durationInDays));
    }

    @Then("^the catalog contains a (special|regular) card named (.*)$")
    public void catalogContains(String kindOfCard, String passName){
        assertTrue(explorer.findCard(passName ,kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD).isPresent());
    }

    @Then("^the catalog contains a pass named (.*) with a duration of (\\d+) (?:day|days)$")
    public void catalogContains(String passName, int durationInDays){
        assertTrue(explorer.findPass(passName , Duration.ofDays(durationInDays)).isPresent());
    }

    @Then("^the catalog doesn't contains a (special|regular) card named (.*)$")
    public void catalogDoesNotContains(String kindOfCard, String passName){
        assertFalse(explorer.findCard(passName ,kindOfCard.equals(REGULAR) ? NORMAL_CARD : SUPERCARTEX_CARD).isPresent());
    }

    @Then("^the catalog doesn't contains a pass named (.*) with a duration of (\\d+) (?:day|days)$")
    public void catalogDoesNotContains(String passName, int durationInDays){
        assertFalse(explorer.findPass(passName , Duration.ofDays(durationInDays)).isPresent());
    }

    /**
     * Cleaning up the test context between each test cases
     */

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    UserTransaction utx;

    @cucumber.api.java.After
    public void cleaningUpContext() throws Exception {
        utx.begin();
        for (ItemCatalog c : explorer.displayCatalog()){
            ItemCatalog item  = entityManager.merge(c);
            entityManager.remove(item);
        }
        utx.commit();
    }
}
