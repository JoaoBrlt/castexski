package cli.commands.shopping;

public class AddCardToCart extends CartCardManager {

    @Override
    public String identifier() {
        return "orderCards";
    }

    @Override
    public void exec() throws Exception {
        shell.system.cartService.addCardToCustomerCart(shell.userEmail, itemName, isSuperCartex, quantity);
    }

    @Override
    public String describe() {
        return "<name> <is_SuperCartex> <quantity> # to add cards in a cart";
    }
}
