package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.accessAdding.AccessAddingService;
import stubs.resort.ResortService;

import java.util.List;

public class AddAccess extends Command<CastexSkiAPI> {

    private String resortName;
    private String passName;
    private String liftName;

    @Override
    public String identifier() {
        return "addAccess";
    }

    @Override
    public void execute() throws Exception {
        AccessAddingService adder = shell.system.accessAddingService;
        ResortService resort = shell.system.resortService;
        String resortId = resort.findResortByName(resortName);
        String liftId = resort.findSkiLiftByName(resortId, liftName);
        adder.addAccess(passName, resortId, liftId);
    }

    @Override
    public String describe() {
        return "<resortName> <passName> <liftName> # to add an access to a ski lift for a pass";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.passName = args.get(1);
        this.liftName = args.get(2);
    }
}
