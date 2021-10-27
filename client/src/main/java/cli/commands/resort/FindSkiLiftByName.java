package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;
import stubs.resort.ResortService;

import java.util.List;

public class FindSkiLiftByName extends Command<CastexSkiAPI> {

    private String resortName;
    private String name;

    @Override
    public String identifier() {
        return "findSkiLift";
    }

    @Override
    public void execute() throws Exception {
        ResortService service = shell.system.resortService;
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < Shell.NB_INDENT; i++) { indent.append(Shell.INDENT); }
        System.out.println(indent + name + ": " + service.findSkiLiftByName(service.findResortByName(resortName), name));
    }

    @Override
    public String describe() {
        return "<resortName> <name> # to find and display a ski lift";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
        this.name = args.get(1);
    }
}
