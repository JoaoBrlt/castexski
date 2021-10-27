package fr.unice.polytech.isa.shopping.webservices;

import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/catalog")
public interface CatalogWebService {
    @WebMethod
    List<Integer> displayCatalog();

    @WebMethod
    void addCard(@WebParam(name="cardName") String cardName, @WebParam(name="isSuperCartex") boolean isSuperCartex, @WebParam(name="price") double price, @WebParam(name = "isPrivateItem") boolean isPrivateItem) throws ItemAlreadyExistException;

    @WebMethod
    void addPass(@WebParam(name="passName") String passName, @WebParam(name="regularPrice") double regularPrice, @WebParam(name="childrenPrice") double childrenPrice, @WebParam(name="durationInDays") String durationInDays,  @WebParam(name = "isPrivateItem") boolean isPrivateItem) throws ItemAlreadyExistException;

    @WebMethod
    void deletePass(@WebParam(name="passName") String passName, @WebParam(name="duration") String duration) throws UnknownCatalogEntryException;

    @WebMethod
    void deleteCard(@WebParam(name="cardName") String cardName, @WebParam(name="isSuperCartex") boolean isSuperCartex) throws UnknownCatalogEntryException;

    @WebMethod
    String getEntryNameById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    double getEntryRegularPriceById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    double getEntryChildrenPriceById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    String getEntryTypeById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    String getPassDurationById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    boolean isSuperCartexById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    boolean isCardById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
    @WebMethod
    boolean isPassById(@WebParam(name="entryId") int id) throws UnknownCatalogEntryException;
}
