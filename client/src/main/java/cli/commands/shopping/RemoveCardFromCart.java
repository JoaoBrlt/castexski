package cli.commands.shopping;

public class RemoveCardFromCart extends CartCardManager {

    @Override
    public String identifier() {
        return "removeCards";
    }

    @Override
    public void exec() throws Exception {
        shell.system.cartService.removeCardFromCustomerCart(shell.userEmail, itemName,isSuperCartex,quantity);
    }

    @Override
    public String describe() {
        return "<name> <is_SuperCartex> <quantity> # to remove cards from a cart";
    }
}
