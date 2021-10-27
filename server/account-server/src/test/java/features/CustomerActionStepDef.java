package features;

import arquillian.AbstractAccountTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import fr.unice.polytech.isa.accounts.exceptions.*;
import fr.unice.polytech.isa.accounts.interfaces.*;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources")
public class CustomerActionStepDef extends AbstractAccountTest {

    @EJB private CustomerRegistration registry;
    @EJB private CustomerFinder finder;
    @EJB private CustomerUpdater updater;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Customer customer;
    private Customer newCustomer;
    private String oldData;
    private String newData;

    @Given("^a customer named (.*) (.*) with email (.*)$")
    public void create_customer(String firstName, String lastName, String email) throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(firstName,lastName,email);
        customer = finder.findByMail(email);
    }

    @When("^new customer (.*) (.*) registers$")
    public void new_customer(String firstName, String lastName){
        newCustomer = new Customer();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
    }

    @And("^their email (.*) is available$")
    public void checkAvailability(String email) {
        boolean canRegister = false;
        try {
            finder.findByMail(email);
        } catch (CustomerNotFoundException e) {
            canRegister = true;
            newCustomer.setEmail(email);
        }
        assertTrue(canRegister);
    }

    @Then("their account is created")
    public void register_customer() throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(newCustomer.getFirstName(), newCustomer.getLastName(), newCustomer.getEmail());
        assertNotNull(finder.findByMail(newCustomer.getEmail()));
        assertEquals(newCustomer.getFirstName(), finder.findByMail(newCustomer.getEmail()).getFirstName());
    }

    @When("Account (.*) edits their first name to (.*)")
    public void edit_first_name(String email, String firstname) throws CustomerNotFoundException {
        customer=finder.findByMail(email);
        oldData=customer.getFirstName();
        assertTrue(updater.updateFirstName(email,firstname));
        customer=finder.findByMail(email);
    }

    @When("Account (.*) edits their last name to (.*)")
    public void edit_last_name(String email, String lastName) throws CustomerNotFoundException {
        customer=finder.findByMail(email);
        oldData=customer.getLastName();
        assertTrue(updater.updateLastName(email,lastName));
        customer=finder.findByMail(email);
    }

    @When("Account (.*) edits their email to (.*)")
    public void edit_email(String email, String newMail) throws CustomerNotFoundException {
        customer=finder.findByMail(email);
        oldData = email;
        newData = newMail;
    }

    @When("Email (.*) is already taken")
    public void existing_mail(String newMail) throws UnavailableEmailException, CustomerNotFoundException {
        newCustomer = new Customer("Name","LastName",newMail);
        registry.register(newCustomer.getFirstName(), newCustomer.getLastName(), newCustomer.getEmail());
        assertNotNull(finder.findByMail(newMail));
        assertEquals("Name", finder.findByMail(newMail).getFirstName());
    }



    @Then("their account is updated")
    public void check_update(){
        assertNotEquals(oldData,customer.getFirstName());
        assertNotEquals(oldData,customer.getLastName());
        assertNotEquals(oldData,customer.getEmail());
    }

    @Then("their email account is not updated")
    public void check_not_updated() throws CustomerNotFoundException {
        try {
            updater.updateEmail(oldData,newData);
        } catch (UnavailableEmailException e){
            assertEquals(oldData,customer.getEmail());
        }
        assertEquals(oldData,customer.getEmail());
        assertNotEquals(newData,customer.getEmail());
    }

    /**
     * Cleaning up the test context between each test cases
     */

    @PersistenceContext private EntityManager entityManager;
    @Inject UserTransaction utx;

    @cucumber.api.java.After
    public void cleaningUpContext() throws Exception {
        utx.begin();
        customer = entityManager.merge(customer);
        newCustomer = entityManager.merge(newCustomer);
        entityManager.remove(customer);
        entityManager.remove(newCustomer);
        utx.commit();
        customer = null;
        newCustomer = null;
    }
}
