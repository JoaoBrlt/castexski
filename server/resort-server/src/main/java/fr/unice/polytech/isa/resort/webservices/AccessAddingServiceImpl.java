package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.AccessRegister;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/accessadding")
@Stateless(name = "AccessAddingWS")
public class AccessAddingServiceImpl implements AccessAddingService{
    @EJB
    AccessRegister accessRegister;

    @Override
    public void addAccess(String passName, String resortId, String skiLiftId) throws ResortNotFoundException, SkiLiftNotFoundException, PassNotFoundException {
        accessRegister.addAccess(passName, resortId, skiLiftId);
    }
}
