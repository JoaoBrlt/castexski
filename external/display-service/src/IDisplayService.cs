using System;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;

using Partner.Data;

/**
 * Display service interface.
 */
namespace Partner.Service {
    [ServiceContract]
    public interface IDisplayService {
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "signs/{identifier}/display",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        bool ReceiveMessageRequest(int identifier, Request request);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "signs/{identifier}",
                   ResponseFormat = WebMessageFormat.Json)]
        Sign FindSignById(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "signs/{identifier}/switch",
                   ResponseFormat = WebMessageFormat.Json)]
        string SwitchSignById(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "signs",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetAllSignsIds();

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "signs/add",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        int CreateNewSign(Request request);
    }
}
