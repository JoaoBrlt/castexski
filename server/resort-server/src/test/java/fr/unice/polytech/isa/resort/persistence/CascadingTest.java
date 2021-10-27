package fr.unice.polytech.isa.resort.persistence;

import fr.unice.polytech.isa.common.entities.resort.LiftAccess;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import fr.unice.polytech.isa.resort.components.ResortRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiLiftRegistryBean;
import fr.unice.polytech.isa.resort.components.SkiTrailRegistryBean;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiTrailFinder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CascadingTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction manual;

    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private SkiTrailFinder skiTrailFinder;

    @EJB
    private ResortFinder resortFinder;

    private Resort resort;
    private SkiLift skiLift;
    private LiftAccess liftAccess;
    private SkiTrail skiTrail;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Add this server's classes
                .addClass(ResortRegistryBean.class)
                .addClass(SkiLiftRegistryBean.class)
                .addClass(SkiTrailRegistryBean.class)
                .addClass(ResortNotFoundException.class)
                .addClass(SkiLiftNotFoundException.class)
                .addClass(SkiTrailNotFoundException.class)
                .addClass(ResortFinder.class)
                .addClass(SkiLiftFinder.class)
                .addClass(SkiTrailFinder.class)
                //Persistence manifest
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    @Before
    public void settingUp() {
        resort = new Resort(RESORT_NAME, true, RESORT_EMAIL, RESORT_CITY_NAME);
        skiLift = new SkiLift(true, resort);
        skiLift.setName(SKI_LIFT_NAME);
        liftAccess = new LiftAccess();
        liftAccess.setPassName(SEASON_PASS);
        skiTrail = new SkiTrail(SKI_TRAIL_NAME, OPEN);
    }

    @After
    public void cleaningUp() throws Exception {
        manual.begin();
        try {
            Resort resort = resortFinder.findByName(RESORT_NAME);
            resort = entityManager.merge(resort);
            entityManager.remove(resort);
        } catch (ResortNotFoundException e) {
            //Do nothing cause there is no need to cleanup
        }
        manual.commit();
    }

    /**
      SKI LIFTS
     */
    @Test
    public void skiLiftPersistedOnCascade() throws Exception {
        resort.addLift(skiLift);
        manual.begin();
            entityManager.persist(resort);
        manual.commit();
        assertNotNull(skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME));
        assertNotNull(resortFinder.findByName(RESORT_NAME));
    }

    @Test
    public void skiLiftPersistedOnCascadeAfterResortPersistedInATransaction() throws Exception {
        manual.begin();
            entityManager.persist(resort);
            resort.addLift(skiLift);
        manual.commit();
        assertNotNull(skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME));
        assertNotNull(resortFinder.findByName(RESORT_NAME));
    }

    @Test(expected = SkiLiftNotFoundException.class)
    public void skiLiftNotPersistedOnCascadeAfterResortPersistedOutsideATransaction() throws Exception {
        manual.begin();
            entityManager.persist(resort);
        manual.commit();
        resort.addLift(skiLift);
        assertNotNull(skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME));
        assertNotNull(resortFinder.findByName(RESORT_NAME));
    }

    @Test(expected = SkiLiftNotFoundException.class)
    public void skiLiftDeletedOnCascade() throws Exception {
        skiLiftPersistedOnCascade();
        manual.begin();
            resort = entityManager.merge(resort);
            entityManager.remove(resort);
        manual.commit();
        skiLiftFinder.findById(skiLift.getId());
    }

    /**
     SKI TRAILS
     */

    @Test
    public void skiTrailPersistedOnCascade() throws Exception {
        resort.addTrail(skiTrail);
        manual.begin();
            entityManager.persist(resort);
        manual.commit();
        assertNotNull(skiTrailFinder.findByName(resort.getId(), SKI_TRAIL_NAME));
        assertNotNull(resortFinder.findByName(RESORT_NAME));
    }

    @Test(expected = SkiTrailNotFoundException.class)
    public void skiTrailDeletedOnCascade() throws Exception {
        skiTrailPersistedOnCascade();
        manual.begin();
            resort = entityManager.merge(resort);
            entityManager.remove(resort);
        manual.commit();
        skiTrailFinder.findById(skiTrail.getId());
    }

    /**
     LIFT ACCESSES
     */

    @Test
    public void liftAccessPersistedOnCascade() throws Exception {
        skiLiftPersistedOnCascade();

        //Add the access
        liftAccess.addAllowedLiftId(skiLift.getId());
        resort.addAccess(liftAccess);

        //Add the access in the database
        manual.begin();
            resort.addAccess(liftAccess);
            entityManager.merge(resort);
        manual.commit();

        //See if the resort in the DB is correct
        Resort resortFound = resortFinder.findByName(RESORT_NAME);
        assertEquals(resortFound.getId(), resort.getId());
        manual.begin();
            resortFound = entityManager.merge(resortFound);
            assertNotNull(resortFound.getAccesses());
            assertFalse(resortFound.getAccesses().isEmpty());
        manual.commit();
    }
}
