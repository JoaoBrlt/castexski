package arquillian;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.payment.components.BillingBean;
import fr.unice.polytech.isa.payment.external.BankService;
import fr.unice.polytech.isa.payment.interfaces.PaymentProcessor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractPaymentTest {


    @Deployment
    public static JavaArchive createDeployment(){
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Interfaces.
            .addPackage(PaymentProcessor.class.getPackage())

            // Components.
            .addPackage(BillingBean.class.getPackage())

            // External services.
            .addPackage(BankService.class.getPackage())

            // Entities package.
            .addPackage(Customer.class.getPackage())

            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
        }
}
