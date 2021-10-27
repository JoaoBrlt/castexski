package cli.commands.cashier;

import api.CastexSkiAPI;
import cli.framework.Command;
import stubs.card.CardService;

import java.util.List;

public class LinkToPhysicalCard extends Command<CastexSkiAPI> {

    private String mail;
    private String cardName;
    private String physicalId;

    @Override
    public String identifier() {
        return "linkCard";
    }

    @Override
    public void execute() throws Exception {
        CardService cs = shell.system.cardService;
        List<String> notLinkedCards = cs.getCardsNotPhysicallyLinked(mail);
        for (String cardId : notLinkedCards) {
            if (cs.getCardNameById(cardId).equalsIgnoreCase(cardName)) {
                cs.linkPhysicalCard(cardId, physicalId);
                break;
            }
        }
    }

    @Override
    public String describe() {
        return "<mail> <cardName> <physicalId> # to link a customer card to a physical card";
    }

    @Override
    public void load(List<String> args) {
        this.mail = args.get(0);
        this.cardName = args.get(1);
        this.physicalId = args.get(2);
    }
}
