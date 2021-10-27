package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.common.entities.items.Pass;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface CustomerPassFinder {

    Optional<Pass> findPassById(String email, String id) throws CustomerNotFoundException;

    List<Pass> findCustomerPasses(String email) throws CustomerNotFoundException;

    List<Pass> findNotLinkedPass(String email) throws CustomerNotFoundException;
    List<Pass> findLinkedPass(String email) throws CustomerNotFoundException;
}
