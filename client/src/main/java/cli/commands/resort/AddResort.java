package cli.commands.resort;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class AddResort extends Command<CastexSkiAPI> {

    private String name;
    private String mail;
    private boolean open;
    private String city;

    @Override
    public String identifier() {
        return "addResort";
    }

    @Override
    public void execute() throws Exception {
        shell.system.resortService.addResort(name,mail,open,city);
    }

    @Override
    public String describe() {
        return "<resortName> <resortMail> <isOpen> <city> # to add a resort";
    }

    @Override
    public void load(List<String> args) {
        this.name = args.get(0);
        this.mail = args.get(1);
        this.open = Boolean.parseBoolean(args.get(2));
        this.city = args.get(3);
    }
}
