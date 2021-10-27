package cli.commands.shopping;

public class AddPassToCart extends CartPassManager {

    @Override
    public String identifier() {
        return "orderPasses";
    }

    @Override
    public void exec() throws Exception {
        shell.system.cartService.addPassToCustomerCart(shell.userEmail, itemName,duration,isChildren,quantity);
    }

    @Override
    public String describe() {
        return "<name> <durationHours> <isChildren> <quantity> # to add passes in a cart";
    }
}
