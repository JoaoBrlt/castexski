package fr.unice.polytech.isa.notifications.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.SMS;
import fr.unice.polytech.isa.notifications.exceptions.ExternalPhoneServiceException;
import fr.unice.polytech.isa.notifications.external.PhoneService;
import fr.unice.polytech.isa.notifications.interfaces.PhoneNotification;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Phone notifier bean.
 * <p>
 * Sends and receives text messages from the phone service.
 * </p>
 */
@Stateless
public class PhoneNotifierBean implements PhoneNotification {
    /**
     * The phone service.
     */
    private PhoneService phoneService;

    /**
     * Initializes the phone service.
     */
    @PostConstruct
    private void initializePhoneService() {
        // Build the phone service client.
        phoneService = new PhoneService(
            System.getenv().getOrDefault("PHONE_HOST", "localhost"),
            System.getenv().getOrDefault("PHONE_PORT", "9393")
        );
    }

    /**
     * Sends a text message.
     *
     * @param message The text message to be sent.
     * @return The text message identifier.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while serializing the text message.
     */
    @Override
    public int sendMessage(SMS message) throws ExternalPhoneServiceException, JsonProcessingException {
        return phoneService.sendMessage(message);
    }

    /**
     * Returns a text message by identifier.
     *
     * @param identifier The text message identifier.
     * @return The requested text message.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    @Override
    public SMS getMessage(int identifier) throws ExternalPhoneServiceException, JsonProcessingException {
        return phoneService.getMessage(identifier);
    }


    /**
     * Returns the text messages sent from a specific phone number.
     *
     * @param from The phone number of the sender.
     * @return The list of text messages sent from the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    @Override
    public List<SMS> getMessagesFrom(String from) throws ExternalPhoneServiceException, JsonProcessingException {
        List<SMS> messages = new ArrayList<>();

        int[] identifiers = phoneService.getMessagesFrom(from);
        for (int identifier : identifiers) {
            messages.add(phoneService.getMessage(identifier));
        }

        return messages;
    }

    /**
     * Returns the text messages sent to a specific phone number.
     *
     * @param to The phone number of the recipient.
     * @return The list of text messages sent to the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    @Override
    public List<SMS> getMessagesTo(String to) throws ExternalPhoneServiceException, JsonProcessingException {
        List<SMS> messages = new ArrayList<>();

        int[] identifiers = phoneService.getMessagesTo(to);
        for (int identifier : identifiers) {
            messages.add(phoneService.getMessage(identifier));
        }

        return messages;
    }

    /**
     * Deletes all the text messages (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     */
    @Override
    public void deleteMessages() throws ExternalPhoneServiceException {
        phoneService.deleteMessages();
    }
}
