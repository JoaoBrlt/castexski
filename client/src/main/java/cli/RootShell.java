package cli;

import api.CastexSkiAPI;
import cli.commands.Exit;
import cli.commands.account.DisplayCards;
import cli.commands.account.DisplayPasses;
import cli.commands.account.LinkPassToCard;
import cli.commands.cashier.LinkToPhysicalCard;
import cli.commands.catalog.*;
import cli.commands.customer.CustomerRegister;
import cli.commands.customer.DeleteCustomer;
import cli.commands.customer.FindCustomerByMail;
import cli.commands.customer.LogIn;
import cli.commands.merchant.*;
import cli.commands.presenceStats.*;
import cli.commands.purchaseStats.*;
import cli.commands.resort.*;
import cli.commands.shopping.*;
import cli.framework.Shell;

public class RootShell extends Shell<CastexSkiAPI> {

    public RootShell(String host, String port) {
        this.system = new CastexSkiAPI(host, port);
        this.invite = "root";
        register(
            AddAccess.class,
            AddACreditCard.class,
            AddCardToCart.class,
            AddEntryCard.class,
            AddEntryPass.class,
            AddPassToCart.class,
            AddResort.class,
            AddDoubleSkiLift.class,
            AddSkiLift.class,
            AllPassesSold.class,
            APassSold.class,
            CheckCard.class,
            ChildPassesSold.class,
            CustomerRegister.class,
            DailyPresence.class,
            DeleteCustomer.class,
            DeleteEntryCard.class,
            DeleteEntryPass.class,
            DeleteMerchant.class,
            DeleteResort.class,
            DeleteSkiLift.class,
            DisplayCards.class,
            DisplayCart.class,
            DisplayCatalog.class,
            DisplayPasses.class,
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
            RemoveCardFromCart.class,
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

}
