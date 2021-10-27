package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/accessadding")
public interface AccessAddingService {
    @WebMethod
    void addAccess(
        String passName,
        String resortId,
        String skiLiftId
    ) throws ResortNotFoundException, SkiLiftNotFoundException, PassNotFoundException;
}
