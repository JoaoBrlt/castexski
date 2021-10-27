import api.CastexSkiAPI;
import cli.commands.*;
import cli.commands.account.DisplayCards;
import cli.commands.account.DisplayPasses;
import cli.commands.account.LinkPassToCard;
import cli.commands.cashier.LinkToPhysicalCard;
import cli.commands.catalog.*;
import cli.commands.customer.CustomerRegister;
import cli.commands.customer.DeleteCustomer;
import cli.commands.customer.FindCustomerByMail;
import cli.commands.customer.LogIn;
import cli.commands.displays.AddDisplayToResort;
import cli.commands.displays.DisplaysText;
import cli.commands.merchant.*;
import cli.commands.notification.AddNotification;
import cli.commands.notification.NotificationExists;
import cli.commands.notification.RemoveNotification;
import cli.commands.presenceStats.*;
import cli.commands.purchaseStats.*;
import cli.commands.resort.*;
import cli.commands.shopping.*;
import cli.framework.Shell;

public class RootCLI extends Shell<CastexSkiAPI> {

    public RootCLI(String host, String port) {
        this.system  = new CastexSkiAPI(host, port);
        this.invite  = "root";
        // Registering the command available for the user
        register(
            AddAccess.class,
            AddACreditCard.class,
            AddCardToCart.class,
            AddDisplayToResort.class,
            AddDoubleSkiLift.class,
            AddEntryCard.class,
            AddEntryPass.class,
            AddNotification.class,
            AddPassToCart.class,
            AddResort.class,
            AddSkiLift.class,
            AllPassesSold.class,
            APassSold.class,
            CheckCard.class,
            ChildPassesSold.class,
            CloseResort.class,
            CustomerRegister.class,
            DailyPresence.class,
            DeleteCustomer.class,
            DeleteEntryCard.class,
            DeleteEntryPass.class,
            DeleteMerchant.class,
            DeleteResort.class,
            DeleteSkiLift.class,
            Demo.class,
            DisplayCards.class,
            DisplayCart.class,
            DisplayCatalog.class,
            DisplayPasses.class,
            DisplaysText.class,
            Exit.class,
            FindCustomerByMail.class,
            FindSkiLiftByName.class,
            HourlyPresence.class,
            LinkPassToCard.class,
            LinkToPhysicalCard.class,
            LogIn.class,
            MerchantBusiness.class,
            MerchantRegister.class,
            MonthlyPresence.class,
            NormalCardSold.class,
            NotificationExists.class,
            RemoveCardFromCart.class,
            RemoveNotification.class,
            RemovePassFromCart.class,
            ResortPresence.class,
            SubscribeMerchant.class,
            SuperCardSold.class,
            UnsubscribeMerchant.class,
            ValidateCart.class,
            WeeklyPresence.class,
            YearlyPresence.class
        );
    }

    public static void main(String[] args) {
        String host = ( args.length == 0 ? "localhost" : args[0] );
        String port = ( args.length < 2  ? "8080"      : args[1] );
        System.out.println("\n\nStarting CastexSki root CLI");
        System.out.println("  - Remote server: " + host);
        System.out.println("  - Port number:   " + port);
        RootCLI rootCLI = new RootCLI(host, port);
        rootCLI.run();
        System.out.println("Exiting CastexSki CLI\n\n");
    }
}
