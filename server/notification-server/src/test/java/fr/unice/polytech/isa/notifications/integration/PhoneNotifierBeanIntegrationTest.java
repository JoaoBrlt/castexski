package fr.unice.polytech.isa.notifications.integration;

import arquillian.AbstractNotificationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.SMS;
import fr.unice.polytech.isa.notifications.exceptions.ExternalPhoneServiceException;
import fr.unice.polytech.isa.notifications.interfaces.PhoneNotification;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PhoneNotifierBeanIntegrationTest extends AbstractNotificationTest {
    @EJB
    private PhoneNotification phoneNotifier;

    @After
    public void cleanUp() throws ExternalPhoneServiceException {
        // Delete all the text messages.
        phoneNotifier.deleteMessages();
    }

    @Test
    public void sendMessage() {
        try {
            // Send the text message.
            SMS message = new SMS("+33123456789", "+33600110011", "This is a message.");
            int identifier = phoneNotifier.sendMessage(message);

            // Check the text message.
            assertEquals(message, phoneNotifier.getMessage(identifier));
        } catch (ExternalPhoneServiceException | JsonProcessingException error) {
            fail(error.getMessage());
        }
    }

    @Test
    public void getMessagesFrom() throws ExternalPhoneServiceException, JsonProcessingException {
        // Send the emails.
        SMS firstMessage = new SMS("+12245770518", "+33695699316", "This is the first message.");
        SMS secondMessage = new SMS("+12245770518", "+37745112746", "This is the second message.");
        SMS thirdMessage = new SMS("+43666944983238", "+584266288694", "This is the third message.");
        phoneNotifier.sendMessage(firstMessage);
        phoneNotifier.sendMessage(secondMessage);
        phoneNotifier.sendMessage(thirdMessage);

        // Check the text messages sent from a specific phone number.
        List<SMS> messages = phoneNotifier.getMessagesFrom("+12245770518");
        assertEquals(2, messages.size());
        assertTrue(messages.contains(firstMessage));
        assertTrue(messages.contains(secondMessage));
        assertFalse(messages.contains(thirdMessage));
    }

    @Test
    public void getMessagesTo() throws ExternalPhoneServiceException, JsonProcessingException {
        // Send the text messages.
        SMS firstMessage = new SMS("+97338380249", "+553278154247", "This is the first message.");
        SMS secondMessage = new SMS("+61483020797", "+573333801374", "This is the second message.");
        SMS thirdMessage = new SMS("+436579358", "+553278154247", "This is the third message.");
        phoneNotifier.sendMessage(firstMessage);
        phoneNotifier.sendMessage(secondMessage);
        phoneNotifier.sendMessage(thirdMessage);

        // Check the text messages sent to a specific phone number.
        List<SMS> messages = phoneNotifier.getMessagesTo("+553278154247");
        assertEquals(2, messages.size());
        assertTrue(messages.contains(firstMessage));
        assertFalse(messages.contains(secondMessage));
        assertTrue(messages.contains(thirdMessage));
    }
}
