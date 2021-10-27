using System;
using System.Net;
using System.IO;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;
using Partner.Data;
using Newtonsoft.Json;

/**
 * Weather service.
 */
namespace Partner.Service {
    // The service is stateful, as it is only a Proof of Concept.
    // Services should be stateless, this is for demonstration purpose only.
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]//, IncludeExceptionDetailInFaults = true)]

    public class WeatherService : IWeatherService {
        public List<Forecast> LoadJson(string date) {
            using (StreamReader r = new StreamReader("reports/" + date + ".json")) {
                string json = r.ReadToEnd();
                return JsonConvert.DeserializeObject<List<Forecast>>(json);
            }
        }

        public void AddForecast(Forecast forecast) {
            Console.WriteLine(" * * New forecast * * \n" + forecast + "\n");
            List<Forecast> forecasts;
            forecasts = File.Exists(@"reports/" + forecast.date + ".json") ? LoadJson(forecast.date) : new List<Forecast>();
            forecasts.Add(forecast);
            string json = JsonConvert.SerializeObject(forecasts);
            File.WriteAllText(@"reports/" + forecast.date + ".json", json);
        }

        public Forecast GetForecastByCity(string date, string city) {
            Console.WriteLine(" * * Forecast Request * * ");
            try {
                Forecast forecast = LoadJson(date).Where(f => f.city.Equals(city)).First();
                Console.WriteLine(forecast);
                return forecast;
            } catch {
                string message = "Bad request: " + (File.Exists(@"reports/" + date + ".json") ? "forecast not found for " + city + " at the given date." : "no forecast at the given date.") + "\n";
                Console.WriteLine("\t" + message);
                WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
                WebOperationContext.Current.OutgoingResponse.StatusDescription = message;
                return null;
            }
        }
    }
}
