package cli.commands.catalog;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.time.Duration;
import java.util.List;

public class DeleteEntryPass extends Command<CastexSkiAPI> {

    private String name;
    private String duration;

    @Override
    public String identifier() {
        return "deleteEntryPass";
    }

    @Override
    public void execute() throws Exception {
        shell.system.catalogService.deletePass(name,duration);
    }

    @Override
    public String describe() {
        return "<name> <durationHour> # to delete a pass entry from the catalog";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.duration = Duration.ofHours(Integer.parseInt(args.get(1))).toString();
    }
}
