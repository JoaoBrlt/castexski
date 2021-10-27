package fr.unice.polytech.isa.accounts.business;

import arquillian.AbstractAccountTest;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
import fr.unice.polytech.isa.accounts.interfaces.CustomerUpdater;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CustomerUpdaterBeanTest extends AbstractAccountTest {

    @EJB private CustomerRegistration registry;
    @EJB private CustomerFinder finder;
    @EJB private CustomerUpdater updater;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    private final String firstName = "Marcel";
    private final String lastName = "Eats";
    private final String marcelMail = "marcel@mail.mail";
    private final String otherMail = "marcel@gmail.mail";
    private Customer marcel;

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
    public void updateMarcelFirstName() throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(firstName,lastName,marcelMail);
        assertTrue(updater.updateFirstName(marcelMail,lastName));
        Customer retrieved = finder.findByMail(marcelMail);
        assertEquals(lastName, retrieved.getFirstName());
        assertTrue(updater.updateFirstName(marcelMail,firstName));
    }

    @Test
    public void updateMarcelLastName() throws UnavailableEmailException, CustomerNotFoundException {
        registry.register(firstName,lastName,marcelMail);
        assertTrue(updater.updateLastName(marcelMail,firstName));
        Customer retrieved = finder.findByMail(marcelMail);
        assertEquals(firstName, retrieved.getLastName());
    }

    @Test(expected = CustomerNotFoundException.class)
    public void updateMarcelMail() throws UnavailableEmailException, CustomerNotFoundException {
        String newMail = otherMail;
        registry.register(firstName,lastName,marcelMail);
        updater.updateEmail(marcelMail,newMail);
        Customer retrieved = finder.findByMail(newMail);
        assertEquals(firstName, retrieved.getFirstName());
        assertEquals(lastName, retrieved.getLastName());
        finder.findByMail(marcelMail);
    }

    @Test(expected = UnavailableEmailException.class)
    public void updateMarcelAlreadyExistingMail() throws UnavailableEmailException, CustomerNotFoundException {
        String usedMail = otherMail;
        registry.register(firstName,lastName,marcelMail);
        registry.register(firstName,lastName,usedMail);
        updater.updateEmail(marcelMail,usedMail);
    }
}
