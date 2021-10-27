package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.presencestatistics.PresenceStatisticsWebService;

import java.util.List;

public class WeeklyPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String liftName;
    private int week;

    @Override
    public String identifier() {
        return "weeklyPresence";
    }

    @Override
    public void execute() throws Exception {
        PresenceStatisticsWebService ps = shell.system.presenceStatisticsWebService;
        int stat = ps.getPresenceStatisticOnSkiLiftOnWeek(resort,liftName,week);
        System.out.println(shell.getIndent()+stat+" persons passed at "+liftName+" in week "+week);
    }

    @Override
    public String describe() {
        return "<resortName> <skiLiftName> <week> # to display a lift presence statistics for a specified week";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.liftName = args.get(1);
        this.week = Integer.parseInt(args.get(2));
    }
}
