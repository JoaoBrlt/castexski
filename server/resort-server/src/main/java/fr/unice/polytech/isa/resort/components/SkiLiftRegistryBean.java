package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftRegister;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class SkiLiftRegistryBean implements SkiLiftFinder, SkiLiftRegister {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB
    private ResortFinder resortFinder;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public SkiLift findByName(String resortId, String name) throws ResortNotFoundException, SkiLiftNotFoundException {
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);

        Optional<SkiLift> skiLift = r.getLifts().stream().filter(l ->
                l.getName().equals(name)
        ).findFirst();
        try {
            return skiLift.get();
        } catch (NoSuchElementException nsee) {
            log.log(Level.FINEST, "No result for [" + name + "]", nsee);
            throw new SkiLiftNotFoundException(name);
        }
    }

    @Override
    public SkiLift findById(String id) throws SkiLiftNotFoundException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<SkiLift> criteria = builder.createQuery(SkiLift.class);
        Root<SkiLift> root = criteria.from(SkiLift.class);
        criteria.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<SkiLift> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            log.log(Level.FINEST, "No result for [" + id + "]", nre);
            throw new SkiLiftNotFoundException(id + "");
        }
    }

    @Override
    public void registerSkiLift(String resortId, String name, boolean isOpen) throws UnavailableNameException, ResortNotFoundException {
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);
        //If the line above throws an exception, we can't create the skiLift, cause we can't add it to its corresponding resort
        try {
            findByName(resortId, name);
            throw new UnavailableNameException(name);
        } catch (SkiLiftNotFoundException e) {
            SkiLift s = new SkiLift();
            s.setOpen(isOpen);
            s.setName(name);
            s.setResort(r);
            manager.persist(s);
            r.addLift(s);
        }
    }

    @Override
    public void registerDoubleSkiLift(String resortId, String name, boolean isOpen, String timeLimit) throws ResortNotFoundException {
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);
        //If the line above throws an exception, we can't create the skiLift, cause we can't add it to its corresponding resort
        try {
            findByName(resortId, name);
            throw new UnavailableNameException(name);
        } catch (SkiLiftNotFoundException | UnavailableNameException e) {
            SkiLift s = new DoubleSkiLift(r, Duration.parse(timeLimit), isOpen, name);
            manager.persist(s);
            r.addLift(s);
        }
    }

    @Override
    public void deleteSkiLift(String id) throws SkiLiftNotFoundException {
        SkiLift s = findById(id);
        manager.merge(s);
        Resort r = s.getResort();
        manager.merge(r);
        r.removeLift(s);
        manager.remove(s);
    }
}
