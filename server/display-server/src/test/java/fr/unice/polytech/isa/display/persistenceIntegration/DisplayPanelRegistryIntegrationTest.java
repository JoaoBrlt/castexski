package fr.unice.polytech.isa.display.persistenceIntegration;

import arquillian.AbstractDisplayTest;
import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DisplayPanelRegistryIntegrationTest extends AbstractDisplayTest {
    @EJB
    ResortRegister resortRegister;

    @EJB
    ResortFinder resortFinder;

    @EJB
    DisplayFinder displayFinder;

    @EJB
    DisplayAccessor displayAccessor;

    @EJB
    DisplayRegister displayRegister;

    @EJB
    SkiTrailRegister skiTrailRegister;

    @EJB
    SkiTrailFinder skiTrailFinder;

    @After
    public void cleanUp() {
        try {
            resortRegister.deleteResort(resortFinder.findByName("ResortTestName").getId());
        } catch (ResortNotFoundException e) {
            //FOR THE TEST BELOW
        }
    }

    @Test(expected = ResortNotFoundException.class)
    public void createDisplayPanelWithoutResort() throws ResortNotFoundException, ExternalServiceException {
        displayRegister.addDisplayPanelToResort("savapamarcher");
    }

    @Test
    public void createDisplayPanelAndResort() throws ResortNotFoundException, UnavailableNameException, ExternalServiceException, DisplayPanelNotFoundException {
        String resortID = createAndGetResortId();
        String displayId = displayRegister.addDisplayPanelToResort(resortID);
        DisplayPanel displayPanel = displayFinder.findDisplayById(displayId);

        List<DisplayPanel> displayPanelsResort = displayFinder.findDisplaysByResort(resortID);
        assertTrue(displayPanelsResort.contains(displayPanel));

        //The line below will get the displayPanel from the external API (and an exception will be thrown if not found)
        displayAccessor.getDisplayedString(displayId);
    }

    @Test
    public void createDisplayPanelAndResortAndSkiTrail() throws ResortNotFoundException, UnavailableNameException, ExternalServiceException, DisplayPanelNotFoundException, SkiTrailNotFoundException {
        String resortID = createAndGetResortId();
        skiTrailRegister.registerSkiTrail(resortID, "SkiTrailTest", true);
        String skiTrailID = skiTrailFinder.findByName(resortID, "SkiTrailTest").getId();
        String displayId = displayRegister.addDisplayPanelToSkiTrail(resortID, skiTrailID);
        DisplayPanel displayPanel = displayFinder.findDisplayById(displayId);

        List<DisplayPanel> displayPanelsResort = displayFinder.findDisplaysByResort(resortID);
        List<DisplayPanel> displayPanelsSkiTrail = displayFinder.findDisplaysBySkiTrail(skiTrailID);
        assertTrue(displayPanelsResort.contains(displayPanel));
        assertTrue(displayPanelsSkiTrail.contains(displayPanel));

        //The line below will get the displayPanel from the external API (and an exception will be thrown if not found)
        displayAccessor.getDisplayedString(displayId);
    }

    private String createAndGetResortId() throws UnavailableNameException, ResortNotFoundException {
        resortRegister.registerResort("ResortTestName", "resort@email.com", true, "Resort City");
        return resortFinder.findByName("ResortTestName").getId();
    }
}
