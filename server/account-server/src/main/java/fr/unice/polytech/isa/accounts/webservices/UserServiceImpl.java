package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.UserNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.accounts.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Optional;

/**
 * User service implementation.
 * <p>
 * Finds the users.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/user")
@Stateless(name = "UserWS")
public class UserServiceImpl implements UserService {
    /**
     * The user finder.
     */
    @EJB
    private UserFinder userFinder;

    /**
     * Indicates whether a user exists.
     *
     * @param email The email of the user.
     * @return <code>true</code> if the user exists; <code>false</code> otherwise.
     */
    @Override
    public boolean userExists(String email) {
        return userFinder.findUser(email).isPresent();
    }

    /**
     * Returns the type of a user.
     *
     * @param email The email of the user.
     * @return The type of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @Override
    public String getUserType(String email) throws UserNotFoundException {
        Optional<User> maybeUser = userFinder.findUser(email);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            return user.getType().name();
        } else {
            throw new UserNotFoundException(email);
        }
    }

    /**
     * Returns the first name of a user.
     *
     * @param email The email of the user.
     * @return The first name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @Override
    public String getUserFirstName(String email) throws UserNotFoundException {
        Optional<User> maybeUser = userFinder.findUser(email);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            return user.getFirstName();
        } else {
            throw new UserNotFoundException(email);
        }
    }

    /**
     * Returns the last name of a user.
     *
     * @param email The email of the user.
     * @return The last name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @Override
    public String getUserLastName(String email) throws UserNotFoundException {
        Optional<User> maybeUser = userFinder.findUser(email);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            return user.getLastName();
        } else {
            throw new UserNotFoundException(email);
        }
    }

    /**
     * Returns the full name of a user.
     *
     * @param email The email of the user.
     * @return The full name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @Override
    public String getUserFullName(String email) throws UserNotFoundException {
        Optional<User> maybeUser = userFinder.findUser(email);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            return user.getFullName();
        } else {
            throw new UserNotFoundException(email);
        }
    }
}
