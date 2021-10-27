package cli.commands.notification;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class NotificationExists extends Command<CastexSkiAPI> {

    protected int id;

    @Override
    public String identifier() {
        return "notificationExists";
    }

    @Override
    public void execute() throws Exception {
        boolean exists = shell.system.notificationService.notificationExists(id);
        System.out.println(shell.getIndent()+((exists)?"notification exists: ":"notification doesn't exist: ")+id);
    }

    @Override
    public String describe() {
        return "<identifier> # to know if a notification exists";
    }

    @Override
    public void load(List<String> args) {
        this.id = Integer.parseInt(args.get(0));
    }
}
