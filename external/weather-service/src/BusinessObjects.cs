using System.Runtime.Serialization;
using System;

/**
 * Business objects.
 */
namespace Partner.Data {
    [DataContract(Namespace = "http://isadevops/2021/weather/data/",
                  Name = "Forecast")]
    public class Forecast {
        [DataMember]
        public string city { get; set; }

        [DataMember]
        public string date { get; set; }

        [DataMember]
        public double temperatureCelsius { get; set; }

        [DataMember]
        public double precipitationMm { get; set; }

        [DataMember]
        public int windKph { get; set; }

        [DataMember]
        public int snowCm { get; set; }

        [DataMember]
        public bool powderAlert { get; set; }

        override public string ToString() {
            return "\tCity: " + city + "\n\tDate: " + date + "\n\tT°C: " + temperatureCelsius +
            "\n\tPrecipitation in mm: " + precipitationMm + "\n\tWind Km/h: " + windKph + "\n\tSnow: " + snowCm + "\n\tPowder alert: " + powderAlert + "\n";
        }
    }
}
