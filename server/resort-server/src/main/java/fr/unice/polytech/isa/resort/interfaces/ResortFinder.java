package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ResortFinder {
    Resort findByName(String name) throws ResortNotFoundException;
    Resort findById(String id) throws ResortNotFoundException;
    List<Resort> findAllResorts();
}
