package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.ejb.Local;

@Local
public interface OpennessChanger {
    void changeResortOpenness(String resortId, boolean isOpen) throws ResortNotFoundException;
    void changeSkiLiftOpenness(String skiLiftId, boolean isOpen) throws SkiLiftNotFoundException;
    void changeSkiTrailOpenness(String skiTrailId, boolean isOpen) throws SkiTrailNotFoundException;
}
