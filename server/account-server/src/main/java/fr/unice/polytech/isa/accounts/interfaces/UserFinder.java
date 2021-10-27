package fr.unice.polytech.isa.accounts.interfaces;

import fr.unice.polytech.isa.common.entities.accounts.User;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

/**
 * Users finder.
 * <p>
 * Searches for users according to various criteria.
 * </p>
 */
@Local
public interface UserFinder {
    /**
     * Returns all the users.
     *
     * @return The list of users.
     */
    List<User> getUsers();

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The requested user.
     */
    Optional<User> findUser(String email);
}
