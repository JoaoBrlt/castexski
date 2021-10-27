package fr.unice.polytech.isa.merchants.webservices;

import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Merchant service.
 * <p>
 * Registers and finds the merchants.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/merchant")
public interface MerchantService {
    /**
     * Indicates whether a merchant exists.
     *
     * @param email The email of the merchant.
     * @return <code>true</code> if the merchant exists; <code>false</code> otherwise.
     */
    @WebMethod
    boolean merchantExists(
        @WebParam(name="email") String email
    );

    /**
     * Registers a merchant.
     *
     * @param firstName The first name of the merchant.
     * @param lastName The last name of the merchant.
     * @param email The email of the merchant.
     * @param business The business of the merchant.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    @WebMethod
    void registerMerchant(
        @WebParam(name="firstName") String firstName,
        @WebParam(name="lastName") String lastName,
        @WebParam(name="email") String email,
        @WebParam(name="business") String business
    ) throws UnavailableEmailException;

    /**
     * Removes a merchant by email.
     *
     * @param email The email of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @WebMethod
    void removeMerchant(
        @WebParam(name="email") String email
    ) throws MerchantNotFoundException;

    /**
     * Subscribes the merchant to the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @WebMethod
    void subscribeMerchantToResort(
        @WebParam(name="email") String email,
        @WebParam(name="resortName") String resortName
    ) throws MerchantNotFoundException;

    /**
     * Unsubscribes the merchant from the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @WebMethod
    void unsubscribeMerchantFromResort(
        @WebParam(name="email") String email,
        @WebParam(name="resortName") String resortName
    ) throws MerchantNotFoundException;

    /**
     * Returns the business of a merchant.
     *
     * @param email The email of the merchant.
     * @return The business of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @WebMethod
    String getMerchantBusiness(
        @WebParam(name="email") String email
    ) throws MerchantNotFoundException;
}
