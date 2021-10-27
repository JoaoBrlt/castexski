using System;
using System.Net;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;

using Partner.Data;

/**
 * Phone service.
 */
namespace Partner.Service {
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class PhoneService : IPhoneService {
        /**
         * The list of messages.
         */
        private Dictionary<int, Message> messages = new Dictionary<int, Message>();

        /**
         * The counter of messages.
         */
        private int counter;

        /**
         * Sends a message.
         */
        public int SendMessage(Message message) {
            Console.WriteLine("SendMessage: " + message);
            messages.Add(counter, message);
            return counter++;
        }

        /**
         * Deletes all the messages.
         */
        public void DeleteMessages() {
            messages.Clear();
        }

         /**
         * Returns a message by identifier.
         */
        public Message GetMessage(int identifier) {
            if (!messages.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return null;
            }
            return messages[identifier];
        }

        /**
         * Deletes a message.
         */
        public void DeleteMessage(int identifier) {
            if (!messages.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return;
            }
            messages.Remove(identifier);
        }

        /**
         * Returns all the messages sent to a specific phone number.
         */
        public List<int> GetMessagesTo(String to) {
            return messages
                .Where(pair => pair.Value.to == to)
                .Select(pair => pair.Key)
                .ToList();
        }

        /**
         * Returns all the messages sent from a specific phone number.
         */
        public List<int> GetMessagesFrom(String from) {
            return messages
                .Where(pair => pair.Value.from == from)
                .Select(pair => pair.Key)
                .ToList();
        }
    }
}