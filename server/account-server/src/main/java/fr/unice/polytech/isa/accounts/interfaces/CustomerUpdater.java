package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;

import javax.ejb.Local;

@Local
public interface CustomerUpdater {

    boolean updateFirstName(String email, String firstName) throws CustomerNotFoundException;

    boolean updateLastName(String email, String lastName) throws CustomerNotFoundException;

    boolean updateEmail(String oldMail, String newMail)
        throws UnavailableEmailException, CustomerNotFoundException;
}
