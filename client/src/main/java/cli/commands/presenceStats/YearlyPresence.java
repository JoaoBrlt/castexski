package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.presencestatistics.PresenceStatisticsWebService;

import java.util.List;

public class YearlyPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String liftName;
    private int year;

    @Override
    public String identifier() {
        return "yearlyPresence";
    }

    @Override
    public void execute() throws Exception {
        PresenceStatisticsWebService ps = shell.system.presenceStatisticsWebService;
        int stat = ps.getPresenceStatisticOnSkiLiftOnYear(resort,liftName,year);
        System.out.println(shell.getIndent()+stat+" persons passed at "+liftName+" in "+year);
    }

    @Override
    public String describe() {
        return "<resortName> <skiLiftName> <year> # to display a lift presence statistics for a specified year";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.liftName = args.get(1);
        this.year = Integer.parseInt(args.get(2));
    }
}
