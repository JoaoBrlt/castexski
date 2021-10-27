package cli.commands.purchaseStats;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class AllPassesSold extends Command<CastexSkiAPI> {

    protected String date;

    @Override
    public String identifier() {
        return "allPassesSold";
    }

    @Override
    public void execute() throws Exception {
        int soled = shell.system.purchaseStatisticsWebService.getNumberOfAllPassesBoughtOnDate(date);
        System.out.println(shell.getIndent()+soled+" passes were sold at "+date);
    }

    @Override
    public String describe() {
        return "<year> <month> <day> # to know how many total passes were sold on a date";
    }

    @Override
    public void load(List<String> args) {
        this.date = args.get(0)+"-"+args.get(1)+"-"+args.get(2);
    }
}
