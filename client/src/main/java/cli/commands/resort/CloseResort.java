package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.resort.ResortService;

import java.util.List;

public class CloseResort extends Command<CastexSkiAPI> {

    private String resortName;

    @Override
    public String identifier() {
        return "closeResort";
    }

    @Override
    public void execute() throws Exception {
        ResortService rs = shell.system.resortService;
        rs.changeResortOpenness(rs.findResortByName(resortName),false);
        System.out.println(shell.getIndent()+resortName+" is now closed!");
    }

    @Override
    public String describe() {
        return "<resortName> # to close the resort";
    }

    @Override
    public void load(List<String> args) {
        this.resortName = args.get(0);
    }
}
