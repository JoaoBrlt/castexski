package arquillian;

import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.common.exceptions.EmptyCartException;
import fr.unice.polytech.isa.notifications.components.NotificationProcessorBean;
import fr.unice.polytech.isa.notifications.components.NotificationRegistryBean;
import fr.unice.polytech.isa.notifications.components.NotificationSchedulerBean;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;
import fr.unice.polytech.isa.resort.components.AccessRegisterBean;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.external.DisplayService;
import fr.unice.polytech.isa.resort.interfaces.AccessRegister;
import fr.unice.polytech.isa.resort.interfaces.DisplayAccessor;
import fr.unice.polytech.isa.resort.interfaces.DisplayFinder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public abstract class AbstractDisplayTest {
    @Deployment
    public static JavaArchive createDeployment() {
        // Building a Java Archive.
        return ShrinkWrap.create(JavaArchive.class)
            //Display Server
            .addPackage(DisplayAccessor.class.getPackage())
            .addClass(DisplayPanelNotFoundException.class)
            .addClass(DisplayService.class)
            .addPackage(DisplayFinder.class.getPackage())

            //Common Server
            .addPackage(DisplayPanel.class.getPackage())
            .addPackage(EmptyCartException.class.getPackage())

            //Resort Server
            .addPackage(AccessRegisterBean.class.getPackage())
            .addPackage(AccessRegister.class.getPackage())
            .addPackage(ResortNotFoundException.class.getPackage())

            //Notification Server
            .addClass(NotificationRegistration.class)
            .addClass(NotificationRegistryBean.class)
            .addClass(NotificationScheduling.class)
            .addClass(NotificationSchedulerBean.class)
//            .addClass(NotificationProcessing.class)
//            .addClass(NotificationProcessorBean.class)

            // Persistence file.
            .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml");
    }
}
