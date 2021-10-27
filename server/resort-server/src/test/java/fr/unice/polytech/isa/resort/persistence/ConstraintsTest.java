package fr.unice.polytech.isa.resort.persistence;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.resort.SkiTrail;
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
import javax.validation.ConstraintViolationException;

@RunWith(Arquillian.class)
public class ConstraintsTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction manual;

    /********************************
     ** Constraint Violation cases **
     ********************************/

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Persistence manifest
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }

    @Test(expected = ConstraintViolationException.class)
    public void cannotStoreSkiTrailWithNullName() throws Exception {
        SkiTrail skiTrail = new SkiTrail(); //Name is obviously null here
        persistWithinTransaction(skiTrail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void cannotStoreResortWithNullName() throws Exception {
        Resort resort = new Resort(); //Name is obviously null here
        persistWithinTransaction(resort);
    }

    @Test(expected = ConstraintViolationException.class)
    public void cannotStoreSkiLiftWithNullStatistics() throws Exception {
        SkiLift skiLift = new SkiLift();
        skiLift.setDailyStatistics(null);
        persistWithinTransaction(skiLift);
    }

    /**
     * Asks the entity manager to persist a given object, within a manually handled transaction. Used to catch the
     * real reason of a transactional error and avoid to hide it inside a RollbackException. Useful to assess that
     * the root cause of an expected error is the right one.
     */
    private void persistWithinTransaction(Object obj) throws Exception {
        manual.begin();
        try {
            entityManager.persist(obj);
            manual.commit();
        } catch(Exception e) {
            manual.rollback();
            throw e;
        }
    }

}
