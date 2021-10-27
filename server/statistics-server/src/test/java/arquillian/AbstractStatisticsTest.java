package arquillian;

import fr.unice.polytech.isa.accounts.components.CardRegistryBean;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassCatalog;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.statistics.business.StatelessBeanToTestInterception;
import fr.unice.polytech.isa.statistics.components.DailyReportBean;
import fr.unice.polytech.isa.statistics.components.presence.PresenceStatisticsUpdateBean;
import fr.unice.polytech.isa.statistics.components.purchases.PurchaseStatisticsUpdateBean;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.DailyReportCreator;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;
//import fr.unice.polytech.isa.statistics.webservices.PurchaseStatisticsWebService;
//import fr.unice.polytech.isa.statistics.webservices.PurchaseStatisticsWebServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional(TransactionMode.COMMIT)
public abstract class AbstractStatisticsTest {
    protected final String RESORT_NAME = "resortTest";

    protected static final String PHYSICAL_CARD_ID = "0123456789101121";
    protected static final String PHYSICAL_CARD_ID_2 = "0123456789101121";
    protected final int startingHour = 15;
    protected final String SKI_LIFT_NAME = "skiLiftTest";
    protected final String SKI_LIFT_NAME_2 = "skiLiftTest2";
    protected final String DOUBLE_SKI_LIFT_NAME = "doubleSkiLift";

    protected final LocalDateTime dateTime = LocalDateTime.of(2021,1,1,startingHour,30);
    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static JavaArchive createDeployment() {
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
                // Entities package.
                .addPackage(DailyBuyingStatistics.class.getPackage())

                //Bean for testing interception
                .addClass(StatelessBeanToTestInterception.class)

                // Interfaces package.
                .addPackage(PurchaseStatisticsFinder.class.getPackage())
                .addPackage(PresenceStatisticsFinder.class.getPackage())
                .addClass(DailyReportCreator.class)

                //Purchase Web Service
              //  .addClass(PurchaseStatisticsWebService.class)
              //  .addClass(PurchaseStatisticsWebServiceImpl.class)

                //Other Modules' Beans & Interfaces
                .addPackage(CardRegistration.class.getPackage())
                .addPackage(CardRegistryBean.class.getPackage())

                // Components package.
                .addPackage(PurchaseStatisticsUpdateBean.class.getPackage())
                .addPackage(PresenceStatisticsUpdateBean.class.getPackage())
                .addClass(DailyReportBean.class)

                // Exceptions package.
                .addPackage(NoDailyStatisticsException.class.getPackage())

                // Persistence file.
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }


    protected void settingUpResort() {
        Resort r = new Resort();
        r.setOpen(true);
        r.setName(RESORT_NAME);
        entityManager.persist(r);
    }

    protected void deleteResort(String id) throws Exception {
        Resort r = findResortById(id);
        entityManager.remove(r);
    }

    protected String getResortId() throws Exception {
        return findByName(RESORT_NAME).getId();
    }

    protected void settingUpSkiLift(String resortId, String name) throws Exception {
        Resort r = findResortById(resortId);
        entityManager.merge(r);
        SkiLift s = new SkiLift();
        s.setOpen(true);
        s.setName(name);
        s.setResort(r);
        entityManager.persist(s);
        r.addLift(s);
    }

    Resort findByName(String name) throws Exception {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resort> criteria = builder.createQuery(Resort.class);
        Root<Resort> root = criteria.from(Resort.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Resort> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            throw new Exception("Error in resort find by name : " + name);
        }
    }

    SkiLift findByName(String resortId, String name) throws Exception {
        Resort r = findResortById(resortId);
        entityManager.merge(r);

        Optional<SkiLift> skiLift = r.getLifts().stream().filter(l ->
            l.getName().equals(name)
        ).findFirst();
        try {
            return skiLift.get();
        } catch (NoSuchElementException ignored) {
            throw new Exception("Error in skiLift find by name : " + name);}
    }

    protected String getSkiLiftId(String resortId, String name) throws Exception {
        return findByName(resortId, name).getId();
    }

    protected Resort findResortById(String resortId) throws Exception {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resort> criteria = builder.createQuery(Resort.class);
        Root<Resort> root = criteria.from(Resort.class);
        criteria.select(root).where(builder.equal(root.get("id"), resortId));
        TypedQuery<Resort> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) { throw new Exception("Exception in findResortId");}
    }

    protected SkiLift findSkiLiftById(String skiLiftId) throws Exception {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkiLift> criteria = builder.createQuery(SkiLift.class);
        Root<SkiLift> root = criteria.from(SkiLift.class);
        criteria.select(root).where(builder.equal(root.get("id"), skiLiftId));
        TypedQuery<SkiLift> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException ignored) { throw new Exception("Exception in findSkiLiftById");}
    }

    protected void settingUpSkiLift() throws Exception {
        Resort r = findResortById(getResortId());
        entityManager.merge(r);
        SkiLift s = new SkiLift();
        s.setOpen(true);
        s.setName(SKI_LIFT_NAME);
        s.setResort(r);
        entityManager.persist(s);
        r.addLift(s);
    }

    protected String getSkiLiftId(String resortId) throws Exception {
        return findByName(resortId, SKI_LIFT_NAME).getId();
    }

    protected void addPass(String name, double regularPrice, double childrenPrice, Duration duration, boolean isPrivateItem) throws ItemAlreadyExistException {
        PassCatalog passCatalog = new PassCatalog(name, regularPrice, childrenPrice, duration, isPrivateItem);
        entityManager.persist(passCatalog);
    }

    protected void deletePass(String name, Duration duration) throws UnknownCatalogEntryException {
        Optional<PassCatalog> passCatalog = findPass(name, duration);
        if (passCatalog.isPresent()) {
            entityManager.remove(passCatalog.get());
        }
    }

    Optional<PassCatalog> findPass(String name, Duration duration) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PassCatalog> criteria = builder.createQuery(PassCatalog.class);
        Root<PassCatalog> root =  criteria.from(PassCatalog.class);
        criteria.where(builder.and(builder.equal(root.get("name"), name), builder.equal(root.get("maxDurationRaw"), duration.toString())));
        TypedQuery<PassCatalog> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre){
            return Optional.empty();
        }
    }

    protected Cart getCustomerCart(Customer customer) {
        Customer c = entityManager.merge(customer);
        if (c.getCart() == null) {
            c.setCart(new Cart(c));
        }
        return c.getCart();
    }

    protected void addToCart(Customer customer, Item item) throws NullQuantityException {
        Cart cart = getCustomerCart(customer);
        Item finalItem = item;
        Optional<Item> itemCart = cart.getItems().stream().filter(i -> i.getType().equals(finalItem.getType())).findFirst();
        if (itemCart.isPresent()){
            cart.deleteItem(itemCart.get());
            item = new Item(item.getType(), item.getQuantity() + itemCart.get().getQuantity());
        }
        if (item.getQuantity() > 0) cart.addItem(new Item(item.getType(), item.getQuantity()));
        else {
            throw new NullQuantityException(item.getType().getName());
        }
    }



}
