package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;

import javax.ejb.Local;

@Local
public interface SkiLiftRegister {
    void registerSkiLift(String resortId, String name, boolean isOpen) throws UnavailableNameException, ResortNotFoundException;
    void deleteSkiLift(String id) throws SkiLiftNotFoundException;
    void registerDoubleSkiLift(String resortId, String name, boolean isOpen, String timeLimit) throws ResortNotFoundException;
}
