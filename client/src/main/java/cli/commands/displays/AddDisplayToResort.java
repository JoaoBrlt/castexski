package cli.commands.displays;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class AddDisplayToResort extends Command<CastexSkiAPI> {

    private String resortName;

    @Override
    public String identifier() {
        return "addDisplayToResort";
    }

    @Override
    public void execute() throws Exception {
        shell.system.displayPanelService.addDisplayPanelToResort(
            shell.system.resortService.findResortByName(resortName));
    }

    @Override
    public String describe() {
        return "<resortName> # to add a display panel to a resort";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
    }
}
