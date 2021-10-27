package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/accesschecking")
public interface AccessCheckingService {
    @WebMethod
    boolean checkCard(
        @WebParam(name="liftId") String liftId,
        @WebParam(name="physicalCardId") String physicalCardId
    ) throws SkiLiftNotFoundException, CardNotFoundException;
}
