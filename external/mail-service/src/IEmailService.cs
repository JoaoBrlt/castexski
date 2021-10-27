using System;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;

using Partner.Data;

/**
 * Email service interface.
 */
namespace Partner.Service {
    [ServiceContract]
    public interface IEmailService {
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "emails",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        int SendEmail(Email email);

        [OperationContract]
        [WebInvoke(Method = "DELETE", UriTemplate = "emails")]
        void DeleteEmails();

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "emails/{identifier}",
                   ResponseFormat = WebMessageFormat.Json)]
        Email GetEmail(int identifier);

        [OperationContract]
        [WebInvoke(Method = "DELETE", UriTemplate = "emails/{identifier}")]
        void DeleteEmail(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "emails/to/{to}",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetEmailsTo(String to);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "emails/from/{from}",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetEmailsFrom(String from);
    }
}
