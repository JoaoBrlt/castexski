package fr.unice.polytech.isa.merchants.interfaces;

import fr.unice.polytech.isa.common.entities.accounts.Merchant;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

/**
 * Merchant finder.
 * <p>
 * Searches for merchants according to various criteria.
 * </p>
 */
@Local
public interface MerchantFinder {
    /**
     * Returns all the merchants.
     *
     * @return The list of merchants.
     */
    List<Merchant> getMerchants();

    /**
     * Finds a merchant by email.
     *
     * @param email The email of the merchant.
     * @return The requested merchant.
     */
    Optional<Merchant> findMerchant(String email);

    /**
     * Finds a merchant by business.
     *
     * @param business The business of the merchant.
     * @return The requested merchant.
     */
    Optional<Merchant> findMerchantByBusiness(String business);
}
