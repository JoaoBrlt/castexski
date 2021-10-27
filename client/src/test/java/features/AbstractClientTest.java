package features;

import api.CastexSkiAPI;
import cli.RootShell;
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
import cli.commands.resort.*;
import cli.commands.shopping.*;
import cli.framework.Command;
import cucumber.api.CucumberOptions;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@CucumberOptions(features = "src/test/resources")
public class AbstractClientTest {

    private static boolean initiated = false;

    protected static final RootShell shell = new RootShell("localhost", "8080");
    protected static Scanner scanner;
    protected static String resortName;

    protected static final AddResort addResort = new AddResort();
    protected static final DeleteResort deleteResort = new DeleteResort();
    protected static final AddSkiLift addSkiLift = new AddSkiLift();
    protected static final DeleteSkiLift deleteSkiLift = new DeleteSkiLift();
    protected static final FindSkiLiftByName findSkiLiftByName = new FindSkiLiftByName();
    protected static final DisplayCatalog displayCatalog = new DisplayCatalog();
    protected static final AddEntryCard addEntryCard  = new AddEntryCard();
    protected static final AddEntryPass addEntryPass  = new AddEntryPass();
    protected static final DeleteEntryCard deleteEntryCard  = new DeleteEntryCard();
    protected static final DeleteEntryPass deleteEntryPass  = new DeleteEntryPass();
    protected static final CustomerRegister customerRegister  = new CustomerRegister();
    protected static final DeleteCustomer deleteCustomer  = new DeleteCustomer();
    protected static final FindCustomerByMail findCustomerByMail  = new FindCustomerByMail();
    protected static final LogIn logIn  = new LogIn();
    protected static final Exit exit  = new Exit();
    protected static final AddCardToCart addCardToCart  = new AddCardToCart();
    protected static final RemoveCardFromCart removeCardFromCart  = new RemoveCardFromCart();
    protected static final AddPassToCart addPassToCart  = new AddPassToCart();
    protected static final RemovePassFromCart removePassFromCart  = new RemovePassFromCart();
    protected static final DisplayCart displayCart  = new DisplayCart();
    protected static final AddACreditCard addACreditCard  = new AddACreditCard();
    protected static final ValidateCart validateCart  = new ValidateCart();
    protected static final DisplayCards displayCards  = new DisplayCards();
    protected static final DisplayPasses displayPasses  = new DisplayPasses();
    protected static final LinkToPhysicalCard linkToPhysicalCard  = new LinkToPhysicalCard();
    protected static final LinkPassToCard linkPassToCard  = new LinkPassToCard();
    protected static final AddAccess addAccess  = new AddAccess();
    protected static final CheckCard checkCard  = new CheckCard();

    @Rule
    protected static ExpectedException exceptionRule = ExpectedException.none();

    protected static void init() throws IOException {
        if (!initiated) {
            addResort.withShell(shell);
            deleteResort.withShell(shell);
            addSkiLift.withShell(shell);
            deleteSkiLift.withShell(shell);
            findSkiLiftByName.withShell(shell);
            displayCatalog.withShell(shell);
            addEntryCard.withShell(shell);
            addEntryPass.withShell(shell);
            deleteEntryCard.withShell(shell);
            deleteEntryPass.withShell(shell);
            customerRegister.withShell(shell);
            deleteCustomer.withShell(shell);
            findCustomerByMail.withShell(shell);
            logIn.withShell(shell);
            exit.withShell(shell);
            addCardToCart.withShell(shell);
            removeCardFromCart.withShell(shell);
            addPassToCart.withShell(shell);
            removePassFromCart.withShell(shell);
            displayCart.withShell(shell);
            addACreditCard.withShell(shell);
            validateCart.withShell(shell);
            displayCards.withShell(shell);
            displayPasses.withShell(shell);
            linkToPhysicalCard.withShell(shell);
            linkPassToCard.withShell(shell);
            addAccess.withShell(shell);
            checkCard.withShell(shell);
            PipedOutputStream sysOut = new PipedOutputStream();
            scanner = new Scanner(new PipedInputStream(sysOut));
            System.setOut(new PrintStream(sysOut));
            initiated = true;
        }
    }

    protected static void cmd(Command<CastexSkiAPI> command, String... rawArgs) throws Exception {
        List<String> args = Arrays.asList(rawArgs);
        command.load(args);
        command.execute();
    }

    protected static void loggedCmd(Command<CastexSkiAPI> command, String... rawArgs) throws Exception {
        command.withShell(shell.internShell);
        cmd(command, rawArgs);
        command.withShell(shell);
    }
}
