package arquillian;


import fr.unice.polytech.isa.accounts.components.CustomerLinkerBean;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.resort.AlertLevel;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.monitoring.components.LiveAlertBean;
import fr.unice.polytech.isa.notifications.components.EmailNotifierBean;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.components.NotificationSchedulerBean;
import fr.unice.polytech.isa.notifications.interfaces.EmailNotification;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;
import fr.unice.polytech.isa.resort.components.ResortRegistryBean;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.ResortRegister;
import fr.unice.polytech.isa.shopping.components.CartManagerBean;
import fr.unice.polytech.isa.shopping.interfaces.SuperCartexProcessor;
import fr.unice.polytech.isa.statistics.interceptors.CardCounter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractMonitoringTest {



    @Deployment
    public static JavaArchive createDeployment(){
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            // Entities package.
            .addPackage(AlertLevel.class.getPackage())
            .addPackage(Notification.class.getPackage())
            .addPackage(Merchant.class.getPackage())

            // Interfaces package.
            .addPackage(ResortFinder.class.getPackage())
            .addPackage(SuperCartexProcessor.class.getPackage())
            .addPackage(CustomerFinder.class.getPackage())
            .addPackage(ResortRegister.class.getPackage())

            // Interceptors package.
            .addPackage(CardCounter.class.getPackage())

            // Components package.
            .addPackage(LiveAlertBean.class.getPackage())
            .addPackage(ResortRegistryBean.class.getPackage())
            .addPackage(CartManagerBean.class.getPackage())
            .addPackage(CustomerLinkerBean.class.getPackage())

            // Add classes for the Notification beans.
            .addClass(NotificationRegistration.class)
            .addClass(NotificationRegistryBean.class)
            .addClass(NotificationScheduling.class)
            .addClass(NotificationSchedulerBean.class)
            .addClass(NotificationProcessing.class)
            .addClass(EmailNotifierBean.class)
            .addClass(EmailNotification.class)


            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }


}
