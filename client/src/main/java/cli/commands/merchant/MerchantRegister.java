package cli.commands.merchant;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class MerchantRegister extends Command<CastexSkiAPI> {

    private String firstname;
    private String lastname;
    private String email;
    private String business;

    @Override
    public String identifier() {
        return "merchantRegister";
    }

    @Override
    public void execute() throws Exception {
        shell.system.merchantService.registerMerchant(firstname,lastname,email,business);
    }

    @Override
    public String describe() {
        return "<firstname> <lastname> <email> <business> # to register a new merchant";
    }

    @Override
    public void load(List<String> args) {
        this.firstname = args.get(0);
        this.lastname = args.get(1);
        this.email = args.get(2);
        this.business = args.get(3);
    }
}
