package cli.commands.purchaseStats;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class APassSold extends Command<CastexSkiAPI> {

    private String date;
    private String passName;

    @Override
    public String identifier() {
        return "aPassSold";
    }

    @Override
    public void execute() throws Exception {
        int soled = shell.system.purchaseStatisticsWebService.getNumberOfSpecificPassBoughtOnDate(passName,date);
        System.out.println(shell.getIndent()+soled+" "+passName+" were sold at "+date);
    }

    @Override
    public String describe() {
        return "<year> <month> <day> <passName> # to know how many total passes were sold on a date";
    }

    @Override
    public void load(List<String> args) {
        this.date = args.get(0)+"-"+args.get(1)+"-"+args.get(2);
        this.passName = args.get(3);
    }
}
