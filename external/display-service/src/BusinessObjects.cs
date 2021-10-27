using System.Runtime.Serialization;
using System;

/**
 * Business objects.
 */
namespace Partner.Data {
    [DataContract(Namespace = "http://isadevops/2021/display/data/",
                  Name = "Sign")]
    public class Sign {
        [DataMember]
        public string GivenName { get; set; }

        [DataMember]
        public string Text { get; set; }

        [DataMember]
        public bool IsOn { get; set; }

        [DataMember]
        public int Identifier { get; set; }

        override public string ToString() {
            return "[" + Identifier + ", " + IsOn + ", " + GivenName + ", " + Text + "]";
        }
    }

    [DataContract(Namespace = "http://isadevops/2021/display/data/",
                  Name = "Request")]
    public class Request {
        [DataMember]
        public string Text { get; set; }

        override public string ToString() {
            return "[" + Text + "]";
        }
    }
}
