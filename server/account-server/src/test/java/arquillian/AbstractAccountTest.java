package arquillian;

import fr.unice.polytech.isa.accounts.components.CustomerRegistryBean;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.accounts.interfaces.CustomerRegistration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractAccountTest {
    @Deployment
    public static JavaArchive createDeployment(){
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Entities package.
            .addPackage(Customer.class.getPackage())

            // Interfaces package.
            .addPackage(CustomerRegistration.class.getPackage())

            // Components package.
            .addPackage(CustomerRegistryBean.class.getPackage())

            // Exceptions package.
            .addPackage(UnavailableEmailException.class.getPackage())

            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }
}
