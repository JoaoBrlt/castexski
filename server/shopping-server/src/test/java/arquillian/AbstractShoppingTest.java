package arquillian;

import fr.unice.polytech.isa.accounts.components.CustomerRegistryBean;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.payment.components.BillingBean;
import fr.unice.polytech.isa.payment.interfaces.PaymentProcessor;
import fr.unice.polytech.isa.shopping.components.CartManagerBean;
import fr.unice.polytech.isa.shopping.interfaces.CartModifier;
import fr.unice.polytech.isa.shopping.interfaces.CreditCardRegistration;
import fr.unice.polytech.isa.statistics.components.purchases.PurchaseStatisticsUpdateBean;
import fr.unice.polytech.isa.statistics.interceptors.PurchaseCounter;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsUpdater;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractShoppingTest {
    @Deployment
    public static JavaArchive createDeployment(){
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Entities package.
            .addPackage(Cart.class.getPackage())


            // Interfaces package.
            .addPackage(CartModifier.class.getPackage())
            .addPackage(CreditCardRegistration.class.getPackage())
            .addPackage(CustomerFinder.class.getPackage())
            .addPackage(PaymentProcessor.class.getPackage())
            .addPackage(PurchaseStatisticsUpdater.class.getPackage())

            // Components package.
            .addPackage(CartManagerBean.class.getPackage())
            .addPackage(CustomerRegistryBean.class.getPackage())
            .addPackage(BillingBean.class.getPackage())
            .addPackage(PurchaseStatisticsUpdateBean.class.getPackage())
            //Interceptors
            .addPackage(PurchaseCounter.class.getPackage())


            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }
}
