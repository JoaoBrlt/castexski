package fr.unice.polytech.isa.resort.persistence;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class StorageTest {
    @PersistenceContext
    private EntityManager entityManager;

    /*******************************
     ** Regular persistence cases **
     *******************************/

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Persistence manifest
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    @Test
    public void storingResort() {
        Resort r = new Resort(RESORT_NAME, true, RESORT_EMAIL, RESORT_CITY_NAME);
        assertNull(r.getId());

        entityManager.persist(r);
        String id = r.getId();
        assertNotNull(id);

        Resort stored = entityManager.find(Resort.class, id);
        assertEquals(r, stored);
    }

    @Test
    public void storingSkiLift() {
        Resort r = new Resort(RESORT_NAME, true, RESORT_EMAIL, RESORT_CITY_NAME);
        entityManager.persist(r);

        SkiLift skiLift = new SkiLift(true, r);
        assertNull(skiLift.getId());

        entityManager.persist(skiLift);
        String id = skiLift.getId();
        assertNotNull(id);

        SkiLift stored = entityManager.find(SkiLift.class, id);
        assertEquals(r, stored.getResort());
        assertEquals(skiLift, stored);
    }

    @Test
    public void updatingResortWithSkiLift() {
        Resort resort = new Resort(RESORT_NAME, true, RESORT_EMAIL, RESORT_CITY_NAME);
        entityManager.persist(resort);

        Resort storedResort = entityManager.find(Resort.class, resort.getId());
        assertEquals(0, storedResort.getLifts().size());

        SkiLift skiLift = new SkiLift(true, resort);
        entityManager.persist(skiLift);

        resort.addLift(skiLift);

        // the persistence context is shared => modifications are propagated
        assertEquals(1, resort.getLifts().size());
        assertEquals(1, storedResort.getLifts().size());
        assertEquals(resort.getLifts(), storedResort.getLifts());

        SkiLift storedSkiLift = entityManager.find(SkiLift.class, skiLift.getId());
        assertEquals(skiLift, storedSkiLift);
    }

    @Test
    public void removingSkiLiftInResort() {
        Resort resort = new Resort(RESORT_NAME, OPEN, RESORT_EMAIL, RESORT_CITY_NAME);
        entityManager.persist(resort);
        SkiLift skiLift1 = new SkiLift(OPEN, resort);
        skiLift1.setName("TestSkiLift1");
        entityManager.persist(skiLift1);
        resort.addLift(skiLift1);

        SkiLift skiLift2 = new SkiLift(OPEN, resort);
        skiLift2.setName("TestSkiLift2");
        entityManager.persist(skiLift2);
        resort.addLift(skiLift2);

        resort.getLifts().remove(skiLift1);
        entityManager.remove(skiLift1);

        assertNull(entityManager.find(SkiLift.class, skiLift1.getId()));
        assertNotNull(entityManager.find(SkiLift.class, skiLift2.getId()));
        assertEquals(1, resort.getLifts().size());
        assertEquals(skiLift2, resort.getLifts().toArray()[0]);
        assertEquals(resort, entityManager.find(Resort.class, resort.getId()));
    }

    @Test
    public void storingSkiTrail() {
        SkiTrail skiTrail = new SkiTrail(SKI_TRAIL_NAME, OPEN);
        assertNull(skiTrail.getId());
        entityManager.persist(skiTrail);
        assertNotNull(skiTrail.getId());
    }

    @Test
    public void removingSkiTrail() {
        Resort resort = new Resort(RESORT_NAME, OPEN, RESORT_EMAIL, RESORT_CITY_NAME);
        entityManager.persist(resort);

        SkiTrail skiTrail = new SkiTrail(SKI_TRAIL_NAME, OPEN);
        entityManager.persist(skiTrail);
        resort.addTrail(skiTrail);

        assertEquals(1, resort.getTrails().size());

        entityManager.remove(skiTrail);

        //Still in the resort cause we don't used EJB that do the work for us >:(
        assertEquals(1, resort.getTrails().size());
    }
}
