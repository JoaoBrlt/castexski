package cli.commands.account;

import cli.commands.UserCommand;
import stubs.pass.PassService;

import java.time.Duration;
import java.util.List;

public class LinkPassToCard extends UserCommand {

    private String passName;
    private String passDuration;
    private boolean isChild;
    private String physicalId;

    @Override
    public String identifier() {
        return "linkPass";
    }

    @Override
    public void exec() throws Exception {
        PassService ps = shell.system.passService;
        List<String> notLinked = ps.getNotLinkedPass(shell.userEmail);
        for (String ident : notLinked) {
            if (ps.getPassNameById(shell.userEmail, ident).equals(passName)
                && ps.getPassDurationById(shell.userEmail, ident).equals(passDuration)
                && (ps.isChildPassById(shell.userEmail, ident) == isChild)) {
                shell.system.cardService.linkPassToCardOnline(shell.userEmail, physicalId, ident);
                return;
            }
        } throw new Exception("Card not found <"
            + passName + ", " + passDuration + ", forChild: " + isChild
            + "> for " + shell.userEmail);
    }

    @Override
    public String describe() {
        return "<passName> <passDurationHour> <isChild> <physicalId> # to link a pass to a customer card";
    }

    @Override
    public void load(List<String> args) {
        this.passName = args.get(0);
        this.passDuration = Duration.ofHours(Integer.parseInt(args.get(1))).toString();
        this.isChild = Boolean.parseBoolean(args.get(2));
        this.physicalId = args.get(3);
    }
}
