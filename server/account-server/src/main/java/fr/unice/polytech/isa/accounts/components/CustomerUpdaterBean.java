package fr.unice.polytech.isa.accounts.components;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerUpdater;
import fr.unice.polytech.isa.common.entities.accounts.Customer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class CustomerUpdaterBean implements CustomerUpdater {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager manager;

    @EJB private CustomerFinder customerFinder;

    @Override
    public boolean updateFirstName(String email, String firstName) throws CustomerNotFoundException {
        Customer customer = customerFinder.findByMail(email);
        customer.setFirstName(firstName);
        manager.persist(customer);
        return customer.getFirstName().equals(firstName);
    }

    @Override
    public boolean updateLastName(String email, String lastName) throws CustomerNotFoundException {
        Customer customer = customerFinder.findByMail(email);
        customer.setLastName(lastName);
        manager.persist(customer);
        return customer.getLastName().equals(lastName);
    }

    @Override
    public boolean updateEmail(String oldMail, String newMail) throws UnavailableEmailException, CustomerNotFoundException {
        Customer customer = customerFinder.findByMail(oldMail);
        try {
            customerFinder.findByMail(newMail);
            throw new UnavailableEmailException(newMail);
        }
        catch (CustomerNotFoundException e){
            customer.setEmail(newMail);
            manager.persist(customer);
            return customer.getEmail().equals(newMail);
        }
    }
}
