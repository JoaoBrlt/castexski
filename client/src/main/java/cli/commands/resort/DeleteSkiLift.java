package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.resort.ResortService;

import java.util.List;

public class DeleteSkiLift extends Command<CastexSkiAPI> {

    private String resort;
    private String name;

    @Override
    public String identifier() {
        return "deleteSkiLift";
    }

    @Override
    public void execute() throws Exception {
        ResortService rs = shell.system.resortService;
        String resortId = rs.findResortByName(resort);
        String id = rs.findSkiLiftByName(resortId,name);
        rs.removeSkiLift(id);
    }

    @Override
    public String describe() {
        return "<resort> <liftName> # to remove a ski lift";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.name = args.get(1);
    }
}
