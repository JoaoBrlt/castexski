package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class ResortPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String date;

    @Override
    public String identifier() {
        return "resortPresence";
    }

    @Override
    public void execute() throws Exception {
        int stat = shell.system.presenceStatisticsWebService.getPresenceStatisticsOnResortByDate(resort,date);
        System.out.println(shell.getIndent()+stat+" persons went to "+resort+" at "+date);
    }

    @Override
    public String describe() {
        return "<resortName> <year> <month> <day> # to display a resort presence statistics for a specified day";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.date = args.get(1)+"-"+args.get(2)+"-"+args.get(3);
    }
}
