package cli.commands.shopping;

import cli.commands.UserCommand;

import java.util.List;

public abstract class CartCardManager extends UserCommand {

    protected String itemName;
    protected boolean isSuperCartex;
    protected int quantity;

    @Override
    public void load(List<String> args) {
        itemName = args.get(0);
        isSuperCartex = Boolean.parseBoolean(args.get(1));
        quantity = Integer.parseInt(args.get(2));
    }
}
