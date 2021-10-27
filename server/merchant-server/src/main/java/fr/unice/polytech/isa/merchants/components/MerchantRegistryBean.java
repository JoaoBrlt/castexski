package fr.unice.polytech.isa.merchants.components;

import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.common.entities.accounts.User;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.merchants.interfaces.MerchantSubscription;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Merchant registry bean.
 * <p>
 * Registers and finds the merchants.
 * </p>
 */
@Stateless
public class MerchantRegistryBean implements MerchantSubscription, MerchantFinder {
    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The user finder.
     */
    @EJB
    private UserFinder userFinder;


    /**
     * Registers a merchant.
     *
     * @param merchant The merchant to be registered.
     * @throws UnavailableEmailException if the email address is unavailable.
     */
    @Override
    public void registerMerchant(Merchant merchant) throws UnavailableEmailException {
        // Check the email availability.
        Optional<User> maybeUser = userFinder.findUser(merchant.getEmail());
        if (!maybeUser.isPresent()) {
            // Save the merchant.
            entityManager.persist(merchant);
        } else {
            throw new UnavailableEmailException(merchant.getEmail());
        }
    }

    /**
     * Removes a merchant by email.
     *
     * @param email The email of the merchant.
     * @throws MerchantNotFoundException if the merchant was not found.
     */
    @Override
    public void removeMerchant(String email) throws MerchantNotFoundException {
        // Find the merchant.
        Optional<Merchant> maybeMerchant = findMerchant(email);
        if (maybeMerchant.isPresent()) {
            // Remove the merchant.
            Merchant merchant = maybeMerchant.get();
            entityManager.remove(merchant);
        } else {
            throw new MerchantNotFoundException(email);
        }
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
        // Find the merchant and resort.
        Optional<Merchant> maybeMerchant = findMerchant(email);

        if (maybeMerchant.isPresent()) {
            // Get the merchant.
            Merchant merchant = maybeMerchant.get();
            merchant = entityManager.merge(merchant);

            // Add the resort.
            merchant.addResort(resortName);

            // Update the merchant.
            entityManager.merge(merchant);
        } else {
            throw new MerchantNotFoundException(email);
        }
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
        // Find the merchant and resort.
        Optional<Merchant> maybeMerchant = findMerchant(email);

        if (maybeMerchant.isPresent()) {
            // Get the merchant.
            Merchant merchant = maybeMerchant.get();
            merchant = entityManager.merge(merchant);

            // Remove the resort.
            merchant.removeResort(resortName);

            // Update the merchant.
            entityManager.persist(merchant);
        } else {
            throw new MerchantNotFoundException(email);
        }
    }

    /**
     * Returns all the merchants.
     *
     * @return The list of merchants.
     */
    @Override
    public List<Merchant> getMerchants() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merchant> criteria = builder.createQuery(Merchant.class);
        Root<Merchant> root = criteria.from(Merchant.class);
        CriteriaQuery<Merchant> all = criteria.select(root);
        TypedQuery<Merchant> query = entityManager.createQuery(all);
        return query.getResultList();
    }

    /**
     * Finds a merchant by email.
     *
     * @param email The email of the merchant.
     * @return The requested merchant.
     */
    @Override
    public Optional<Merchant> findMerchant(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merchant> criteria = builder.createQuery(Merchant.class);
        Root<Merchant> root = criteria.from(Merchant.class);
        criteria.select(root).where(builder.equal(root.get("email"), email));
        TypedQuery<Merchant> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException error) {
            return Optional.empty();
        }
    }

    /**
     * Finds a merchant by business.
     *
     * @param business The business of the merchant.
     * @return The requested merchant.
     */
    @Override
    public Optional<Merchant> findMerchantByBusiness(String business) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merchant> criteria = builder.createQuery(Merchant.class);
        Root<Merchant> root = criteria.from(Merchant.class);
        criteria.select(root).where(builder.equal(root.get("business"), business));
        TypedQuery<Merchant> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException error) {
            return Optional.empty();
        }
    }
}
