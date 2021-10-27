package fr.unice.polytech.isa.shopping.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/shopping")
public interface CartWebService {
    @WebMethod
    void addPassToCustomerCart(@WebParam(name="email") String email, @WebParam(name="passName") String passName, @WebParam(name="duration") String durationInDays, @WebParam(name="isChildrenPass") boolean isChildrenPass, @WebParam(name="quantity") int quantity) throws NullQuantityException, UnknownCatalogEntryException;
    @WebMethod
    void addCardToCustomerCart(@WebParam(name="email") String email, @WebParam(name="cardName") String cardName, @WebParam(name="isSuperCartex") boolean isSuperCartex, @WebParam(name="quantity") int quantity) throws NullQuantityException, UnknownCatalogEntryException;
    @WebMethod
    void removePassFromCustomerCart(@WebParam(name="email") String email, @WebParam(name="passName") String passName, @WebParam(name="duration") String durationInDays, @WebParam(name="isChildrenPass") boolean isChildrenPass, @WebParam(name="quantity") int quantity) throws ItemNotFoundException, NullQuantityException, UnknownCatalogEntryException;
    @WebMethod
    void removeCardFromCustomerCart(@WebParam(name="email") String email, @WebParam(name="cardName") String cardName, @WebParam(name="isSuperCartex") boolean isSuperCartex, @WebParam(name="quantity") int quantity) throws NullQuantityException, UnknownCatalogEntryException, ItemNotFoundException;

    @WebMethod
    void addCreditCard(@WebParam(name="email") String email, @WebParam(name="creditCardName") String name, @WebParam(name="creditCardNumber") String number, @WebParam(name="creditCardCVV") String securityCode, @WebParam(name="creditCardExpiryMonth") String expiryMonth, @WebParam(name="creditCardExpiryYear") String expiryYear, @WebParam(name="saveCreditCard") boolean saveChoice);

    @WebMethod
    void validateCustomerCart(@WebParam(name="email") String email) throws EmptyCartException, NoCreditCardException, PaymentException, CustomerNotFoundException;

    @WebMethod
    Set<Integer> displayCustomerCart(@WebParam(name="email") String email);

    @WebMethod
    String getItemNameById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;

    @WebMethod
    double getItemPriceById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;

    @WebMethod
    double getTotalPriceById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;

    @WebMethod
    String getItemTypeById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;

    @WebMethod
    int getItemQuantityById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;

    @WebMethod
    String getPassDurationById(@WebParam(name="email") String email, @WebParam(name="passItemId") int id) throws ItemNotFoundException;

    @WebMethod
    boolean isChildrenPassById(@WebParam(name="email") String email, @WebParam(name="passItemId") int id) throws ItemNotFoundException;

    @WebMethod
    boolean isSuperCartexById(@WebParam(name="email") String email, @WebParam(name="passItemId") int id) throws ItemNotFoundException;

    @WebMethod
    boolean isPassById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;
    @WebMethod
    boolean isCardById(@WebParam(name="email") String email, @WebParam(name="itemId") int id) throws ItemNotFoundException;
}
