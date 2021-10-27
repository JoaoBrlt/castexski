package cli.commands.catalog;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class AddEntryCard extends Command<CastexSkiAPI> {

    private String name;
    private boolean isSuper;
    private double price;
    private boolean isPrivate;

    @Override
    public String identifier() {
        return "addEntryCard";
    }

    @Override
    public void execute() throws Exception {
        shell.system.catalogService.addCard(name,isSuper,price,isPrivate);
    }

    @Override
    public String describe() {
        return "<name> <isSuperCartex> <price> <isPrivateItem> # to add a card entry to the catalog";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.isSuper = Boolean.parseBoolean(args.get(1));
        this.price = Double.parseDouble(args.get(2));
        this.isPrivate = Boolean.parseBoolean(args.get(3));
    }
}
