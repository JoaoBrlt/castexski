package cli.commands.catalog;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class DeleteEntryCard extends Command<CastexSkiAPI> {

    private String name;
    private boolean isSuper;

    @Override
    public String identifier() {
        return "deleteEntryCard";
    }

    @Override
    public void execute() throws Exception {
        shell.system.catalogService.deleteCard(name,isSuper);
    }

    @Override
    public String describe() {
        return "<name> <isSuperCartex> # to delete a card entry from the catalog";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.isSuper = Boolean.parseBoolean(args.get(1));
    }
}
