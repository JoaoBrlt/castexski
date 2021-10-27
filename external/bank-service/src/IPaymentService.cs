using System;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;

using Partner.Data;

/**
 * Payment service interface.
 */
namespace Partner.Service {
    [ServiceContract]
    public interface IPaymentService {
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "pay",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        int ReceivePaymentRequest(PaymentRequest request);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "payments/{identifier}",
                   ResponseFormat = WebMessageFormat.Json)]
        Payment FindPaymentById(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "payments",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetAllPaymentIds();

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "refund",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        int ReceiveRefundRequest(RefundRequest request);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "refunds/{identifier}",
                   ResponseFormat = WebMessageFormat.Json)]
        Refund FindRefundById(int identifier);

        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "refunds",
                   ResponseFormat = WebMessageFormat.Json)]
        List<int> GetAllRefundIds();
    }
}
