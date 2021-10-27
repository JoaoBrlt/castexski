package arquillian;

import fr.unice.polytech.isa.accounts.components.UserFinderBean;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.common.entities.accounts.User;
import fr.unice.polytech.isa.merchants.components.MerchantRegistryBean;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.merchants.webservices.MerchantService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractMerchantTest {
    @Deployment
    public static JavaArchive createDeployment(){
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Entities.
            .addPackage(Merchant.class.getPackage())
            .addPackage(User.class.getPackage())
            .addPackage(Resort.class.getPackage())

            // Interfaces.
            .addPackage(MerchantFinder.class.getPackage())
            .addPackage(UserFinder.class.getPackage())

            // Components.
            .addPackage(MerchantRegistryBean.class.getPackage())
            .addPackage(UserFinderBean.class.getPackage())

            // Exceptions.
            .addPackage(MerchantNotFoundException.class.getPackage())
            .addPackage(UnavailableEmailException.class.getPackage())

            // Web services.
            .addPackage(MerchantService.class.getPackage())

            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
