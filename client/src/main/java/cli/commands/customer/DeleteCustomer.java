package cli.commands.customer;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class DeleteCustomer extends Command<CastexSkiAPI> {

    protected String mail;

    @Override
    public String identifier() {
        return "deleteCustomer";
    }

    @Override
    public void execute() throws Exception {
        shell.system.customerService.deleteCustomer(mail);
    }

    @Override
    public String describe() {
        return "<email> # to remove a customer";
    }

    @Override
    public void load(List<String> args) {
        this.mail = args.get(0);
    }
}
