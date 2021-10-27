package fr.unice.polytech.isa.notifications.business;

import arquillian.AbstractNotificationTest;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class NotificationProcessorBeanTest extends AbstractNotificationTest {
    @EJB
    private NotificationProcessing notificationProcessor;

    @Rule
    public final ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void processNotification() {
       assertTrue(true);
    }
}
