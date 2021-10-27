package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.LiftAccess;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassCatalog;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.AccessRegister;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Stateless
public class AccessRegisterBean implements AccessRegister {
    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private CatalogExplorer catalogExplorer;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void addAccess(String passName, String resortId, String skiLiftId) throws ResortNotFoundException, SkiLiftNotFoundException, PassNotFoundException {
        //We check to see if the pass name provided is a real thing
        List<PassCatalog> passCatalogList = catalogExplorer.displayPassCatalog();
        Optional<PassCatalog> passCatalog = passCatalogList.stream().filter(pc -> pc.getName().equals(passName)).findFirst();
        if(!passCatalog.isPresent()) {
            throw new PassNotFoundException(passName);
        }

        //We check to see if the resort and skiLift provided exist
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);
        skiLiftFinder.findById(skiLiftId);

        //We finally add the access
        Optional<LiftAccess> liftAccess = r.getAccesses().stream().filter(a ->
                a.getPassName().equals(passName)
        ).findFirst();
        if (liftAccess.isPresent()){
            liftAccess.get().addAllowedLiftId(skiLiftId);
        } else {
            LiftAccess liftAccessToAdd = new LiftAccess();
            liftAccessToAdd.setPassName(passName);
            liftAccessToAdd.addAllowedLiftId(skiLiftId);
            r.addAccess(liftAccessToAdd);
        }
    }

    @Override
    public void removeAccess(String passName, String resortId) throws ResortNotFoundException, PassNotFoundException {
        //We check to see if the resort provided exist
        Resort r = resortFinder.findById(resortId);
        manager.merge(r);

        //We finally remove the access
        Optional<LiftAccess> liftAccess = r.getAccesses().stream().filter(a ->
                a.getPassName().equals(passName)
        ).findFirst();
        if (liftAccess.isPresent()){
            LiftAccess la = liftAccess.get();
            r.removeAccess(la);
            manager.remove(la);
        } else {
            throw new PassNotFoundException(passName);
        }
    }
}
