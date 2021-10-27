package cli.commands.customer;


import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class CustomerRegister extends Command<CastexSkiAPI> {

    private String firstname;
    private String lastname;
    private String email;

    @Override
    public String identifier() {
        return "register";
    }

    @Override
    public void execute() throws Exception {
        shell.system.customerService.register(firstname, lastname, email);
    }

    @Override
    public String describe() {
        return "<firstname> <lastname> <email> # to register a new customer";
    }

    @Override
    public void load(List<String> args) {
        this.firstname = args.get(0);
        this.lastname = args.get(1);
        this.email = args.get(2);
    }
}
