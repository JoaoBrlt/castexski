package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;

import javax.ejb.Local;

@Local
public interface AccessRegister {
    void addAccess(String passName, String resortId, String skiLiftId) throws ResortNotFoundException, SkiLiftNotFoundException, PassNotFoundException;
    void removeAccess(String passName, String resortId)throws ResortNotFoundException, PassNotFoundException;
}
