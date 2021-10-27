package fr.unice.polytech.isa.accounts.webservices;


import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/pass")
public interface PassService {
    @WebMethod
    List<String> getNotLinkedPass(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    List<String> getLinkedPass(@WebParam(name="email") String email) throws CustomerNotFoundException;

    @WebMethod
    String getPassNameById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    double getPassPriceById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    String getPassDurationById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    Date getPassStartDateById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    Date getPassEndDateById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    boolean isPassLinkedById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    String getPassPhysicalCardLinkedById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    boolean isActivatedPassById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;
    @WebMethod
    boolean isChildPassById(@WebParam(name="email") String email, @WebParam(name="passId") String id) throws CustomerNotFoundException, PassNotFoundException;

    }
