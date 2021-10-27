package fr.unice.polytech.isa.merchants.interfaces;

import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;

import javax.ejb.Local;

/**
 * Merchant subscription.
 * <p>
 * Registers the merchants.
 * </p>
 */
@Local
public interface MerchantSubscription {
    /**
     * Registers a merchant.
     *
     * @param merchant The merchant to be registered.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    void registerMerchant(Merchant merchant) throws UnavailableEmailException;

    /**
     * Removes a merchant by email.
     *
     * @param email The email of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    void removeMerchant(String email) throws MerchantNotFoundException;

    /**
     * Subscribes the merchant to the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    void subscribeMerchantToResort(String email, String resortName) throws MerchantNotFoundException;

    /**
     * Unsubscribes the merchant from the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    void unsubscribeMerchantFromResort(String email, String resortName) throws MerchantNotFoundException;
}
