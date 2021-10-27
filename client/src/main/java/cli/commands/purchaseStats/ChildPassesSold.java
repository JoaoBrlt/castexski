package cli.commands.purchaseStats;

public class ChildPassesSold extends AllPassesSold {

    @Override
    public String identifier() {
        return "childPassesSold";
    }

    @Override
    public void execute() throws Exception {
        int soled = shell.system.purchaseStatisticsWebService.getNumberOfChildPassesBoughtOnDate(date);
        System.out.println(shell.getIndent()+soled+" child passes were sold at "+date);
    }

    @Override
    public String describe() {
        return "<year> <month> <day> # to know how many child passes were sold on a date";
    }
}
