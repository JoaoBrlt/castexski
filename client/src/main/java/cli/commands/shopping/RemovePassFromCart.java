package cli.commands.shopping;

public class RemovePassFromCart extends CartPassManager {

    @Override
    public String identifier() {
        return "removePasses";
    }

    @Override
    public void exec() throws Exception {
        shell.system.cartService.removePassFromCustomerCart(shell.userEmail, itemName,duration,isChildren,quantity);
    }

    @Override
    public String describe() {
        return "<name> <duration_in_days> <is_children> <quantity> # to remove cards from a cart";
    }
}
