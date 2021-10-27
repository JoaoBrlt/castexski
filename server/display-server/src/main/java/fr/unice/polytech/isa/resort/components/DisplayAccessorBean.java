package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.external.DisplayService;
import fr.unice.polytech.isa.resort.interfaces.DisplayAccessor;
import fr.unice.polytech.isa.resort.interfaces.DisplayFinder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class DisplayAccessorBean implements DisplayAccessor {
    /**
     * The component logger.
     */
    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The external display service.
     */
    private DisplayService displayService;

    @EJB
    private DisplayFinder displayFinder;

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
    public String getDisplayedString(String displayPanelId) throws DisplayPanelNotFoundException, ExternalServiceException {
        DisplayPanel displayPanel = displayFinder.findDisplayById(displayPanelId);
        return displayService.getDisplayedText(displayPanel.getExternalId());
    }

    @Override
    public boolean getDisplayPanelState(String displayPanelId) throws DisplayPanelNotFoundException, ExternalServiceException {
        DisplayPanel displayPanel = displayFinder.findDisplayById(displayPanelId);
        return displayService.getDisplayPanelState(displayPanel.getExternalId());
    }

    @Override
    public void modifyDisplayedText(String displayPanelId, String message) throws DisplayPanelNotFoundException, ExternalServiceException {
        DisplayPanel displayPanel = displayFinder.findDisplayById(displayPanelId);
        displayService.modifyDisplayedText(displayPanel.getExternalId(), message);
    }
}
