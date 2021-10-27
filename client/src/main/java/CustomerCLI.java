import api.CastexSkiAPI;
import cli.commands.Exit;
import cli.commands.catalog.DisplayCatalog;
import cli.commands.customer.CustomerRegister;
import cli.commands.customer.LogIn;
import cli.commands.merchant.MerchantRegister;
import cli.commands.resort.*;
import cli.framework.Shell;

public class CustomerCLI extends Shell<CastexSkiAPI> {

    public CustomerCLI(String host, String port) {
        this.system  = new CastexSkiAPI(host, port);
        this.invite  = "customer";
        // Registering the command available for the customer
        register(
            Exit.class,
            CustomerRegister.class,
            MerchantRegister.class,
            DisplayCatalog.class,
            CheckCard.class,
            LogIn.class
        );
    }

    public static void main(String[] args) {
        String host = ( args.length == 0 ? "localhost" : args[0] );
        String port = ( args.length < 2  ? "8080"      : args[1] );
        System.out.println("\n\nStarting CastexSki customer CLI");
        System.out.println("  - Remote server: " + host);
        System.out.println("  - Port number:   " + port);
        CustomerCLI main = new CustomerCLI(host, port);
        main.run();
        System.out.println("Exiting CastexSki customer CLI\n\n");
    }
}
