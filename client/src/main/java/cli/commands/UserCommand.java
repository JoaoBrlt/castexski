package cli.commands;

import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;

public abstract class UserCommand extends Command<CastexSkiAPI> {

    abstract public String identifier();
    abstract public String describe();
    abstract public void exec() throws Exception;

    protected String indent;

    @Override
    public void execute() throws Exception {
        indent = shell.getIndent();
        if (shell.userEmail!=null) {
            exec();
        } else {
            System.out.println(indent+"Please login first!");
        }
    }
}
