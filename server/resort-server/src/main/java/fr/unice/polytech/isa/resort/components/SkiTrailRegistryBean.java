package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiTrailFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiTrailRegister;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class SkiTrailRegistryBean implements SkiTrailRegister, SkiTrailFinder {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB
    private ResortFinder resortFinder;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public SkiTrail findByName(String resortId, String name) throws ResortNotFoundException, SkiTrailNotFoundException {
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);

        Optional<SkiTrail> skiTrail = r.getTrails().stream().filter(t ->
                t.getName().equals(name)
        ).findFirst();
        try {
            return skiTrail.get();
        } catch (NoSuchElementException nsee) {
            log.log(Level.FINEST, "No result for [" + name + "]", nsee);
            throw new SkiTrailNotFoundException(name);
        }
    }

    @Override
    public SkiTrail findById(String id) throws SkiTrailNotFoundException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<SkiTrail> criteria = builder.createQuery(SkiTrail.class);
        Root<SkiTrail> root = criteria.from(SkiTrail.class);
        criteria.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<SkiTrail> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            log.log(Level.FINEST, "No result for [" + id + "]", nre);
            throw new SkiTrailNotFoundException(id + "");
        }
    }

    @Override
    public void registerSkiTrail(String resortId, String name, boolean isOpen) throws ResortNotFoundException, UnavailableNameException {
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);
        //If the line above throws an exception, we can't create the ski trail, cause we can't add it to its corresponding resort
        try {
            findByName(resortId, name);
            throw new UnavailableNameException(name);
        } catch (SkiTrailNotFoundException e) {
            SkiTrail s = new SkiTrail();
            s.setOpen(isOpen);
            s.setName(name);
            manager.persist(s);
            r.addTrail(s);
        }
    }

    @Override
    public void deleteSkiTrail(String resortId, String id) throws SkiTrailNotFoundException, ResortNotFoundException {
        SkiTrail s = findById(id);

        Resort resort = resortFinder.findById(resortId);
        manager.merge(resort);

        resort.removeTrail(s);
        manager.remove(s);
    }
}
