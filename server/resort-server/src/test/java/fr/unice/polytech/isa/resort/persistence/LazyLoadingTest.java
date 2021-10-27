package fr.unice.polytech.isa.resort.persistence;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class LazyLoadingTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction manual;

    /********************************
     ** Lazy loading demonstration **
     ********************************/

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Persistence manifest
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    //This test displays the "lazy loading" fetching method used by JPA.
    //There is no need to do the same on the "SkiTrail" and "LiftAccess" sets as they will behave the same
    //(it's not laziness i swear)
    @Test
    public void lazyLoadingSkiLiftsDemo() throws Exception {
        // Code executed inside a given transaction
        manual.begin();
            Resort resort = new Resort(RESORT_NAME, true, RESORT_EMAIL, RESORT_CITY_NAME);
            entityManager.persist(resort);

            SkiLift skiLift1 = new SkiLift(true, resort);
            skiLift1.setName("TestSkiLift1");
            entityManager.persist(skiLift1);
            resort.addLift(skiLift1);

            SkiLift skiLift2 = new SkiLift(true, resort);
            skiLift2.setName("TestSkiLift2");
            entityManager.persist(skiLift2);
            resort.addLift(skiLift2);

            SkiLift skiLift3 = new SkiLift(true, resort);
            skiLift3.setName("TestSkiLift3");
            entityManager.persist(skiLift3);
            resort.addLift(skiLift3);

            Resort sameTransaction = entityManager.find(Resort.class, resort.getId());
            assertEquals(resort, sameTransaction);
            assertEquals(3, resort.getLifts().size()); // lifts are attached in this transaction => available
        manual.commit();

        // Code executed outside the given transaction
        Resort detached = entityManager.find(Resort.class, resort.getId());
        assertNotEquals(resort, detached); // not equal cause the sets are different (one's are null, the other's are not)
        assertNull(detached.getLifts()); // lifts are not attached outside of the transaction => null;
    }
}
