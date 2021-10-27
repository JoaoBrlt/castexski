package arquillian;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.isa.accounts.components.CustomerRegistryBean;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.merchants.components.MerchantRegistryBean;
import fr.unice.polytech.isa.accounts.components.UserFinderBean;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.accounts.interfaces.UserFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.common.entities.accounts.User;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.exceptions.NotificationNotFoundException;
import fr.unice.polytech.isa.notifications.external.EmailService;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractNotificationTest {
    @Deployment
    public static JavaArchive createDeployment() {
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Entities.
            .addPackage(Notification.class.getPackage())
            .addPackage(User.class.getPackage())
            .addPackage(Customer.class.getPackage())
            .addPackage(Merchant.class.getPackage())

            // Interfaces.
            .addPackage(NotificationRegistration.class.getPackage())
            .addPackage(UserFinder.class.getPackage())
            .addPackage(CustomerFinder.class.getPackage())
            .addPackage(MerchantFinder.class.getPackage())

            // Components.
            .addPackage(NotificationRegistryBean.class.getPackage())
            .addPackage(UserFinderBean.class.getPackage())
            .addPackage(CustomerRegistryBean.class.getPackage())
            .addPackage(MerchantRegistryBean.class.getPackage())

            // External services.
            .addPackage(EmailService.class.getPackage())

            // Exceptions.
            .addPackage(NotificationNotFoundException.class.getPackage())
            .addPackage(UnavailableEmailException.class.getPackage())

            // Utilities.
            .addPackage(JsonProcessingException.class.getPackage())
            .addPackage(ObjectMapper.class.getPackage())

            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        }
}
