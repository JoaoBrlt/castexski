package fr.unice.polytech.isa.shopping.components;

import fr.unice.polytech.isa.common.entities.shopping.catalog.*;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class CatalogBean implements CatalogModifier, CatalogExplorer {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void addPass(String name, double regularPrice, double childrenPrice, Duration duration, boolean isPrivateItem) throws ItemAlreadyExistException {
        PassCatalog passCatalog = new PassCatalog(name, regularPrice, childrenPrice, duration, isPrivateItem);
        if (findPass(name, duration).isPresent()) {
            throw new ItemAlreadyExistException(name);
        } else {
            entityManager.persist(passCatalog);
        }
    }

    @Override
    public void addCard(String name, boolean isSuperCartex, double price, boolean isPrivateItem) throws ItemAlreadyExistException {
        if (findCard(name, isSuperCartex).isPresent()) {
            throw new ItemAlreadyExistException(name);
        } else {
            entityManager.persist(new CardCatalog(name, price, isSuperCartex, isPrivateItem));
        }
    }

    @Override
    public void deletePass(String name, Duration duration) throws UnknownCatalogEntryException {
        Optional<PassCatalog> passCatalog = findPass(name, duration);
        if (passCatalog.isPresent()) {
            entityManager.remove(passCatalog.get());
        }
        else {
            throw new UnknownCatalogEntryException(name);
        }
    }

    @Override
    public void deleteCard(String name, boolean type) throws UnknownCatalogEntryException {
        Optional<CardCatalog> cardCatalog = findCard(name, type);
        if (cardCatalog.isPresent()) {
            entityManager.remove(cardCatalog.get());
        }
        else {
            throw new UnknownCatalogEntryException(name);
        }
    }

    @Override
    public Optional<CardCatalog> findCard(String name, boolean type) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CardCatalog> criteria = builder.createQuery(CardCatalog.class);
        Root<CardCatalog> root =  criteria.from(CardCatalog.class);
        criteria.select(root);
        criteria.where(builder.and(builder.equal(root.get("name"), name), builder.equal(root.get("isSuperCartex"), type)));
        TypedQuery<CardCatalog> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre){
            return Optional.empty();
        }
    }

    @Override
    public Optional<PassCatalog> findPass(String name, Duration duration) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PassCatalog> criteria = builder.createQuery(PassCatalog.class);
        Root<PassCatalog> root =  criteria.from(PassCatalog.class);
        criteria.where(builder.and(builder.equal(root.get("name"), name), builder.equal(root.get("maxDurationRaw"), duration.toString())));
        TypedQuery<PassCatalog> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre){
            return Optional.empty();
        }
    }

    @Override
    public Optional<ItemCatalog> findCatalogEntryById(int id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemCatalog> criteria = builder.createQuery(ItemCatalog.class);
        Root<ItemCatalog> root =  criteria.from(ItemCatalog.class);
        criteria.where(builder.equal(root.get("id"), id));
        TypedQuery<ItemCatalog> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre){
            return Optional.empty();
        }
    }

    @Override
    public Item pickCard(String name, boolean type, int quantity) throws UnknownCatalogEntryException, NullQuantityException {
        if (quantity > 0) {
            Optional<CardCatalog> cardCatalog = findCard(name, type);
            if (cardCatalog.isPresent()) {
                return new Item(new ItemType(name, cardCatalog.get().getPrice(), type ? ItemTypeName.SUPERCARTEX : ItemTypeName.CARD), quantity);
            } else {
                throw new UnknownCatalogEntryException(name);
            }
        } else {
            throw new NullQuantityException(name);
        }
    }

    @Override
    public Item pickPass(String name, Duration duration, boolean isChildren, int quantity) throws UnknownCatalogEntryException, NullQuantityException {
        if (quantity > 0) {
            Optional<PassCatalog> passCatalog = findPass(name, duration);
            if (passCatalog.isPresent()) {
                double price = isChildren ? passCatalog.get().getChildrenPrice() : passCatalog.get().getPrice();
                return new Item(new PassType(name, isChildren, duration, price), quantity);
            } else {
                throw new UnknownCatalogEntryException(name);
            }
        } else {
            throw new NullQuantityException();
        }
    }

    @Override
    public List<ItemCatalog> displayCatalog() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemCatalog> criteria = builder.createQuery(ItemCatalog.class);
        Root<ItemCatalog> root =  criteria.from(ItemCatalog.class);
        criteria.where(builder.equal(root.get("isPrivateItem"), false));
        TypedQuery<ItemCatalog> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<ItemCatalog> displayPrivateCatalog() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItemCatalog> criteria = builder.createQuery(ItemCatalog.class);
        Root<ItemCatalog> root =  criteria.from(ItemCatalog.class);
        criteria.where(builder.equal(root.get("isPrivateItem"), true));
        TypedQuery<ItemCatalog> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<PassCatalog> displayPassCatalog() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PassCatalog> criteria = builder.createQuery(PassCatalog.class);
        Root<PassCatalog> root =  criteria.from(PassCatalog.class);
        criteria.where(builder.equal(root.get("isPrivateItem"), false));
        TypedQuery<PassCatalog> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }


}
