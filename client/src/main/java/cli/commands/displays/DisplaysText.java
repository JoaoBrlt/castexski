package cli.commands.displays;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.displaypanel.DisplayPanelService;

import java.util.List;

public class DisplaysText extends Command<CastexSkiAPI> {

    private String resortName;
    private String message;

    @Override
    public String identifier() {
        return "displaysText";
    }

    @Override
    public void execute() throws Exception {
        DisplayPanelService ds = shell.system.displayPanelService;
        String resortId = shell.system.resortService.findResortByName(resortName);
        List<String> panelIds = ds.findDisplaysByResort(resortId);
        for (String id : panelIds) {
            ds.modifyDisplayedText(id, message);
        }
    }

    @Override
    public String describe() {
        return "<resortName> <message> # to modify the display texts in a resort";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.message = args.get(1);
    }
}
