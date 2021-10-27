package fr.unice.polytech.isa.display.business;

import arquillian.AbstractDisplayTest;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.DisplayAccessor;
import fr.unice.polytech.isa.resort.interfaces.DisplayRegister;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.ResortRegister;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DisplayAccessorIntegrationTest extends AbstractDisplayTest {
    @EJB
    ResortRegister resortRegister;

    @EJB
    ResortFinder resortFinder;

    @EJB
    DisplayAccessor displayAccessor;

    @EJB
    DisplayRegister displayRegister;

    @After
    public void cleanUp() throws ResortNotFoundException {
        resortRegister.deleteResort(resortFinder.findByName("ResortTestName").getId());
    }

    @Test
    public void changeDisplayedText() throws ExternalServiceException, DisplayPanelNotFoundException, ResortNotFoundException, UnavailableNameException {
        String displayPanelId = createAndGetDisplayPanel();
        displayAccessor.modifyDisplayedText(displayPanelId, "hello");
        assertEquals("hello", displayAccessor.getDisplayedString(displayPanelId));
    }

    @Test
    public void turnOnADisplayPanel() throws ExternalServiceException, DisplayPanelNotFoundException, ResortNotFoundException, UnavailableNameException {
        String displayPanelId = createAndGetDisplayPanel();
        assertFalse(displayAccessor.getDisplayPanelState(displayPanelId));
        displayAccessor.modifyDisplayedText(displayPanelId, "hello");
        assertTrue(displayAccessor.getDisplayPanelState(displayPanelId));
    }

    @Test
    public void turnOffADisplayPanel() throws ExternalServiceException, DisplayPanelNotFoundException, ResortNotFoundException, UnavailableNameException {
        String displayPanelId = createAndGetDisplayPanel();
        displayAccessor.modifyDisplayedText(displayPanelId, "hello");
        assertTrue(displayAccessor.getDisplayPanelState(displayPanelId));
        displayAccessor.modifyDisplayedText(displayPanelId, "");
        assertFalse(displayAccessor.getDisplayPanelState(displayPanelId));
    }

    private String createAndGetDisplayPanel() throws UnavailableNameException, ResortNotFoundException, ExternalServiceException {
        resortRegister.registerResort("ResortTestName", "resort@email.com", true, "Resort City");
        return displayRegister.addDisplayPanelToResort(resortFinder.findByName("ResortTestName").getId());
    }
}
