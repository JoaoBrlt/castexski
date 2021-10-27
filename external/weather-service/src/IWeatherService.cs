using System;
using System.Collections.Generic;
using System.ServiceModel;
using System.ServiceModel.Web;
using Partner.Data;

/**
 * Weather service interface.
 */
namespace Partner.Service {
    [ServiceContract]
    public interface IWeatherService {
        [OperationContract]
        [WebInvoke(Method = "GET", UriTemplate = "forecasts/{date}/{city}",
                   ResponseFormat = WebMessageFormat.Json)]
        Forecast GetForecastByCity(string date, string city);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "forecasts",
                   RequestFormat = WebMessageFormat.Json,
                   ResponseFormat = WebMessageFormat.Json)]
        void AddForecast(Forecast forecast);
    }
}
