package cli.commands.notification;

public class RemoveNotification extends NotificationExists {

    @Override
    public String identifier() {
        return "removeNotification";
    }

    @Override
    public void execute() throws Exception {
        shell.system.notificationService.removeNotification(id);
    }

    @Override
    public String describe() {
        return "<identifier> # to remove a notification";
    }
}
