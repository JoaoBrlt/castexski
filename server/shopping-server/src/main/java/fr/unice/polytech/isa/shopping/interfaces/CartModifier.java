package fr.unice.polytech.isa.shopping.interfaces;


import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.exceptions.ItemNotFoundException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;

import javax.ejb.Local;

@Local
public interface CartModifier {
    void addToCart(Customer customer, Item item) throws NullQuantityException;
    void removeFromCart(Customer customer, Item item) throws ItemNotFoundException, NullQuantityException;
    void clearCart(Customer customer);
}
