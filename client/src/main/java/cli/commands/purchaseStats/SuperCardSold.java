package cli.commands.purchaseStats;

public class SuperCardSold extends AllPassesSold {

    @Override
    public String identifier() {
        return "superCardSold";
    }

    @Override
    public void execute() throws Exception {
        int soled = shell.system.purchaseStatisticsWebService.getNumberOfSuperCartexBoughtOnDate(date);
        System.out.println(shell.getIndent()+soled+" superCartex were sold at "+date);
    }

    @Override
    public String describe() {
        return "<year> <month> <day> # to know how many superCartex were sold on a date";
    }
}
