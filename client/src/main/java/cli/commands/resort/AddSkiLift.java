package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.resort.ResortService;

import java.util.List;

public class AddSkiLift extends Command<CastexSkiAPI> {

    protected String resortName;
    protected String liftName;
    protected boolean open;

    @Override
    public String identifier() {
        return "addSkiLift";
    }

    @Override
    public void execute() throws Exception {
        ResortService rs = shell.system.resortService;
        rs.addSkiLift(rs.findResortByName(resortName),liftName,open);
    }

    @Override
    public String describe() {
        return "<resortName> <liftName> <isOpen> # to add a ski lift to a resort";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.liftName = args.get(1);
        this.open = Boolean.parseBoolean(args.get(2));
    }
}
