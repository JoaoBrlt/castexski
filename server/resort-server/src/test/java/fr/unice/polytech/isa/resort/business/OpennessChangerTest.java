package fr.unice.polytech.isa.resort.business;

import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.notifications.components.NotificationProcessorBean;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.components.NotificationSchedulerBean;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationFinder;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;
import fr.unice.polytech.isa.resort.components.OpennessChangerBean;
import fr.unice.polytech.isa.resort.components.ResortRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiLiftRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiTrailRegistryBean;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.*;
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

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class OpennessChangerTest {
    @EJB
    private OpennessChanger opennessChanger;

    @EJB
    private ResortRegister resortRegister;
    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiLiftRegister skiLiftRegister;
    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private SkiTrailRegister skiTrailRegister;
    @EJB
    private SkiTrailFinder skiTrailFinder;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Add this server's classes
                .addClass(OpennessChangerBean.class)
                .addClass(ResortRegistryBean.class)
                .addClass(SkiLiftRegistryBean.class)
                .addClass(SkiTrailRegistryBean.class)
                .addClass(ResortNotFoundException.class)
                .addClass(SkiLiftNotFoundException.class)
                .addClass(SkiTrailNotFoundException.class)
                .addClass(UnavailableNameException.class)
                .addClass(OpennessChanger.class)
                .addClass(ResortFinder.class)
                .addClass(ResortRegister.class)
                .addClass(SkiLiftFinder.class)
                .addClass(SkiLiftRegister.class)
                .addClass(SkiTrailFinder.class)
                .addClass(SkiTrailRegister.class)
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
    public void cleaningUp() {
        try {
            resortRegister.deleteResort(getResortId());
        } catch (Exception e) {
            //Shouldn't happen
        }
    }

    @Test
    public void openingAResort() throws Exception {
        setUpCompleteResort();
        assertFalse(resortFinder.findById(getResortId()).isOpen());
        assertFalse(skiLiftFinder.findById(getSkiLiftId()).isOpen());
        assertFalse(skiTrailFinder.findById(getSkiTrailId()).isOpen());
        opennessChanger.changeResortOpenness(getResortId(), OPEN);
        assertTrue(resortFinder.findById(getResortId()).isOpen());
        assertTrue(skiLiftFinder.findById(getSkiLiftId()).isOpen());
        assertTrue(skiTrailFinder.findById(getSkiTrailId()).isOpen());
    }

    @Test
    public void closingAResort() throws Exception {
        openingAResort();
        opennessChanger.changeResortOpenness(getResortId(), CLOSE);
        assertFalse(resortFinder.findById(getResortId()).isOpen());
        assertFalse(skiLiftFinder.findById(getSkiLiftId()).isOpen());
        assertFalse(skiTrailFinder.findById(getSkiTrailId()).isOpen());
    }

    private void setUpCompleteResort() throws Exception {
        resortRegister.registerResort(RESORT_NAME, RESORT_EMAIL, CLOSE, RESORT_CITY_NAME);
        String resortId = getResortId();
        skiLiftRegister.registerSkiLift(resortId, SKI_LIFT_NAME, CLOSE);
        skiTrailRegister.registerSkiTrail(resortId, SKI_TRAIL_NAME, CLOSE);
    }

    private String getResortId() throws Exception {
        return resortFinder.findByName(RESORT_NAME).getId();
    }

    private String getSkiTrailId() throws Exception {
        return skiTrailFinder.findByName(getResortId(), SKI_TRAIL_NAME).getId();
    }

    private String getSkiLiftId() throws Exception {
        return skiLiftFinder.findByName(getResortId(), SKI_LIFT_NAME).getId();
    }
}
