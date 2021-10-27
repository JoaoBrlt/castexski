package cli.commands.account;

import cli.commands.UserCommand;
import stubs.pass.PassService;

import java.util.List;

public class DisplayPasses extends UserCommand {

    private PassService ps;

    @Override
    public String identifier() {
        return "displayPasses";
    }

    @Override
    public void exec() throws Exception {
        this.ps = shell.system.passService;
        displayLinked();
        displayNotLinked();
    }

    private void displayLinked() throws Exception {
        List<String> linkedPasses = ps.getLinkedPass(shell.userEmail);
        StringBuilder passes = new StringBuilder(indent + "Linked passes:");
        for (String cardId : linkedPasses) {
            passes.append("\n").append(indent).append("    ").append(aPass(cardId, true));
        }
        System.out.println(passes);
    }

    private void displayNotLinked() throws Exception {
        List<String> notLinkedPasses = ps.getNotLinkedPass(shell.userEmail);
        StringBuilder passes = new StringBuilder(indent + "Not linked passes:");
        for (String cardId : notLinkedPasses) {
            passes.append("\n").append(indent).append("    ").append(aPass(cardId, false));
        }
        System.out.println(passes);
    }

    private String aPass(String passId, boolean linked) throws Exception {
        return ps.getPassNameById(shell.userEmail, passId) + ": "
            + ps.getPassDurationById(shell.userEmail, passId) + " ["
            + ((ps.isActivatedPassById(shell.userEmail, passId))?
            "activated from " + ps.getPassStartDateById(shell.userEmail, passId)
                + "to " + ps.getPassEndDateById(shell.userEmail, passId)
            :"not activated") + "] | card: "
            + ((linked)?ps.getPassPhysicalCardLinkedById(shell.userEmail, passId):"null");
    }

    @Override
    public String describe() {
        return "# to display the passes of a customer";
    }
}
