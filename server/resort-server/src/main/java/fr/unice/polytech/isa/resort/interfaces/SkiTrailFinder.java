package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.ejb.Local;

@Local
public interface SkiTrailFinder {
    SkiTrail findByName(String resortId, String name) throws ResortNotFoundException, SkiTrailNotFoundException;
    SkiTrail findById(String id) throws SkiTrailNotFoundException;
}
