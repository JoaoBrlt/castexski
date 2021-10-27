package fr.unice.polytech.isa.accounts.components;

import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.accounts.User;

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
 * User finder bean.
 * <p>
 * Finds the users.
 * </p>
 */
@Stateless
public class UserFinderBean implements UserFinder {
    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns all the users.
     *
     * @return The list of users.
     */
    @Override
    public List<User> getUsers() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        CriteriaQuery<User> all = criteria.select(root);
        TypedQuery<User> query = entityManager.createQuery(all);
        return query.getResultList();
    }

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The requested user.
     */
    @Override
    public Optional<User> findUser(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root).where(builder.equal(root.get("email"), email));
        TypedQuery<User> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException error) {
            return Optional.empty();
        }
    }
}
