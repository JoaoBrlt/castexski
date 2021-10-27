using System.Runtime.Serialization;
using System;

/**
 * Business objects.
 */
namespace Partner.Data {
    [DataContract(Namespace = "http://isadevops/2021/message/data/",
                  Name = "Message")]
    public class Message {
        [DataMember]
        public string from { get; set; }

        [DataMember]
        public string to { get; set; }

        [DataMember]
        public string message { get; set; }

        override public string ToString() {
            return "[" + from + ", " + to + ", " + message + "]";
        }
    }
}