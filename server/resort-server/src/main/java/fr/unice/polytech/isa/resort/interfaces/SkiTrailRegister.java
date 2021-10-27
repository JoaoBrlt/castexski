package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;

import javax.ejb.Local;

@Local
public interface SkiTrailRegister {
    void registerSkiTrail(String resortId, String name, boolean isOpen) throws ResortNotFoundException, UnavailableNameException;
    void deleteSkiTrail(String resortId, String id) throws SkiTrailNotFoundException, ResortNotFoundException;
}
