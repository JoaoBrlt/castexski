package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.OpennessChanger;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiTrailFinder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OpennessChangerBean implements OpennessChanger {
    @EJB
    ResortFinder resortFinder;

    @EJB
    SkiLiftFinder skiLiftFinder;

    @EJB
    SkiTrailFinder skiTrailFinder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void changeResortOpenness(String resortId, boolean isOpen) throws ResortNotFoundException {
        Resort r = resortFinder.findById(resortId);
        entityManager.merge(r);
        r.setOpen(isOpen);
        for(SkiLift sl : r.getLifts())
            sl.setOpen(isOpen);
        for(SkiTrail st : r.getTrails())
            st.setOpen(isOpen);
    }

    @Override
    public void changeSkiLiftOpenness(String skiLiftId, boolean isOpen) throws SkiLiftNotFoundException {
        skiLiftFinder.findById(skiLiftId).setOpen(isOpen);
    }

    @Override
    public void changeSkiTrailOpenness(String skiTrailId, boolean isOpen) throws SkiTrailNotFoundException {
        skiTrailFinder.findById(skiTrailId).setOpen(isOpen);
    }
}
