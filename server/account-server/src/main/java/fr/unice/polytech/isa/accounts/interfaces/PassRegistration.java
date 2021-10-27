package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.Pass;

import javax.ejb.Local;

@Local
public interface PassRegistration {
    String registerPass(String customer, ItemType item) throws CustomerNotFoundException;
    void activatePass(Pass pass);

}
