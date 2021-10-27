using System.Runtime.Serialization;
using System;

/**
 * Business objects.
 */
namespace Partner.Data {
    [DataContract(Namespace = "http://isadevops/2021/payment/data/",
                  Name = "CreditCard")]
    public class CreditCard {
        [DataMember]
        public string CreditCardNumber { get; set; }

        [DataMember]
        public string ExpirationDate { get; set; }

        [DataMember]
        public int CVV { get; set; }

        override public string ToString() {
            return "[" + CreditCardNumber + ", " + ExpirationDate + ", " + CVV + "]";
        }
    }

    // 0 IS OK
    // 1 IS KO
    public enum PaymentStatus { Ok, Ko }

    [DataContract(Namespace = "http://isadevops/2021/payment/data/",
                  Name = "PaymentRequest")]
    public class PaymentRequest {
        [DataMember]
        public CreditCard creditCard { get; set; }

        [DataMember]
        public double Amount { get; set; }

        override public string ToString() {
            return "PaymentRequest : \n\t Credit Card : " + creditCard.ToString() + "\n\tAmount : " + Amount;
        }
    }

    [DataContract(Namespace = "http://isadevops/2021/payment/data/",
                  Name = "Payment")]
    public class Payment {
        [DataMember]
        public int Identifier { get; set; }

        [DataMember]
        public CreditCard creditCard { get; set; }

        [DataMember]
        public double Amount { get; set; }

        [DataMember]
        public PaymentStatus Status { get; set; }

        [DataMember]
        public string Date { get; set; }
    }

    [DataContract(Namespace = "http://isadevops/2021/payment/data/",
                  Name = "RefundRequest")]
    public class RefundRequest {
        [DataMember]
        public int paymentId { get; set; }

        [DataMember]
        public double Amount { get; set; }

        override public string ToString() {
            return "RefundRequest : \n\t paymentId : " + paymentId + "\n\tAmount : " + Amount;
        }
    }

    [DataContract(Namespace = "http://isadevops/2021/payment/data/",
                  Name = "Refund")]
    public class Refund {
        [DataMember]
        public int PaymentIdentifier { get; set; }

        [DataMember]
        public double Amount { get; set; }

        [DataMember]
        public string Date { get; set; }
    }
}