package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.presencestatistics.PresenceStatisticsWebService;

import java.util.List;

public class HourlyPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String liftName;
    private String date;
    private int hour;

    @Override
    public String identifier() {
        return "presenceHourStat";
    }

    @Override
    public void execute() throws Exception {
        PresenceStatisticsWebService ps = shell.system.presenceStatisticsWebService;
        int stat = ps.getPresenceStatisticOnSkiLiftByDateAndHour(resort,liftName,date,hour);
        System.out.println(shell.getIndent()+stat+" persons passed at "+liftName+" on the "+date+" at "+hour);
    }

    @Override
    public String describe() {
        return "<resortName> <skiLiftName> <year> <month> <day> <hour> # to display the the presence statistics for a specified hour";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.liftName = args.get(1);
        this.date = args.get(2)+"-"+args.get(3)+"-"+args.get(4);
        this.hour = Integer.parseInt(args.get(5));
    }
}
