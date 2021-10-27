# CastexSki - Server application

The CastexSki server application implemented as a component-based system that relies on the Java EE 7 framework and divided into multiple submodules.

## Compile

To compile the server:

```bash
mvn clean install
```

## Run

To run the server:

```bash
mvn tomee:run -pl main-server
```

## Structure

- [main-server](main-server): Builds the main server (EAR).
- [common-server](common-server): Stores the common objects (JAR).
- [account-server](account-server): Manages the accounts (EJB).
- [shopping-server](shopping-server): Manages the shopping process (EJB).
- [payment-server](payment-server): Manages the payment process (EJB).
- [resort-server](resort-server): Manages the whole resort (EJB).
- [statistics-server](statistics-server): Manages the statistics (EJB).
- [monitoring-server](monitoring-server): Monitor the whole resort (EJB).
- [notification-server](notification-server): Manages the notifications (EJB).

## Authors

- [João Brilhante](https://github.com/JoaoBrlt)
- [Armand Fargeon](https://github.com/armandfargeon)
- [Ryana Karaki](https://github.com/RyanaKaraki)
- [Ludovic Marti](https://github.com/LudovicMarti)
- [Valentin Roccelli](https://github.com/RoccelliV)
