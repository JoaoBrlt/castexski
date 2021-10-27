using System;
using System.Net;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;

using Partner.Data;

/**
 * Email service.
 */
namespace Partner.Service {
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class EmailService : IEmailService {
        /**
         * The list of emails.
         */
        private Dictionary<int, Email> emails = new Dictionary<int, Email>();

        /**
         * The counter of emails.
         */
        private int counter;

        /**
         * Sends an email.
         */
        public int SendEmail(Email email) {
            Console.WriteLine("SendMail: " + email);
            emails.Add(counter, email);
            return counter++;
        }

        /**
         * Deletes all the emails.
         */
        public void DeleteEmails() {
            emails.Clear();
        }

        /**
         * Returns an email by identifier.
         */
        public Email GetEmail(int identifier) {
            if (!emails.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return null;
            }
            return emails[identifier];
        }

        /**
         * Deletes an email.
         */
        public void DeleteEmail(int identifier) {
            if (!emails.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return;
            }
            emails.Remove(identifier);
        }

        /**
         * Returns all the emails sent to an email address.
         */
        public List<int> GetEmailsTo(String to) {
            return emails
                .Where(pair => pair.Value.to == to)
                .Select(pair => pair.Key)
                .ToList();
        }

        /**
         * Returns all the emails sent from an email address.
         */
        public List<int> GetEmailsFrom(String from) {
            return emails
                .Where(pair => pair.Value.from == from)
                .Select(pair => pair.Key)
                .ToList();
        }
    }
}