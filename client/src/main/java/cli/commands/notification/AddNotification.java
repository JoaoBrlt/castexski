package cli.commands.notification;

import api.CastexSkiAPI;
import cli.framework.Command;

import java.util.List;

public class AddNotification extends Command<CastexSkiAPI> {

    private String channel;
    private String triggerType;
    private String triggerParameter;
    private String triggerCoolDown;
    private String weatherParameter;
    private String target;
    private String targetParameter;
    private String resortName;
    private String subject;
    private String message;

    @Override
    public String identifier() {
        return "addNotification";
    }

    @Override
    public void execute() throws Exception {
        int id = shell.system.notificationService.addNotification(
            channel,
            triggerType,
            triggerParameter,
            triggerCoolDown,
            weatherParameter,
            target,
            targetParameter,
            resortName,
            subject,
            message);
        System.out.println(shell.getIndent()+"notification created with id: "+id);
    }

    @Override
    public String describe() {
        return "<channel> <triggerType> <triggerParameter> <triggerCoolDown> <weatherParameter> <target> <targetParameter> <resortName> <subject> <message> # to create a new notification";
    }

    @Override
    public void load(List<String> args) {
        this.channel = args.get(0);
        this.triggerType = args.get(1);
        this.triggerParameter = args.get(2);
        this.triggerCoolDown = args.get(3);
        this.weatherParameter = args.get(4);
        this.target = args.get(5);
        this.targetParameter = args.get(6);
        this.resortName = args.get(7);
        this.subject = args.get(8);
        this.message = args.get(9);
    }
}
