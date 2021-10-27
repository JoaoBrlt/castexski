package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;

import javax.ejb.Local;

@Local
public interface SkiLiftFinder {
    SkiLift findByName(String resortId, String name) throws ResortNotFoundException, SkiLiftNotFoundException;
    SkiLift findById(String id) throws SkiLiftNotFoundException;
}
