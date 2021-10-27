package fr.unice.polytech.isa.accounts.components;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.entities.accounts.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Customer registry bean.
 * <p>
 * Registers and finds the customers.
 * </p>
 */
@Stateless
public class CustomerRegistryBean implements CustomerFinder, CustomerRegistration {
    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The user finder.
     */
    @EJB
    private UserFinder userFinder;

    /**
     * Registers a customer.
     *
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @param email The email of the customer.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    @Override
    public void register(String firstName, String lastName, String email) throws UnavailableEmailException {
        // Check the email availability.
        Optional<User> maybeUser = userFinder.findUser(email);
        if (!maybeUser.isPresent()) {
            // Save the customer.
            Customer customer = new Customer(firstName, lastName, email);
            entityManager.persist(customer);
        } else {
            throw new UnavailableEmailException(email);
        }
    }

    /**
     * Removes a customer.
     *
     * @param email The email of the customer.
     * @throws CustomerNotFoundException if the customer was not found.
     */
    @Override
    public void deleteCustomer(String email) throws CustomerNotFoundException {
        // Remove the customer.
        Customer customer = findByMail(email);
        entityManager.remove(customer);
    }

    /**
     * Returns all the customers.
     *
     * @return The list of customers.
     */
    @Override
    public List<Customer> getCustomers() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root = criteria.from(Customer.class);
        CriteriaQuery<Customer> all = criteria.select(root);
        TypedQuery<Customer> query = entityManager.createQuery(all);
        return query.getResultList();
    }

    /**
     * Returns the premium customers.
     *
     * @return The list of premium customers.
     */
    @Override
    public List<Customer> getPremiumCustomers() {
        return getCustomers()
            .stream()
            .filter(customer -> {
                List<SuperCartex> superCards = customer.getSuperCartexs();
                for (SuperCartex superCard : superCards) {
                    return !superCard.hasExpired();
                }
                return false;
            })
            .collect(Collectors.toList());
    }

    /**
     * Finds a customer by email.
     *
     * @param email The email of the customer.
     * @return The requested customer.
     * @throws CustomerNotFoundException if the customer was not found.
     */
    @Override
    public Customer findByMail(String email) throws CustomerNotFoundException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root = criteria.from(Customer.class);
        criteria.select(root).where(builder.equal(root.get("email"), email));
        TypedQuery<Customer> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            throw new CustomerNotFoundException(email);
        }
    }

    /**
     * Finds the customer with a specific first name.
     *
     * @param firstName The first name to be found.
     * @return The list of customers with the specified first name.
     */
    @Override
    public List<Customer> findByFirstName(String firstName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root =  criteria.from(Customer.class);
        criteria.select(root).where(builder.equal(root.get("firstName"), firstName));
        TypedQuery<Customer> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    /**
     * Finds the customer with a specific last name.
     *
     * @param lastName The last name to be found.
     * @return The list of customers with the specified last name.
     */
    @Override
    public List<Customer> findByLastName(String lastName){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root =  criteria.from(Customer.class);
        criteria.select(root).where(builder.equal(root.get("lastName"), lastName));
        TypedQuery<Customer> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }
}
