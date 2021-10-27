package fr.unice.polytech.isa.notifications.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.SMS;
import fr.unice.polytech.isa.notifications.exceptions.ExternalPhoneServiceException;

import javax.ejb.Local;
import java.util.List;

/**
 * Phone notification.
 * <p>
 * Sends and receives text messages from the phone service.
 * </p>
 */
@Local
public interface PhoneNotification {
    /**
     * Sends a text message.
     *
     * @param message The text message to be sent.
     * @return The text message identifier.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while serializing the text message.
     */
    int sendMessage(SMS message) throws ExternalPhoneServiceException, JsonProcessingException;

    /**
     * Returns a text message by identifier.
     *
     * @param identifier The text message identifier.
     * @return The requested text message.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text message.
     */
    SMS getMessage(int identifier) throws ExternalPhoneServiceException, JsonProcessingException;

    /**
     * Returns the text messages sent from a specific phone number.
     *
     * @param from The phone number of the sender.
     * @return The list of text message identifiers sent from the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    List<SMS> getMessagesFrom(String from) throws ExternalPhoneServiceException, JsonProcessingException;

    /**
     * Returns the text messages sent to a specific phone number.
     *
     * @param to The phone number of the recipient.
     * @return The list of text message identifiers sent to the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    List<SMS> getMessagesTo(String to) throws ExternalPhoneServiceException, JsonProcessingException;

    /**
     * Deletes all the text messages (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     */
    void deleteMessages() throws ExternalPhoneServiceException;
}
