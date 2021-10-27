package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.accounts.Customer;

import javax.ejb.Local;
import java.util.List;

/**
 * Customer finder.
 * <p>
 * Searches for customers according to various criteria.
 * </p>
 */
@Local
public interface CustomerFinder {
    /**
     * Returns all the customers.
     *
     * @return The list of customers.
     */
    List<Customer> getCustomers();

    /**
     * Returns the premium customers.
     *
     * @return The list of premium customers.
     */
    List<Customer> getPremiumCustomers();

    /**
     * Finds a customer by email.
     *
     * @param email The email of the customer.
     * @return The requested customer.
     * @throws CustomerNotFoundException if the customer was not found.
     */
    Customer findByMail(String email) throws CustomerNotFoundException;

    /**
     * Finds the customer with a specific first name.
     *
     * @param firstName The first name to be found.
     * @return The list of customers with the specified first name.
     */
    List<Customer> findByFirstName(String firstName);

    /**
     * Finds the customer with a specific last name.
     *
     * @param lastName The last name to be found.
     * @return The list of customers with the specified last name.
     */
    List<Customer> findByLastName(String lastName);
}
