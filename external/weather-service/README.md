# CastexSki - Weather service

Simulates a weather service.

## Compile

To compile the weather service:

```bash
./compile.sh
```

## Run

1. To run the weather service:

```bash
mono server.exe
```

2. To run the weather service on a given port:

```bash
mono server.exe /port 9494
```

3. To run the weather service in a standalone mode:

```bash
mono server.exe /standalone
```

## Deploy

To deploy the weather service to the Artifactory:

```bash
./deploy.sh
```

## Using the API
### Add a forecast

To add a forecast to the database, a POST request must be sent to the path /newForecast with a JSON formatted as the example bellow.
```bash
{
"City": "PolyVille",
"Date": "27042050",
"TemperatureCelsius": -10.0,
"PrecipitationMm": 0.0,
"WindKph": 15,
"SnowCm": 30,
"PowderAlert": true
}
```
The forecasts are stored by date in the folder reports

### Request a forecast

To obtain a forecast from the API, a GET request must be sent to the path /forecasts/{date}/{city} where {date} must follow the format ddMMyyyy and {city} by any string.
```bash
http://localhost:9095/forecasts/27042050/PolyVille
```

Response example:

```bash
{
"city": "PolyVille",
"date": "27042050",
"temperatureCelsius": -10.0,
"precipitationMm": 0.0,
"windKph": 15,
"snowCm": 30,
"powderAlert": true
}

```
## Authors

- [João Brilhante](https://github.com/JoaoBrlt)
- [Armand Fargeon](https://github.com/armandfargeon)
- [Ryana Karaki](https://github.com/RyanaKaraki)
- [Ludovic Marti](https://github.com/LudovicMarti)
- [Valentin Roccelli](https://github.com/RoccelliV)
