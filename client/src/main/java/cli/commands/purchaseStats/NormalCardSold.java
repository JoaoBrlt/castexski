package cli.commands.purchaseStats;

public class NormalCardSold extends AllPassesSold {

    @Override
    public String identifier() {
        return "normalCardSold";
    }

    @Override
    public void execute() throws Exception {
        int soled = shell.system.purchaseStatisticsWebService.getNumberOfCardsBoughtOnDate(date);
        System.out.println(shell.getIndent()+soled+" normal cards were sold at "+date);
    }

    @Override
    public String describe() {
        return "<year> <month> <day> # to know how many normal cards were sold on a date";
    }
}
