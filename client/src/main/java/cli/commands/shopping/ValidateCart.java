package cli.commands.shopping;

import cli.commands.UserCommand;

public class ValidateCart extends UserCommand {

    @Override
    public String identifier() {
        return "validateCart";
    }

    @Override
    public void exec() throws Exception {
        shell.system.cartService.validateCustomerCart(shell.userEmail);
    }

    @Override
    public String describe() {
        return "# to validate a customer cart";
    }
}
