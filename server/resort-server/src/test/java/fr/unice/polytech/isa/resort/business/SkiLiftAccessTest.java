package fr.unice.polytech.isa.resort.business;

import fr.unice.polytech.isa.accounts.components.*;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.notifications.components.NotificationProcessorBean;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.components.NotificationSchedulerBean;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;
import fr.unice.polytech.isa.resort.components.*;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.*;
import fr.unice.polytech.isa.shopping.components.CatalogBean;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;
import fr.unice.polytech.isa.statistics.components.presence.PresenceStatisticsFindingBean;
import fr.unice.polytech.isa.statistics.components.presence.PresenceStatisticsRegistryBean;
import fr.unice.polytech.isa.statistics.components.presence.PresenceStatisticsUpdateBean;
import fr.unice.polytech.isa.statistics.interceptors.CardCounter;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsRegistration;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsUpdater;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * For this class, we assume the skiLift always exist
 * (since it is supposed to called when a skier swipes his card at a skiLift, it has to physically exist)
 * We will verify whether it exists or not in another test classes
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class SkiLiftAccessTest {
    @EJB
    private CardChecker cardChecker;

    @EJB
    private ResortRegister resortRegister;

    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiLiftRegister skiLiftRegister;

    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private AccessRegister accessRegister;

    @EJB
    private CustomerRegistration customerRegistration;

    @EJB
    private CardRegistration cardRegistration;

    @EJB
    private CardFinder cardFinder;

    @EJB
    private PassRegistration passRegistration;

    @EJB
    private CustomerPassFinder customerPassFinder;

    @EJB
    private CustomerCardLinker customerCardLinker;

    @EJB
    private CatalogModifier catalogModifier;

    @PersistenceContext
    private EntityManager entityManager;

    private SkiLift skiLift;
    private Resort resort;
    private Card card;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Add this server's classes
                .addClass(AccessControllerBean.class)
                .addClass(AccessRegisterBean.class)
                .addClass(ResortRegistryBean.class)
                .addClass(SkiLiftRegistryBean.class)
                .addClass(SkiTrailRegistryBean.class)
                .addClass(ResortNotFoundException.class)
                .addClass(SkiLiftNotFoundException.class)
                .addClass(SkiTrailNotFoundException.class)
                .addClass(UnavailableNameException.class)
                .addClass(AccessRegister.class)
                .addClass(CardChecker.class)
                .addClass(ResortFinder.class)
                .addClass(ResortRegister.class)
                .addClass(SkiLiftFinder.class)
                .addClass(SkiLiftRegister.class)
                .addClass(SkiTrailFinder.class)
                .addClass(SkiTrailRegister.class)
                //Account server's classes
                .addClass(CustomerRegistration.class)
                .addClass(CustomerRegistryBean.class)
                .addClass(CardRegistration.class)
                .addClass(CardFinder.class)
                .addClass(CardRegistryBean.class)
                .addClass(PassRegistration.class)
                .addClass(CustomerPassFinder.class)
                .addClass(PassRegistryBean.class)
                .addClass(CustomerCardLinker.class)
                .addClass(CustomerLinkerBean.class)
                .addClass(UserFinder.class)
                .addClass(UserFinderBean.class)
                //Shopping server's classes
                .addClass(CatalogModifier.class)
                .addClass(CatalogBean.class)
                //Statistics server's classes
                .addClass(CardCounter.class)
                .addClass(PresenceStatisticsUpdater.class)
                .addClass(PresenceStatisticsUpdateBean.class)
                .addClass(PresenceStatisticsFinder.class)
                .addClass(PresenceStatisticsFindingBean.class)
                .addClass(PresenceStatisticsRegistration.class)
                .addClass(PresenceStatisticsRegistryBean.class)
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

    @Before
    public void setUpContext() throws Exception {
        //Setup resort
        resortRegister.registerResort(RESORT_NAME, RESORT_EMAIL, true, RESORT_CITY_NAME);
        resort = resortFinder.findByName(RESORT_NAME);

        //Setup skiLift
        skiLiftRegister.registerSkiLift(resort.getId(), SKI_LIFT_NAME, false);
        skiLift = skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME);

        //Setup customer, card and pass
        customerRegistration.register(JOHN_FIRSTNAME, JOHN_LASTNAME, JOHN_EMAIL);
        cardRegistration.addCard(JOHN_EMAIL, new ItemType(REGULAR, 0.0, ItemTypeName.CARD), PHYSICAL_CARD_ID);
        String passId = passRegistration.registerPass(JOHN_EMAIL,
                new PassType(DISCOVERY_PASS, false, Duration.ofMillis(1000), 0.0));
        Pass pass = customerPassFinder.findPassById(JOHN_EMAIL, passId).get();
        catalogModifier.addPass(DISCOVERY_PASS, 0.0, 0.0, Duration.ofMillis(1000), false);
        customerCardLinker.linkPassToCardOnline(JOHN_EMAIL, PHYSICAL_CARD_ID, pass.getId());
        card = cardFinder.findCardByPhysicalId(PHYSICAL_CARD_ID);
        card = entityManager.merge(card);
    }

    @After
    public void cleanUp() {
        //Delete resort, skiLift and access
        try {
            resortRegister.deleteResort(resort.getId());
        } catch (ResortNotFoundException e) {
            //This shouldn't happen
        }
        //Delete customer, card and pass
        try {
            customerRegistration.deleteCustomer(JOHN_EMAIL);
        } catch (CustomerNotFoundException e) {
            //This shouldn't happen
        }
        try {
            catalogModifier.deletePass(DISCOVERY_PASS, Duration.ofMillis(1000));
        } catch (UnknownCatalogEntryException e) {
            //This shouldn't happen
        }

        try {
            skiLiftRegister.deleteSkiLift(skiLift.getId());
        } catch (SkiLiftNotFoundException e){
            //This shouldn't happen
        }
    }

    @Test
    public void closedSkiLift() {
        assertFalse(cardChecker.checkCard(skiLift, card));
    }

    //There is no liftAccess corresponding to the pass name in the resort
    @Test
    public void liftAccessDoesntExist() {
        skiLift.setOpen(true);
        assertFalse(cardChecker.checkCard(skiLift, card));
    }

    //There is a lift access corresponding to the pass name
    // but it doesn't allow the skier to use the lift
    @Test
    public void liftAccessHasPassNameButNotTheRightLift() throws Exception {
        skiLiftRegister.registerSkiLift(resort.getId(), SKI_LIFT_NAME_2, true);
        accessRegister.addAccess(DISCOVERY_PASS, resort.getId(), skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME_2).getId());
        skiLift.setOpen(true);
        assertFalse(cardChecker.checkCard(skiLift, card));
    }

    @Test
    public void liftAccessHasPassNameAndLift() throws Exception {
        skiLift.setOpen(true);
        accessRegister.addAccess(DISCOVERY_PASS, resort.getId(), skiLift.getId());
        assertTrue(cardChecker.checkCard(skiLift, card));
    }
}
