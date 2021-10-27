using System;
using System.Net;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;
using Partner.Data;

/**
 * Payment service.
 */
namespace Partner.Service {
    // The service is stateful, as it is only a Proof of Concept.
    // Services should be stateless, this is for demonstration purpose only.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class PaymentService : IPaymentService {
        private const string MagicKey = "896983"; // ASCII code for "YES"

        private Dictionary<int, Payment> payments = new Dictionary<int, Payment>();
        private int counter;

        public int ReceivePaymentRequest(PaymentRequest request) {
            Console.WriteLine("ReceivePaymentRequest: " + request);
            var payment = BuildPayment(request);
            payments.Add(counter, payment);
            return counter;
        }

        public Payment FindPaymentById(int identifier) {
            if(!payments.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return null;
            }
            return payments[identifier];
        }

        public List<int> GetAllPaymentIds() {
            return payments.Keys.ToList();
        }

        private Payment BuildPayment(PaymentRequest request) {
            var payment = new Payment();
            payment.Identifier = counter++;
            payment.creditCard = request.creditCard;
            payment.Amount = request.Amount;
            if (request.creditCard.CreditCardNumber.Contains(MagicKey) && isStillActive(payment.creditCard)) {
                payment.Status = PaymentStatus.Ok;
            } else {
                payment.Status = PaymentStatus.Ko;
            }
            payment.Date = DateTime.Now.ToString();
            return payment;
        }

        private bool isStillActive(CreditCard creditCard) {
            var expirationDate = DateTime.Parse(creditCard.ExpirationDate);
            return DateTime.Compare(expirationDate, DateTime.Now) > 0;
        }

        //===============================================
        //====================REFUNDS====================
        //===============================================

        private Dictionary<int, Refund> refunds = new Dictionary<int, Refund>();

        public int ReceiveRefundRequest(RefundRequest request) {
            Console.WriteLine("ReceiveRefundRequest: " + request);
            if(thereIsAnErrorWithTheRequest(request)) return -1;
            var refund = BuildRefund(request);
            refunds[request.paymentId] = refund;
            return request.paymentId;
        }

        public Refund FindRefundById(int identifier) {
            if(!refunds.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return null;
            }
            return refunds[identifier];
        }

        public List<int> GetAllRefundIds() {
            return refunds.Keys.ToList();
        }

        private Refund BuildRefund(RefundRequest request) {
            var refund = new Refund();
            refund.PaymentIdentifier = request.paymentId;
            refund.Amount = request.Amount;
            refund.Date = DateTime.Now.ToString();
            return refund;
        }

        private bool thereIsAnErrorWithTheRequest(RefundRequest request) {
            int paymentId = request.paymentId;
            if (!payments.ContainsKey(paymentId)) {
                Console.WriteLine("The order number " + paymentId + " does not exist");
                return true;
            }
            if (refunds.ContainsKey(paymentId)) {
                Console.WriteLine("The refund for the order number " + paymentId + " has already been processed");
                return true;
            }
            if (payments[paymentId].Amount != request.Amount) {
                Console.WriteLine("The amount of money specified in the refund request is different than the one in the order number " + paymentId);
                return true;
            }
            if (payments[paymentId].Status != 0) {
                Console.WriteLine("The payment for the order number " + paymentId + " hasn't been processed, therefore there is no refund to issue!");
                return true;
            }
            return false;
        }
    }    
}