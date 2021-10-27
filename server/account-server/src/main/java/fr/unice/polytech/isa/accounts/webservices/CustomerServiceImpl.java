package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/customer")
@Stateless(name = "CustomerWS")
public class CustomerServiceImpl implements CustomerService {
    @EJB
    private CustomerFinder customerFinder;

    @EJB
    private CustomerRegistration customerRegistration;

    @Override
    public void register(String firstName, String lastName, String email) throws UnavailableEmailException {
        customerRegistration.register(firstName, lastName, email);
    }

    @Override
    public void deleteCustomer(String email) throws CustomerNotFoundException {
        customerRegistration.deleteCustomer(email);
    }

    @Override
    public boolean exists(String email) {
        try {
            customerFinder.findByMail(email);
            return true;
        } catch (CustomerNotFoundException e) {
            return false;
        }
    }

    @Override
    public String getUserFullName(String email) throws CustomerNotFoundException {
        return customerFinder.findByMail(email).getFullName();
    }

    @Override
    public boolean hasCreditCard(String email) throws CustomerNotFoundException {
        return customerFinder.findByMail(email).getCreditCard() != null;
    }

    @Override
    public String getCreditCardName(String email) throws CustomerNotFoundException {
        return customerFinder.findByMail(email).getCreditCard().getName();
    }

    @Override
    public String getCreditCardNumber(String email) throws CustomerNotFoundException {
        String cardNumber = customerFinder.findByMail(email).getCreditCard().getNumber();
        int cacheSize = Math.max(cardNumber.length()-4,0);
        StringBuilder cache = new StringBuilder();
        for (int i = 0; i < cacheSize; i++) { cache.append('*'); }
        return cardNumber.replace(cardNumber.substring(0, cacheSize), cache);
    }

    @Override
    public String getCreditCardDate(String email) throws CustomerNotFoundException {
        return customerFinder.findByMail(email).getCreditCard().getFormattedExpiryDate();
    }
}
