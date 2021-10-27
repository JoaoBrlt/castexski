package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.resort.ResortService;

import java.util.List;

public class DeleteResort extends Command<CastexSkiAPI> {

    private String name;

    @Override
    public String identifier() {
        return "deleteResort";
    }

    @Override
    public void execute() throws Exception {
        ResortService rs = shell.system.resortService;
        String id = rs.findResortByName(name);
        rs.removeResort(id);
    }

    @Override
    public String describe() {
        return "<resortName> # to delete a resort";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
    }
}
