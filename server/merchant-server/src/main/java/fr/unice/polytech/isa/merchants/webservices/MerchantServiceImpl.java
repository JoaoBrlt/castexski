package fr.unice.polytech.isa.merchants.webservices;

import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.merchants.interfaces.MerchantSubscription;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Optional;

/**
 * Merchant service implementation.
 * <p>
 * Registers and finds the merchants.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/merchant")
@Stateless(name = "MerchantWS")
public class MerchantServiceImpl implements MerchantService {
    /**
     * The merchant finder.
     */
    @EJB
    private MerchantFinder merchantFinder;

    /**
     * The merchant subscriber.
     */
    @EJB
    private MerchantSubscription merchantSubscriber;

    /**
     * Indicates whether a merchant exists.
     *
     * @param email The email of the merchant.
     * @return <code>true</code> if the merchant exists; <code>false</code> otherwise.
     */
    @Override
    public boolean merchantExists(String email) {
        return merchantFinder.findMerchant(email).isPresent();
    }

    /**
     * Registers a merchant.
     *
     * @param firstName The first name of the merchant.
     * @param lastName The last name of the merchant.
     * @param email The email of the merchant.
     * @param business The business of the merchant.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    @Override
    public void registerMerchant(String firstName, String lastName, String email, String business) throws UnavailableEmailException {
        merchantSubscriber.registerMerchant(new Merchant(
            firstName,
            lastName,
            email,
            business
        ));
    }

    /**
     * Removes a merchant by email.
     *
     * @param email The email of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @Override
    public void removeMerchant(String email) throws MerchantNotFoundException {
        merchantSubscriber.removeMerchant(email);
    }

    /**
     * Subscribes the merchant to the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @Override
    public void subscribeMerchantToResort(String email, String resortName) throws MerchantNotFoundException {
        merchantSubscriber.subscribeMerchantToResort(email, resortName);
    }

    /**
     * Unsubscribes the merchant from the statistics of a resort.
     *
     * @param email The email of the merchant.
     * @param resortName The name of the resort.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @Override
    public void unsubscribeMerchantFromResort(String email, String resortName) throws MerchantNotFoundException {
        merchantSubscriber.unsubscribeMerchantFromResort(email, resortName);
    }

    /**
     * Returns the business of a merchant.
     *
     * @param email The email of the merchant.
     * @return The business of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @Override
    public String getMerchantBusiness(String email) throws MerchantNotFoundException {
        Optional<Merchant> maybeMerchant = merchantFinder.findMerchant(email);
        if (maybeMerchant.isPresent()) {
            Merchant merchant = maybeMerchant.get();
            return merchant.getBusiness();
        } else {
            throw new MerchantNotFoundException(email);
        }
    }
}
