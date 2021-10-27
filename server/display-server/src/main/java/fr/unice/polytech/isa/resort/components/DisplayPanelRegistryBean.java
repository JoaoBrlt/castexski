package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.external.DisplayService;
import fr.unice.polytech.isa.resort.interfaces.DisplayFinder;
import fr.unice.polytech.isa.resort.interfaces.DisplayRegister;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiTrailFinder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class DisplayPanelRegistryBean implements DisplayRegister, DisplayFinder {

    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiTrailFinder skiTrailFinder;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The external display service.
     */
    private DisplayService displayService;

    /**
     * Initializes the display service.
     */
    @PostConstruct
    private void initializeDisplayService() {
        // Build the email service client.
        displayService = new DisplayService(
            System.getenv().getOrDefault("DISPLAY_HOST", "localhost"),
            System.getenv().getOrDefault("DISPLAY_PORT", "9191")
        );
    }

    @Override
    public DisplayPanel findDisplayById(String displayPanelId) throws DisplayPanelNotFoundException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DisplayPanel> criteria = builder.createQuery(DisplayPanel.class);
        Root<DisplayPanel> root = criteria.from(DisplayPanel.class);
        criteria.select(root).where(builder.equal(root.get("id"), displayPanelId));
        TypedQuery<DisplayPanel> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            logger.log(Level.FINEST, "No result for [" + displayPanelId + "]", nre);
            throw new DisplayPanelNotFoundException(displayPanelId + "");
        }
    }

    @Override
    public List<DisplayPanel> findDisplaysByResort(String resortId) throws ResortNotFoundException {
        Resort r = resortFinder.findById(resortId);
        entityManager.merge(r);
        return new ArrayList<>(r.getDisplayPanels());
    }

    @Override
    public List<DisplayPanel> findDisplaysBySkiTrail(String skiTrailId) throws SkiTrailNotFoundException {
        SkiTrail skiTrail = skiTrailFinder.findById(skiTrailId);
        entityManager.merge(skiTrail);
        return new ArrayList<>(skiTrail.getDisplayPanels());
    }

    @Override
    public String addDisplayPanelToResort(String resortId) throws ResortNotFoundException, ExternalServiceException {
        //Create the new display panel
        DisplayPanel displayPanel = new DisplayPanel();
        entityManager.persist(displayPanel);

        //Add it to the resort
        Resort resort = resortFinder.findById(resortId);
        entityManager.merge(resort);
        resort.addDisplayPanel(displayPanel);

        //Finally add it to the external service
        displayService.createDisplayPanel(displayPanel);

        return displayPanel.getId();
    }

    @Override
    public String addDisplayPanelToSkiTrail(String resortId, String skiTrailId) throws ResortNotFoundException, SkiTrailNotFoundException, ExternalServiceException {
        //Create the new display panel
        DisplayPanel displayPanel = new DisplayPanel();
        entityManager.persist(displayPanel);

        //Add it to the ski trail
        SkiTrail skiTrail = skiTrailFinder.findById(skiTrailId);
        entityManager.merge(skiTrail);
        skiTrail.addDisplayPanel(displayPanel);

        //Also add it to the resort (because the resort stores all display panels)
        Resort resort = resortFinder.findById(resortId);
        entityManager.merge(resort);
        resort.addDisplayPanel(displayPanel);

        //Finally add it to the external service
        displayService.createDisplayPanel(displayPanel);

        return displayPanel.getId();
    }

    @Override
    public void deleteDisplayPanel(String resortId, String displayPanelId) throws ResortNotFoundException, DisplayPanelNotFoundException {
        //Find the resort (to delete the DisplayPanel from it)
        Resort resort = resortFinder.findById(resortId);
        entityManager.merge(resort);

        //Find the display to further delete it
        DisplayPanel displayPanel = findDisplayById(displayPanelId);

        //Delete the display panel from the resort
        resort.removeDisplayPanel(displayPanel);

        //Delete the display panel from the resort
        Set<SkiTrail> skiTrails = resort.getTrails();
        for(SkiTrail skiTrail : skiTrails)
            skiTrail.removeDisplayPanel(displayPanel);

        //Finally delete from the persistence
        entityManager.remove(displayPanel);
    }
}
