package cli.commands.customer;


import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;
import stubs.customer.CustomerService;

import java.util.List;

public class FindCustomerByMail extends Command<CastexSkiAPI> {

    private String mail;

    @Override
    public String identifier() {
        return "find";
    }

    @Override
    public void execute() throws Exception {
        CustomerService service = shell.system.customerService;
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < Shell.NB_INDENT; i++) { indent.append(Shell.INDENT); }
        if (service.exists(mail)) {
            String res = indent + "- name : " + service.getUserFullName(mail)
                + '\n' + indent + "- mail : " + mail
                + '\n' + indent + "- credit card :";
            if(service.hasCreditCard(mail)) {
                res += "\n" + indent + "    name = " + service.getCreditCardName(mail)
                    + '\n' + indent + "    number = " + service.getCreditCardNumber(mail)
                    + '\n' + indent + "    date = " + service.getCreditCardDate(mail);
            } else {
                res += " null";
            }
            System.out.println(res);
        }
        else {
            System.out.println(indent + "Unknown user: " + mail);
        }
    }

    @Override
    public String describe() {
        return "<email> # to find and display a customer";
    }

    @Override
    public void load(List<String> args) {
        this.mail = args.get(0);
    }

}
