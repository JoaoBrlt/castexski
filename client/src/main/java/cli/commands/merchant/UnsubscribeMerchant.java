package cli.commands.merchant;

public class UnsubscribeMerchant extends SubscribeMerchant {

    @Override
    public String identifier() {
        return "unsubscribeMerchant";
    }

    @Override
    public void execute() throws Exception {
        shell.system.merchantService.unsubscribeMerchantFromResort(mail,resortName);
    }

    @Override
    public String describe() {
        return "<email> <resortName> # to unsubscribe a merchant from a resort";
    }

}
