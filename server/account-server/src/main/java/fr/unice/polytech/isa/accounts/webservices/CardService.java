package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/card")
public interface CardService {

    @WebMethod
    void linkPhysicalCard(@WebParam(name="cardId") String cardId,
                 @WebParam(name="physicalId") String physicalId) throws CardNotFoundException;

    @WebMethod
    void linkPassToCardOnline(@WebParam(name="email") String email, @WebParam(name="physicalId") String physicalId, @WebParam(name="passId") String passId) throws CardNotFoundException, PassNotFoundException, CustomerNotFoundException;

    @WebMethod
    List<String> getCardsNotPhysicallyLinked(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    List<String> getCardsPhysicallyLinked(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    String getCardIdByPhysicalId(@WebParam(name="physicalId") String physicalId) throws CardNotFoundException;

    @WebMethod
    String getCardNameById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    String getCardTypeById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    double getCardPriceById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    boolean isCardLinkedWPassById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    boolean isCardPhysicallyLinkedById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    String getPhysicalCardIdById(@WebParam(name="cardId") String cardId) throws CardNotFoundException;

    @WebMethod
    String getLinkedPassIdByCardId(@WebParam(name="cardId") String cardId) throws CardNotFoundException;
    }
