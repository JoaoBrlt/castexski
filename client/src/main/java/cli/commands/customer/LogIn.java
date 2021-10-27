package cli.commands.customer;

import api.CastexSkiAPI;
import cli.commands.Demo;
import cli.commands.Exit;
import cli.commands.account.DisplayCards;
import cli.commands.account.DisplayPasses;
import cli.commands.account.LinkPassToCard;
import cli.commands.cashier.LinkToPhysicalCard;
import cli.commands.catalog.*;
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
import cli.framework.Command;
import cli.framework.Shell;

import java.util.List;

public class LogIn extends Command<CastexSkiAPI> {

    private String email;

    @Override
    public String identifier() {
        return "login";
    }

    @Override
    public void execute() throws Exception {
        if (email.equalsIgnoreCase("root")) {
            shell.internShell = new Shell<>();
            shell.internShell.system = shell.system;
            shell.internShell.register(
                AddAccess.class,
                AddDisplayToResort.class,
                AddDoubleSkiLift.class,
                AddEntryCard.class,
                AddEntryPass.class,
                AddNotification.class,
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
                DisplayCatalog.class,
                DisplaysText.class,
                Exit.class,
                FindCustomerByMail.class,
                FindSkiLiftByName.class,
                HourlyPresence.class,
                LinkToPhysicalCard.class,
                LogIn.class,
                MerchantBusiness.class,
                MerchantRegister.class,
                MonthlyPresence.class,
                NormalCardSold.class,
                NotificationExists.class,
                RemoveNotification.class,
                ResortPresence.class,
                SubscribeMerchant.class,
                SuperCardSold.class,
                UnsubscribeMerchant.class,
                WeeklyPresence.class,
                YearlyPresence.class
            );
            shell.runInternal(true);
        }
        else if (shell.system.customerService.exists(email)) {
            shell.internShell = new Shell<>();
            shell.internShell.system = shell.system;
            shell.internShell.userEmail = email;
            shell.internShell.register(
                Exit.class,
                AddCardToCart.class,
                AddPassToCart.class,
                RemoveCardFromCart.class,
                RemovePassFromCart.class,
                AddACreditCard.class,
                DisplayCatalog.class,
                DisplayCart.class,
                ValidateCart.class,
                CheckCard.class,
                LinkPassToCard.class,
                DisplayCards.class,
                DisplayPasses.class
            );
            shell.runInternal(false);
            shell.internShell.userEmail = null;
        } else System.out.println(shell.getIndent()+"Unknown user: "+email);
    }

    @Override
    public String describe() {
        return "<email> # to login as a user ({email = root} to login as an employee)";
    }

    @Override
    public void load(List<String> args) {
        this.email = args.get(0);
    }
}
