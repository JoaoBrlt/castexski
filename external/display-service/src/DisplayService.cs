using System;
using System.Net;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;
using Partner.Data;

/**
 * Display service.
 */
namespace Partner.Service {
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class DisplayService : IDisplayService {
        /**
         * The list of signs.
         */
        private Dictionary<int, Sign> signs = new Dictionary<int, Sign>();

        /**
         * The counter of signs.
         */
        private int counter = 1;

        public bool ReceiveMessageRequest(int identifier, Request request) {
            if(!signs.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return false;
            }
            var sign = signs[identifier];
            sign.Text = request.Text;
            Console.WriteLine("\"" + sign.GivenName + "\"(" + sign.Identifier + ") displays now \"" + request.Text + "\"");
            return true;
        }

        public Sign FindSignById(int identifier) {
            if(!signs.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return null;
            }
            return signs[identifier];
        }

        public string SwitchSignById(int identifier) {
            if(!signs.ContainsKey(identifier)) {
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                return "not found";
            }
            var sign = signs[identifier];
            if(sign.IsOn) {
                sign.IsOn = false;
                Console.WriteLine("\"" + sign.GivenName + "\"(" + sign.Identifier + ") is now off");
                return "switched off";
            } else {
                sign.IsOn = true;
                Console.WriteLine("\"" + sign.GivenName + "\"(" + sign.Identifier + ") is now on");
                return "switched on";
            }
        }

        public List<int> GetAllSignsIds() {
            return signs.Keys.ToList();
        }

        public int CreateNewSign(Request request) {
            var sign = new Sign();
            sign.Identifier = counter;
            sign.GivenName = request.Text;
            sign.Text = "";
            sign.IsOn = false;
            signs.Add(counter, sign);
            Console.WriteLine(request.Text + ": " + counter);
            return counter++;
        }
    }
}
