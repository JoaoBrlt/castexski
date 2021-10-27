package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;
import stubs.accessChecking.AccessCheckingService;
import stubs.resort.ResortService;

import java.util.List;

public class CheckCard extends Command<CastexSkiAPI> {

    private String resortName;
    private String liftName;
    private String physicalId;

    @Override
    public String identifier() {
        return "checkCard";
    }

    @Override
    public void execute() throws Exception {
        ResortService resort = shell.system.resortService;
        AccessCheckingService service = shell.system.accessCheckingService;
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < Shell.NB_INDENT; i++) { indent.append(Shell.INDENT); }
        String liftId = resort.findSkiLiftByName(resort.findResortByName(resortName) ,liftName);
        if (service.checkCard(liftId, physicalId)) {
            System.out.println(indent + liftName + ": " + physicalId + " passed!");
        }
        else {
            System.out.println(indent + liftName + ": " + physicalId + " refused!");
        }
    }

    @Override
    public String describe() {
        return "<resortName> <liftName> <cardId> # to check a card to a ski lift";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.liftName = args.get(1);
        this.physicalId = args.get(2);
    }
}
