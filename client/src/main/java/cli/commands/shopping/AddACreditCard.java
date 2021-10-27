package cli.commands.shopping;

import cli.commands.UserCommand;

import java.util.List;

public class AddACreditCard extends UserCommand {

    private String name;
    private String number;
    private String securityCode;
    private String expireMonth;
    private String expireYear;
    private boolean saveChoice;

    @Override
    public String identifier() {
        return "addCard";
    }

    @Override
    public void exec() {
        shell.system.cartService.addCreditCard(
            shell.userEmail,
            name,
            number,
            securityCode,
            expireMonth,
            expireYear,
            saveChoice);
    }

    @Override
    public String describe() {
        return "<name> " +
            "<number> " +
            "<securityCode> " +
            "<expireMonth> " +
            "<expireYear> " +
            "<saveCard> " +
            "# to add a customer credit card";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.number = args.get(1);
        this.securityCode = args.get(2);
        this.expireMonth = args.get(3);
        this.expireYear = args.get(4);
        this.saveChoice = Boolean.parseBoolean(args.get(5));
    }
}
