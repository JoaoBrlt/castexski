package fr.unice.polytech.isa.resort.business;

import fr.unice.polytech.isa.common.entities.resort.LiftAccess;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.notifications.components.NotificationProcessorBean;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.components.NotificationSchedulerBean;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;
import fr.unice.polytech.isa.resort.components.AccessRegisterBean;
import fr.unice.polytech.isa.resort.components.ResortRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiLiftRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiTrailRegistryBean;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.*;
import fr.unice.polytech.isa.shopping.components.CatalogBean;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.time.Duration;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.apache.webbeans.util.Asserts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class AccessRegisterTest {
    @EJB
    private AccessRegister accessRegister;

    @EJB
    private SkiLiftRegister skiLiftRegister;
    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private ResortRegister resortRegister;
    @EJB
    private ResortFinder resortFinder;

    @EJB
    private CatalogModifier catalogModifier;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            //Add this server's classes
            .addClass(AccessRegisterBean.class)
            .addClass(ResortRegistryBean.class)
            .addClass(SkiLiftRegistryBean.class)
            .addClass(SkiTrailRegistryBean.class)
            .addClass(ResortNotFoundException.class)
            .addClass(SkiLiftNotFoundException.class)
            .addClass(SkiTrailNotFoundException.class)
            .addClass(UnavailableNameException.class)
            .addClass(AccessRegister.class)
            .addClass(ResortFinder.class)
            .addClass(ResortRegister.class)
            .addClass(SkiLiftFinder.class)
            .addClass(SkiLiftRegister.class)
            .addClass(SkiTrailFinder.class)
            .addClass(SkiTrailRegister.class)
            //Add the shopping server's classes
            .addClass(CatalogModifier.class)
            .addClass(CatalogBean.class)
            //Add the notification server's classes
            .addClass(NotificationRegistration.class)
            .addClass(NotificationRegistryBean.class)
            .addClass(NotificationScheduling.class)
            .addClass(NotificationSchedulerBean.class)
            .addClass(NotificationProcessing.class)
            .addClass(NotificationProcessorBean.class)
            //Persistence manifest
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    @After
    public void cleanUp() {
        try {
            resortRegister.deleteResort(getResortId());
        } catch (ResortNotFoundException e) {
            //There is nothing to delete
        }
        try {
            catalogModifier.deletePass(DISCOVERY_PASS, Duration.ZERO);
        } catch (UnknownCatalogEntryException e) {
            //There is nothing to delete
        }
    }

    @Test
    public void successfullyAddingAccess() throws Exception {
        settingUpPass();
        settingUpResort();
        String resortId = getResortId();
        settingUpSkiLift(resortId);
        String skiLiftId = getSkiLiftId(resortId);
        accessRegister.addAccess(DISCOVERY_PASS, resortId, skiLiftId);

        Resort resortFound = resortFinder.findByName(RESORT_NAME);
        assertNotNull(resortFound.getAccesses());
        assertFalse(resortFound.getAccesses().isEmpty());
        LiftAccess liftAccess = (LiftAccess) resortFound.getAccesses().toArray()[0];
        assertEquals(liftAccess.getPassName(), DISCOVERY_PASS);
        assertNotNull(liftAccess.getAllowedLiftsIds());
        assertFalse(liftAccess.getAllowedLiftsIds().isEmpty());
        assertEquals(liftAccess.getAllowedLiftsIds().toArray()[0], skiLiftId);
    }

    @Test(expected = PassNotFoundException.class)
    public void addingAccessWithoutPassExisting() throws Exception {
        settingUpResort();
        String resortId = getResortId();
        settingUpSkiLift(resortId);
        String skiLiftId = getSkiLiftId(resortId);
        accessRegister.addAccess(DISCOVERY_PASS, resortId, skiLiftId);
    }

    @Test(expected = ResortNotFoundException.class)
    public void addingAccessWithoutResortExisting() throws Exception {
        settingUpPass();
        accessRegister.addAccess(DISCOVERY_PASS, BAD_RESORT_ID, BAD_SKI_LIFT_ID);
    }

    @Test(expected = SkiLiftNotFoundException.class)
    public void addingAccessWithoutLiftExisting() throws Exception {
        settingUpPass();
        settingUpResort();
        String resortId = getResortId();
        accessRegister.addAccess(DISCOVERY_PASS, resortId, BAD_SKI_LIFT_ID);
    }

    @Test
    public void successfullyRemovingALiftAccess() throws Exception {
        successfullyAddingAccess();
        accessRegister.removeAccess(DISCOVERY_PASS, getResortId());
    }

    @Test(expected = PassNotFoundException.class)
    public void tryingToRemoveANonExistingLiftAccess() throws Exception {
        settingUpResort();
        accessRegister.removeAccess(DISCOVERY_PASS, getResortId());
    }

    private void settingUpPass() throws ItemAlreadyExistException {
        catalogModifier.addPass(DISCOVERY_PASS, 0.00, 0.00, Duration.ZERO, false);
    }

    private void settingUpResort() throws UnavailableNameException {
        resortRegister.registerResort(RESORT_NAME, RESORT_EMAIL, true, RESORT_CITY_NAME);
    }

    private String getResortId() throws ResortNotFoundException {
        return resortFinder.findByName(RESORT_NAME).getId();
    }

    private void settingUpSkiLift(String resortId) throws ResortNotFoundException, UnavailableNameException {
        skiLiftRegister.registerSkiLift(resortId, SKI_LIFT_NAME, true);
    }

    private String getSkiLiftId(String resortId) throws SkiLiftNotFoundException, ResortNotFoundException {
        return skiLiftFinder.findByName(resortId, SKI_LIFT_NAME).getId();
    }
}
