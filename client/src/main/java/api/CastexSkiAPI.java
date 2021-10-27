package api;

import stubs.accessAdding.AccessAddingService;
import stubs.accessAdding.AccessAddingServiceImplService;
import stubs.accessChecking.AccessCheckingService;
import stubs.accessChecking.AccessCheckingServiceImplService;
import stubs.card.CardService;
import stubs.card.CardServiceImplService;
import stubs.catalog.CatalogWebService;
import stubs.catalog.CatalogWebServiceImplService;
import stubs.customer.CustomerService;
import stubs.customer.CustomerServiceImplService;
import stubs.displaypanel.DisplayPanelService;
import stubs.displaypanel.DisplayPanelServiceImplService;
import stubs.merchant.MerchantService;
import stubs.merchant.MerchantServiceImplService;
import stubs.notification.NotificationService;
import stubs.notification.NotificationServiceImplService;
import stubs.pass.PassService;
import stubs.pass.PassServiceImplService;
import stubs.presencestatistics.PresenceStatisticsWebService;
import stubs.presencestatistics.PresenceStatisticsWebServiceImplService;
import stubs.purchasestatistics.PurchaseStatisticsWebService;
import stubs.purchasestatistics.PurchaseStatisticsWebServiceImplService;
import stubs.resort.ResortService;
import stubs.resort.ResortServiceImplService;
import stubs.shopping.CartWebService;
import stubs.shopping.CartWebServiceImplService;
import stubs.user.UserService;
import stubs.user.UserServiceImplService;

import javax.xml.ws.BindingProvider;
import java.net.URL;

public class CastexSkiAPI {
    public CartWebService cartService;
    public UserService userService;
    public CustomerService customerService;
    public MerchantService merchantService;
    public CatalogWebService catalogService;
    public AccessCheckingService accessCheckingService;
    public AccessAddingService accessAddingService;
    public ResortService resortService;
    public PassService passService;
    public CardService cardService;
    public PurchaseStatisticsWebService purchaseStatisticsWebService;
    public PresenceStatisticsWebService presenceStatisticsWebService;
    public NotificationService notificationService;
    public DisplayPanelService displayPanelService;

    public CastexSkiAPI(String host, String port) {
        initCart(host, port);
        initUserS(host, port);
        initCS(host, port);
        initMS(host, port);
        initCard(host, port);
        initPass(host, port);
        initCatalog(host, port);
        initAccessChecking(host, port);
        initAccessAdding(host, port);
        initResort(host, port);
        initPurchaseStatistics(host, port);
        initPresenceStatistics(host, port);
        initNotification(host, port);
        initDisplays(host, port);
    }

    private void initCart(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/CartWS.wsdl");
        System.out.println("#### Instantiating the CartWS Proxy");
        CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
        this.cartService = factory.getCartWebServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/shopping-server/CartWS";
        ((BindingProvider) cartService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initUserS(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/UserWS.wsdl");
        System.out.println("#### Instantiating the UserWS Proxy");
        UserServiceImplService factory = new UserServiceImplService(wsdlLocation);
        this.userService = factory.getUserServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/account-server/UserWS";
        ((BindingProvider) userService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initCS(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/CustomerWS.wsdl");
        System.out.println("#### Instantiating the CustomerWS Proxy");
        CustomerServiceImplService factory = new CustomerServiceImplService(wsdlLocation);
        this.customerService = factory.getCustomerServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/account-server/CustomerWS";
        ((BindingProvider) customerService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initMS(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/MerchantWS.wsdl");
        System.out.println("#### Instantiating the MerchantWS Proxy");
        MerchantServiceImplService factory = new MerchantServiceImplService(wsdlLocation);
        this.merchantService = factory.getMerchantServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/merchant-server/MerchantWS";
        ((BindingProvider) merchantService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initCard(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/CardWS.wsdl");
        System.out.println("#### Instantiating the CardWS Proxy");
        CardServiceImplService factory = new CardServiceImplService(wsdlLocation);
        this.cardService = factory.getCardServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/account-server/CardWS";
        ((BindingProvider) cardService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initPass(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/PassWS.wsdl");
        System.out.println("#### Instantiating the PassWS Proxy");
        PassServiceImplService factory = new PassServiceImplService(wsdlLocation);
        this.passService = factory.getPassServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/account-server/PassWS";
        ((BindingProvider) passService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initCatalog(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/CatalogWS.wsdl");
        System.out.println("#### Instantiating the CatalogWS Proxy");
        CatalogWebServiceImplService factory = new CatalogWebServiceImplService(wsdlLocation);
        this.catalogService = factory.getCatalogWebServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/shopping-server/CatalogWS";
        ((BindingProvider) catalogService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initAccessChecking(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/AccessCheckingWS.wsdl");
        System.out.println("#### Instantiating the AccessCheckingWS Proxy");
        AccessCheckingServiceImplService factory = new AccessCheckingServiceImplService(wsdlLocation);
        this.accessCheckingService = factory.getAccessCheckingServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/resort-server/AccessCheckingWS";
        ((BindingProvider) accessCheckingService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initAccessAdding(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/AccessAddingWS.wsdl");
        System.out.println("#### Instantiating the AccessAddingWS Proxy");
        AccessAddingServiceImplService factory = new AccessAddingServiceImplService(wsdlLocation);
        this.accessAddingService = factory.getAccessAddingServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/resort-server/AccessAddingWS";
        ((BindingProvider) accessAddingService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initResort(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/ResortWS.wsdl");
        System.out.println("#### Instantiating the ResortWS Proxy");
        ResortServiceImplService factory = new ResortServiceImplService(wsdlLocation);
        this.resortService = factory.getResortServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/resort-server/ResortWS";
        ((BindingProvider) resortService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initPurchaseStatistics(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/PurchaseStatisticsWS.wsdl");
        System.out.println("#### Instantiating the PurchaseStatisticsWS Proxy");
        PurchaseStatisticsWebServiceImplService factory = new PurchaseStatisticsWebServiceImplService(wsdlLocation);
        this.purchaseStatisticsWebService = factory.getPurchaseStatisticsWebServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/statistics-server/PurchaseStatisticsWS";
        ((BindingProvider) purchaseStatisticsWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initPresenceStatistics(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/PresenceStatisticsWS.wsdl");
        System.out.println("#### Instantiating the PresenceStatisticsWS Proxy");
        PresenceStatisticsWebServiceImplService factory = new PresenceStatisticsWebServiceImplService(wsdlLocation);
        this.presenceStatisticsWebService = factory.getPresenceStatisticsWebServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/statistics-server/PresenceStatisticsWS";
        ((BindingProvider) presenceStatisticsWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initNotification(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/NotificationWS.wsdl");
        System.out.println("#### Instantiating the NotificationWS Proxy");
        NotificationServiceImplService factory = new NotificationServiceImplService(wsdlLocation);
        this.notificationService = factory.getNotificationServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/notification-server/NotificationWS";
        ((BindingProvider) notificationService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    private void initDisplays(String host, String port) {
        System.out.println("#### Loading the WSDL contract");
        URL wsdlLocation = CastexSkiAPI.class.getResource("/DisplayPanelWS.wsdl");
        System.out.println("#### Instantiating the DisplayPanelWS Proxy");
        DisplayPanelServiceImplService factory = new DisplayPanelServiceImplService(wsdlLocation);
        this.displayPanelService = factory.getDisplayPanelServiceImplPort();
        System.out.println("#### Updating the endpoint address dynamically");
        String address = "http://" + host + ":" + port + "/display-server/DisplayPanelWS";
        ((BindingProvider) displayPanelService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }
}
