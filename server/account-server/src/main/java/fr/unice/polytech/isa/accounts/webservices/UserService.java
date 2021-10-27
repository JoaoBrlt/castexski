package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.UserNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * User service.
 * <p>
 * Finds the users.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/user")
public interface UserService {
    /**
     * Indicates whether a user exists.
     *
     * @param email The email of the user.
     * @return <code>true</code> if the user exists; <code>false</code> otherwise.
     */
    @WebMethod
    boolean userExists(
        @WebParam(name="email") String email
    );

    /**
     * Returns the type of a user.
     *
     * @param email The email of the user.
     * @return The type of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @WebMethod
    String getUserType(
        @WebParam(name="email") String email
    ) throws UserNotFoundException;

    /**
     * Returns the first name of a user.
     *
     * @param email The email of the user.
     * @return The first name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @WebMethod
    String getUserFirstName(
        @WebParam(name="email") String email
    ) throws UserNotFoundException;

    /**
     * Returns the last name of a user.
     *
     * @param email The email of the user.
     * @return The last name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @WebMethod
    String getUserLastName(
        @WebParam(name="email") String email
    ) throws UserNotFoundException;

    /**
     * Returns the full name of a user.
     *
     * @param email The email of the user.
     * @return The full name of the user.
     * @throws UserNotFoundException if the user was not found.
     */
    @WebMethod
    String getUserFullName(
        @WebParam(name="email") String email
    ) throws UserNotFoundException;
}
