package cli.commands.account;

import cli.commands.UserCommand;
import stubs.card.CardService;

import java.util.List;

public class DisplayCards extends UserCommand {

    private CardService cs;

    @Override
    public String identifier() {
        return "displayCards";
    }

    @Override
    public void exec() throws Exception {
        this.cs = shell.system.cardService;
        displayLinked();
        displayNotLinked();
    }

    private void displayLinked() throws Exception {
        List<String> linkedCards = cs.getCardsPhysicallyLinked(shell.userEmail);
        StringBuilder cards = new StringBuilder(indent + "Linked cards:");
        for (String cardId : linkedCards) {
            cards.append("\n").append(indent).append("    ").append(aCard(cardId, true));
        }
        System.out.println(cards);
    }

    private void displayNotLinked() throws Exception {
        List<String> notLinkedCards = cs.getCardsNotPhysicallyLinked(shell.userEmail);
        StringBuilder cards = new StringBuilder(indent + "Not linked cards:");
        for (String cardId : notLinkedCards) {
            cards.append("\n").append(indent).append("    ").append(aCard(cardId, false));
        }
        System.out.println(cards);
    }

    private String aCard(String cardId, boolean linked) throws Exception {
        return "[" + cs.getCardTypeById(cardId) + "] "
            + cs.getCardNameById(cardId) + " | card: "
            + ((linked)?cs.getPhysicalCardIdById(cardId):"null");
    }

    @Override
    public String describe() {
        return "# to display the cards of a customer";
    }
}
