package fr.unice.polytech.isa.notifications.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
import fr.unice.polytech.isa.notifications.external.EmailService;
import fr.unice.polytech.isa.notifications.interfaces.EmailNotification;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Mail notifier bean.
 * <p>
 * Sends and receives emails from the email service.
 * </p>
 */
@Stateless
public class EmailNotifierBean implements EmailNotification {
    /**
     * The email service.
     */
    private EmailService emailService;

    /**
     * Initializes the email service.
     */
    @PostConstruct
    private void initializeEmailService() {
        // Build the email service client.
        emailService = new EmailService(
            System.getenv().getOrDefault("MAIL_HOST", "localhost"),
            System.getenv().getOrDefault("MAIL_PORT", "9292")
        );
    }

    /**
     * Sends an email.
     *
     * @param email The email to be sent.
     * @return The email identifier.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while serializing the email.
     */
    @Override
    public int sendEmail(Email email) throws ExternalEmailServiceException, JsonProcessingException {
        return emailService.sendEmail(email);
    }

    /**
     * Returns an email by identifier.
     *
     * @param identifier The email identifier.
     * @return The requested email.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the email.
     */
    @Override
    public Email getEmail(int identifier) throws ExternalEmailServiceException, JsonProcessingException {
        return emailService.getEmail(identifier);
    }

    /**
     * Returns the emails sent from a specific email address.
     *
     * @param from The email address of the sender.
     * @return The list of emails sent from the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the emails.
     */
    @Override
    public List<Email> getEmailsFrom(String from) throws ExternalEmailServiceException, JsonProcessingException {
        List<Email> emails = new ArrayList<>();

        // Get the identifiers.
        int[] identifiers = emailService.getEmailsFrom(from);

        // Get the emails.
        for (int identifier : identifiers) {
            emails.add(emailService.getEmail(identifier));
        }

        return emails;
    }

    /**
     * Returns the emails sent to a specific email address.
     *
     * @param to The email address of the recipient.
     * @return The list of emails sent to the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the emails.
     */
    @Override
    public List<Email> getEmailsTo(String to) throws ExternalEmailServiceException, JsonProcessingException {
        List<Email> emails = new ArrayList<>();

        // Get the identifiers.
        int[] identifiers = emailService.getEmailsTo(to);

        // Get the emails.
        for (int identifier : identifiers) {
            emails.add(emailService.getEmail(identifier));
        }

        return emails;
    }

    /**
     * Deletes all the emails (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     */
    @Override
    public void deleteEmails() throws ExternalEmailServiceException {
        emailService.deleteEmails();
    }
}
