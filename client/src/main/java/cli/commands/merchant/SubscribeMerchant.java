package cli.commands.merchant;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class SubscribeMerchant extends Command<CastexSkiAPI> {

    protected String mail;
    protected String resortName;

    @Override
    public String identifier() {
        return "subscribeMerchant";
    }

    @Override
    public void execute() throws Exception {
        shell.system.merchantService.subscribeMerchantToResort(mail,resortName);
    }

    @Override
    public String describe() {
        return "<email> <resortName> # to subscribe a merchant to a resort";
    }

    @Override
    public void load(List<String> args) {
        this.mail = args.get(0);
        this.resortName = args.get(1);
    }
}
