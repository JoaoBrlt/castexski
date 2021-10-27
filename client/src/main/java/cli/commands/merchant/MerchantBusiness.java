package cli.commands.merchant;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class MerchantBusiness extends Command<CastexSkiAPI> {

    private String mail;

    @Override
    public String identifier() {
        return "business";
    }

    @Override
    public void execute() throws Exception {
        String business = shell.system.merchantService.getMerchantBusiness(mail);
        System.out.println(shell.getIndent()+mail+" business is: "+business);
    }

    @Override
    public String describe() {
        return "<email> # to know a merchant business";
    }

    @Override
    public void load(List<String> args) {
        this.mail = args.get(0);
    }
}
