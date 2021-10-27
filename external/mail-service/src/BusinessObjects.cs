using System.Runtime.Serialization;
using System;

/**
 * Business objects.
 */
namespace Partner.Data {
    [DataContract(Namespace = "http://isadevops/2021/email/data/",
                  Name = "Email")]
    public class Email {
        [DataMember]
        public string from { get; set; }

        [DataMember]
        public string to { get; set; }

        [DataMember]
        public string subject { get; set; }

        [DataMember]
        public string body { get; set; }

        override public string ToString() {
            return "[" + from + ", " + to + ", " + subject + ", " + body + "]";
        }
    }
}