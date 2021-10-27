package features;

import cucumber.api.CucumberOptions;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
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
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;

import static fr.unice.polytech.isa.resort.ResortTestUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources")
@Transactional(TransactionMode.COMMIT)
public class CheckingAccessCucumberTest {
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

    private Resort resort;
    private SkiLift skiLift;
    private Card card;
    private String registeredPassName;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                //Add this server's classes
                .addClass(AccessControllerBean.class)
                .addClass(AccessRegisterBean.class)
                .addClass(OpennessChangerBean.class)
                .addClass(ResortRegistryBean.class)
                .addClass(SkiLiftRegistryBean.class)
                .addClass(SkiTrailRegistryBean.class)
                .addClass(ResortNotFoundException.class)
                .addClass(SkiLiftNotFoundException.class)
                .addClass(SkiTrailNotFoundException.class)
                .addClass(UnavailableNameException.class)
                .addClass(AccessRegister.class)
                .addClass(CardChecker.class)
                .addClass(OpennessChanger.class)
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

    @Given("a (.*) resort named (.*)")
    public void createResort(String isOpen, String resortName) throws Exception {
        resortRegister.registerResort(resortName, RESORT_EMAIL, (isOpen.equals("opened")), RESORT_CITY_NAME);
        resort = resortFinder.findByName(resortName);
    }

    @And("a ski lift named (.*) belonging to (.*)")
    public void createSkiLift(String liftName, String resortName) throws Exception {
        if(resortName.equals(resort.getName())) {
            skiLiftRegister.registerSkiLift(resort.getId(), liftName, false);
            skiLift = skiLiftFinder.findByName(resort.getId(), liftName);
        }
    }

    @And("a (.*) pass allowing anyone to go through (.*)")
    public void createGoodPass(String passName, String liftName) throws Exception {
        if(skiLift.getName().equals(liftName)) {
            catalogModifier.addPass(passName, 0.0, 0.0, Duration.ofMillis(1000), false);
            registeredPassName = passName;
            accessRegister.addAccess(passName, resort.getId(), skiLift.getId());
        }
    }

    @When("the skier swipes his card at a (.*) ski lift")
    public void bipTheCardAndOpenSkiLift(String isOpen) {
        skiLift.setOpen((isOpen.equals("opened")));
    }

    @And("his card has the (.*) pass")
    public void checkPassAndSetCard(String passName) throws Exception{
        customerRegistration.register(JOHN_FIRSTNAME, JOHN_LASTNAME, JOHN_EMAIL);
        cardRegistration.addCard(JOHN_EMAIL, new ItemType(REGULAR, 0.0, ItemTypeName.CARD), PHYSICAL_CARD_ID);
        String passId = passRegistration.registerPass(JOHN_EMAIL,
                new PassType(passName, false, Duration.ofMillis(1000), 0.0));
        Pass pass = customerPassFinder.findPassById(JOHN_EMAIL, passId).get();
        customerCardLinker.linkPassToCardOnline(JOHN_EMAIL, PHYSICAL_CARD_ID, pass.getId());
        card = cardFinder.findCardByPhysicalId(PHYSICAL_CARD_ID);
        card = entityManager.merge(card);
    }

    @Then("the ski lift allows the skier to go")
    public void checkSkiLiftTrue() {
        assertTrue(cardChecker.checkCard(skiLift, card));
    }

    @Then("the ski lift does not allow the skier to go")
    public void checkSkiLiftFalse() {
        assertFalse(cardChecker.checkCard(skiLift, card));
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
            catalogModifier.deletePass(registeredPassName, Duration.ofMillis(1000));
        } catch (UnknownCatalogEntryException | NullPointerException e) {
            //NPE for the "closed" scenario
        }
    }
}
