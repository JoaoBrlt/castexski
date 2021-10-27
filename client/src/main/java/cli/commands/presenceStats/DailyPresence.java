package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.presencestatistics.PresenceStatisticsWebService;

import java.util.List;

public class DailyPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String liftName;
    private String date;

    @Override
    public String identifier() {
        return "dailyPresence";
    }

    @Override
    public void execute() throws Exception {
        PresenceStatisticsWebService ps = shell.system.presenceStatisticsWebService;
        int stat = ps.getPresenceStatisticOnSkiLiftByDate(resort,liftName,date);
        System.out.println(shell.getIndent()+stat+" persons passed at "+liftName+" at "+date);
    }

    @Override
    public String describe() {
        return "<resortName> <skiLiftName> <year> <month> <day> # to display a lift presence statistics for a specified day";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.liftName = args.get(1);
        this.date = args.get(2)+"-"+args.get(3)+"-"+args.get(4);
    }
}
