package cli.commands.shopping;

import cli.commands.UserCommand;
import stubs.shopping.CartWebService;

import java.util.List;

public class DisplayCart extends UserCommand {

    @Override
    public String identifier() {
        return "displayCart";
    }

    @Override
    public void exec() throws Exception {
        CartWebService service = shell.system.cartService;
        List<Integer> items = service.displayCustomerCart(shell.userEmail);
        StringBuilder res = new StringBuilder(indent + "Cart:");
        indent += "    ";
        int totalPrice = 0;
        for (int item : items) {
            String type = service.getItemTypeById(shell.userEmail, item);
            String name = service.getItemNameById(shell.userEmail, item);
            double price = service.getItemPriceById(shell.userEmail, item);
            int quantity = service.getItemQuantityById(shell.userEmail, item);
            totalPrice += (price * quantity);
            res.append("\n").append(indent)
                .append(type).append(" -> ")
                .append(name).append('(')
                .append(price).append("€) x ")
                .append(quantity).append(" = ")
                .append(price * quantity).append("€");
        }
        if (items.isEmpty()) {
            System.out.println(res + "\n" + indent + "Empty cart!");
        } else {
            res.append("\n").append(indent).append("TOTAL PRICE: ").append(totalPrice).append("€");
            System.out.println(res);
        }
    }

    @Override
    public String describe() {
        return "# to display a customer cart";
    }
}
