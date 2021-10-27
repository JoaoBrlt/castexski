package fr.unice.polytech.isa.shopping.interfaces;


import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;

import javax.ejb.Local;

@Local
public interface SuperCartexDiscount {

    Item findSuperCartexPass(SuperCartex superCartex) throws CardNotFoundException, PassNotFoundException, CustomerNotFoundException, NullQuantityException, UnknownCatalogEntryException;
}
