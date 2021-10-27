package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/customer")
public interface CustomerService {
    @WebMethod
    void register(@WebParam(name="firstName") String firstName,
                  @WebParam(name="lastName") String lastName,
                  @WebParam(name="email") String email)
        throws UnavailableEmailException;

    @WebMethod
    void deleteCustomer(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    boolean exists(@WebParam(name="email") String email);

    @WebMethod
    String getUserFullName(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    boolean hasCreditCard(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    String getCreditCardName(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    String getCreditCardNumber(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    String getCreditCardDate(@WebParam(name="email") String email) throws CustomerNotFoundException;
}
