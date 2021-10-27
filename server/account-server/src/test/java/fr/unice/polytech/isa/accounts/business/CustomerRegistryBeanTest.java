package fr.unice.polytech.isa.accounts.business;

import arquillian.AbstractAccountTest;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
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
import javax.transaction.*;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CustomerRegistryBeanTest extends AbstractAccountTest {

    @EJB private CustomerRegistration registry;
    @EJB private CustomerFinder finder;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    private final String firstName = "Marcel";
    private final String lastName = "Eats";
    private final String marcelMail = "marcel@mail.mail";
    private Customer marcel;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUpContext(){
        marcel = new Customer(firstName, lastName, marcelMail);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        List<Customer> toDispose = finder.findByFirstName(marcel.getFirstName());
        //TODO: réussir à clean tous les customer sans exception
        for(Customer cust : toDispose){
            Customer c = entityManager.merge(cust);
            entityManager.remove(c);
        }
        utx.commit();
    }

    @Test
    public void unknownCustomer() {
        assertTrue(finder.findByFirstName("Marceline").isEmpty());
    }

    @Test
    public void registerMarcelWithAvailableMail()
        throws CustomerNotFoundException, UnavailableEmailException {

        registry.register(firstName,lastName,marcelMail);
        Customer customer = finder.findByMail(marcelMail);
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(marcelMail, customer.getEmail());
    }

    @Test(expected = UnavailableEmailException.class)
    public void registerMarcelWithUnavailableMail()
        throws UnavailableEmailException {
        registry.register(firstName,lastName,marcelMail);
        registry.register(firstName,lastName,marcelMail);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void removeMarcel()
        throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(firstName,lastName,marcelMail);
        registry.deleteCustomer(marcelMail);
        finder.findByMail(marcelMail);
        exceptionRule.expect(CustomerNotFoundException.class);
    }

}
