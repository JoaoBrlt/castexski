package cli.commands.catalog;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.time.Duration;
import java.util.List;

public class AddEntryPass extends Command<CastexSkiAPI> {

    private String name;
    private double regularPrice;
    private double childrenPrice;
    private String duration;
    private boolean isPrivate;

    @Override
    public String identifier() {
        return "addEntryPass";
    }

    @Override
    public void execute() throws Exception {
        shell.system.catalogService.addPass(name,regularPrice,childrenPrice,duration,isPrivate);
    }

    @Override
    public String describe() {
        return "<name> <regularPrice> <childrenPrice> <durationHour> <isPrivateItem> # to add a card entry to the catalog";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.regularPrice = Double.parseDouble(args.get(1));
        this.childrenPrice = Double.parseDouble(args.get(2));
        this.duration = Duration.ofHours(Integer.parseInt(args.get(3))).toString();
        this.isPrivate = Boolean.parseBoolean(args.get(4));
    }
}
