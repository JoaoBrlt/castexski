package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;

import javax.ejb.Local;

/**
 * Customer registration.
 * <p>
 * Registers the customers.
 * </p>
 */
@Local
public interface CustomerRegistration {
    /**
     * Registers a customer.
     *
     * @param firstName The first name of the customer.
     * @param lastName The last name of the customer.
     * @param email The email of the customer.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    void register(String firstName, String lastName, String email) throws UnavailableEmailException;

    /**
     * Removes a customer.
     *
     * @param email The email of the customer.
     * @throws CustomerNotFoundException if the customer was not found.
     */
    void deleteCustomer(String email) throws CustomerNotFoundException;
}
