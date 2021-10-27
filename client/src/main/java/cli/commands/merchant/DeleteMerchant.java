package cli.commands.merchant;

import cli.commands.customer.DeleteCustomer;

public class DeleteMerchant extends DeleteCustomer {

    @Override
    public String identifier() {
        return "deleteMerchant";
    }

    @Override
    public void execute() throws Exception {
        shell.system.merchantService.removeMerchant(mail);
    }

    @Override
    public String describe() {
        return "<email> # to remove a merchant";
    }
}
