package cli.commands;


import api.CastexSkiAPI;
import cli.framework.Command;

public class Exit extends Command<CastexSkiAPI> {
    @Override
    public String identifier() {
        return "exit";
    }

    @Override
    public void execute() {}

    @Override
    public String describe() {
        return "# to exit the program";
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }
}
