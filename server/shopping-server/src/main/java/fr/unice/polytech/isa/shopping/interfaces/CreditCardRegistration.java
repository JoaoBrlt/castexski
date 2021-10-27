package fr.unice.polytech.isa.shopping.interfaces;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.exceptions.NoCreditCardException;

import javax.ejb.Local;
import java.time.YearMonth;

@Local
public interface CreditCardRegistration {
    void creditCardRegistry(Customer customer, String name, String number, String securityCode, YearMonth expiryDate, boolean saveChoice);
    void creditCardDeletion(Customer customer) throws NoCreditCardException;

}
