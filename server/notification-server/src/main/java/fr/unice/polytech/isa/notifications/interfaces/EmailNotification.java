package fr.unice.polytech.isa.notifications.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;

import javax.ejb.Local;
import java.util.List;

/**
 * Email notification.
 * <p>
 * Sends and receives emails from the email service.
 * </p>
 */
@Local
public interface EmailNotification {
    /**
     * Sends an email.
     *
     * @param email The email to be sent.
     * @return The email identifier.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException  if there was an error while serializing the email.
     */
    int sendEmail(Email email) throws ExternalEmailServiceException, JsonProcessingException;

    /**
     * Returns an email by identifier.
     *
     * @param identifier The email identifier.
     * @return The requested email.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException  if there was an error while serializing the email.
     */
    Email getEmail(int identifier) throws ExternalEmailServiceException, JsonProcessingException;

    /**
     * Returns the emails sent from a specific email address.
     *
     * @param from The email address of the sender.
     * @return The list of emails sent from the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException  if there was an error while deserializing the emails.
     */
    List<Email> getEmailsFrom(String from) throws ExternalEmailServiceException, JsonProcessingException;

    /**
     * Returns the emails sent to a specific email address.
     *
     * @param to The email address of the recipient.
     * @return The list of emails sent to the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException  if there was an error while deserializing the emails.
     */
    List<Email> getEmailsTo(String to) throws ExternalEmailServiceException, JsonProcessingException;

    /**
     * Deletes all the emails (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     */
    void deleteEmails() throws ExternalEmailServiceException;
}
