package fr.unice.polytech.isa.shopping.interfaces;


import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.exceptions.EmptyCartException;
import fr.unice.polytech.isa.common.exceptions.NoCreditCardException;
import fr.unice.polytech.isa.common.exceptions.PaymentException;

import javax.ejb.Local;
import java.util.Optional;
import java.util.Set;

@Local
public interface CartProcessor {
    void validateCart(Customer customer) throws EmptyCartException, NoCreditCardException, PaymentException, CustomerNotFoundException;
    Set<Item> displayContents(Customer customer);
    Optional<Item> findItemById(Customer customer, int id);
    Cart getCustomerCart(Customer customer);
    double priceCart(Customer customer);

}
