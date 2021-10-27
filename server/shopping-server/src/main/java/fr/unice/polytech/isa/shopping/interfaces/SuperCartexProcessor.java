package fr.unice.polytech.isa.shopping.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.*;

import javax.ejb.Local;

@Local
public interface SuperCartexProcessor {
    boolean processSuperCartex(SuperCartex superCartex) throws NullQuantityException, UnknownCatalogEntryException, CustomerNotFoundException, CardNotFoundException, PassNotFoundException, PaymentException, EmptyCartException, NoCreditCardException;
}
