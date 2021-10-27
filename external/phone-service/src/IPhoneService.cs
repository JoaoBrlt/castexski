using System;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;

using Partner.Data;

/**
 * Phone service interface.
 */
namespace Partner.Service {
    [ServiceContract]
    public interface IPhoneService {
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "messages",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        int SendMessage(Message message);

        [OperationContract]
        [WebInvoke(Method = "DELETE", UriTemplate = "messages")]
        void DeleteMessages();

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "messages/{identifier}",
                   ResponseFormat = WebMessageFormat.Json)]
        Message GetMessage(int identifier);

         [OperationContract]
        [WebInvoke(Method = "DELETE", UriTemplate = "messages/{identifier}")]
        void DeleteMessage(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "messages/to/{to}",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetMessagesTo(String to);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "messages/from/{from}",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetMessagesFrom(String from);
    }
}
