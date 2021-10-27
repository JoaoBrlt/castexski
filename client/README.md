# CastexSki - Client application

The CastexSki client application.

## Compile

To compile the client and update the stubs:

```bash
mvn clean package
```

## Update stubs

To update stubs from .wsdl files:

```bash
mvn cxf-codegen:wsdl2java
```

## Run

To run the client:

```bash
mvn exec:java
```

## Update

To update the client after a server web service modification, you should execute the following steps:

1. Download or copy the modified service from its remote web-page adding ```?wsdl``` at the end of the address.

2. Place the new ```.wsdl``` service file into ```src/main/resources/``` replacing the old one.
    **If there is no old one**, either
  - the name changed. In that case, rename the new as the old **OR** keep the new one but then don't forget to:
    - delete the old file.
    - refactor the client API ```CastexSkiAPI.java```, maven setup ```pom.xml``` and all the concerned imports in the client's classes.
  - or it is a new service. In that case:
    - add a new ```<wsdlOption>``` in the ```pom.xml```
    - add the new service initiator in the ```CastexSkiAPI.java```
    
3. Compile to update java from WSDLs.

### Current services addresses

- **CartWS**: http://localhost:8080/shopping-server/CartWS
- **CatalogWS**: http://localhost:8080/shopping-server/CatalogWS
- **UserWS**: http://localhost:8080/account-server/UserWS
- **CustomerWS**: http://localhost:8080/account-server/CustomerWS
- **MerchantWS**: http://localhost:8080/account-server/MerchantWS
- **CardWS**: http://localhost:8080/account-server/CardWS
- **PassWS**: http://localhost:8080/account-server/PassWS
- **AccessAddingWS**: http://localhost:8080/resort-server/AccessAddingWS
- **AccessCheckingWS**: http://localhost:8080/resort-server/AccessCheckingWS
- **ResortWS**: http://localhost:8080/resort-server/ResortWS
- **PurchaseStatisticsWS**: http://localhost:8080/statistics-server/PurchaseStatisticsWS
- **PresenceStatisticsWS**: http://localhost:8080/statistics-server/PresenceStatisticsWS
- **NotificationWS**: http://localhost:8080/notification-server/NotificationWS
- **DisplayPanelWS**: http://localhost:8080/display-server/DisplayPanelWS

## Add commands to the CLI

To add a command when its java class is ready, just add ```NewCommand.class``` as an argument of the ```register``` method into the ```src/main/java/RootCLI.java``` (and others runnable classes or sub-CLIs) constructor. 
Then, compile and run the client.

## Authors

- [João Brilhante](https://github.com/JoaoBrlt)
- [Armand Fargeon](https://github.com/armandfargeon)
- [Ryana Karaki](https://github.com/RyanaKaraki)
- [Ludovic Marti](https://github.com/LudovicMarti)
- [Valentin Roccelli](https://github.com/RoccelliV)
