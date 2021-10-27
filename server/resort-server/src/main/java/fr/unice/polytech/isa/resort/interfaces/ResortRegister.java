package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;

import javax.ejb.Local;

@Local
public interface ResortRegister {
    void registerResort(String name, String email, boolean isOpen, String cityName) throws UnavailableNameException;
    void deleteResort(String id) throws ResortNotFoundException;
}
