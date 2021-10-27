package cli.commands.shopping;

import cli.commands.UserCommand;

import java.time.Duration;
import java.util.List;

public abstract class CartPassManager extends UserCommand {

    protected String itemName;
    protected String duration;
    protected boolean isChildren;
    protected int quantity;

    @Override
    public void load(List<String> args) {
        itemName = args.get(0);
        duration = Duration.ofHours(Integer.parseInt(args.get(1))).toString();
        isChildren = Boolean.parseBoolean(args.get(2));
        quantity = Integer.parseInt(args.get(3));
    }
}
