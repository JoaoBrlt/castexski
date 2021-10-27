package cli.commands.presenceStats;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.presencestatistics.PresenceStatisticsWebService;

import java.util.List;

public class MonthlyPresence extends Command<CastexSkiAPI> {

    private String resort;
    private String liftName;
    private int month;

    @Override
    public String identifier() {
        return "monthlyPresence";
    }

    @Override
    public void execute() throws Exception {
        PresenceStatisticsWebService ps = shell.system.presenceStatisticsWebService;
        int stat = ps.getPresenceStatisticOnSkiLiftOnMonth(resort,liftName,month);
        System.out.println(shell.getIndent()+stat+" persons passed at "+liftName+" in month "+month);
    }

    @Override
    public String describe() {
        return "<resortName> <skiLiftName> <month> # to display a lift presence statistics for a specified month";
    }

    @Override
    public void load(List<String> args) {
        this.resort = args.get(0);
        this.liftName = args.get(1);
        this.month = Integer.parseInt(args.get(2));
    }
}
