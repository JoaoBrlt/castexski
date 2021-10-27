package fr.unice.polytech.isa.notifications.integration;

import arquillian.AbstractNotificationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
import fr.unice.polytech.isa.notifications.interfaces.EmailNotification;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class MailNotifierBeanIntegrationTest extends AbstractNotificationTest {
    @EJB
    private EmailNotification emailNotifier;

    @After
    public void cleanUp() throws ExternalEmailServiceException {
        // Delete all the emails.
        emailNotifier.deleteEmails();
    }

    @Test
    public void sendEmail() throws ExternalEmailServiceException, JsonProcessingException {
        // Send the email.
        Email email = new Email("lois.caryn@example.org", "nance.vernon@example.org", "Subject", "This is a message.");
        int identifier = emailNotifier.sendEmail(email);

        // Check the email.
        assertEquals(email, emailNotifier.getEmail(identifier));
    }

    @Test
    public void getEmailsFrom() throws ExternalEmailServiceException, JsonProcessingException {
        // Send the emails.
        Email firstEmail = new Email("john.doe@example.org", "jane.doe@example.org", "First email", "This is the first email.");
        Email secondEmail = new Email("john.doe@example.org", "silvester.francene@example.org", "Second email", "This is the second email.");
        Email thirdEmail = new Email("mick.winston@example.org", "arn.melanie@example.org", "Third email", "This is the third email.");
        emailNotifier.sendEmail(firstEmail);
        emailNotifier.sendEmail(secondEmail);
        emailNotifier.sendEmail(thirdEmail);

        // Check the emails sent from a specific email address.
        List<Email> emails = emailNotifier.getEmailsFrom("john.doe@example.org");
        assertEquals(2, emails.size());
        assertTrue(emails.contains(firstEmail));
        assertTrue(emails.contains(secondEmail));
        assertFalse(emails.contains(thirdEmail));
    }

    @Test
    public void getEmailsTo() throws ExternalEmailServiceException, JsonProcessingException {
        // Send the emails.
        Email firstEmail = new Email("elisa.jason@example.org", "danna.wanda@example.org", "First email", "This is the first email.");
        Email secondEmail = new Email("louis.loretta@example.org", "perce.chandler@example.org", "Second email", "This is the second email.");
        Email thirdEmail = new Email("allyn.wilbur@example.org", "danna.wanda@example.org", "Third email", "This is the third email.");
        emailNotifier.sendEmail(firstEmail);
        emailNotifier.sendEmail(secondEmail);
        emailNotifier.sendEmail(thirdEmail);

        // Check the emails sent to a specific email address.
        List<Email> emails = emailNotifier.getEmailsTo("danna.wanda@example.org");
        assertEquals(2, emails.size());
        assertTrue(emails.contains(firstEmail));
        assertFalse(emails.contains(secondEmail));
        assertTrue(emails.contains(thirdEmail));
    }
}
