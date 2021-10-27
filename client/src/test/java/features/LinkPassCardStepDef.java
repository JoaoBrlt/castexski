package features;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.io.IOException;
import java.time.Duration;

public class LinkPassCardStepDef extends AbstractClientTest {

    public LinkPassCardStepDef() throws IOException {
        init();
    }

    @And("an employee links (.*) (.*) card to the physical (.*)")
    public void linkToPhysical(String mail, String card, String physical) throws Exception {
        cmd(linkToPhysicalCard, mail, card, physical);
    }

    @And("he links his (.*) which lasts (.*) hours and with children at (.*) to the physical (.*)")
    public void linkPassToCard(String name, String duration, String isChildren, String physical) throws Exception {
        loggedCmd(linkPassToCard, name, duration, isChildren, physical);
    }

    @Then("for (.*) card, his account displays (.*) linked to (.*)")
    public void checkLinkedCard(String lines, String name, String physical) throws Exception {
        loggedCmd(displayCards);
        String expected = (name+" | card: "+physical).toLowerCase();
        ShoppingItemsStepDef.checkAccountPassOrCard(Integer.parseInt(lines),expected);
    }

    @Then("for (.*) pass, his account displays (.*) of duration (.*) hours linked to (.*)")
    public void checkLinkedPass(String lines, String name, String duration, String physical) throws Exception {
        int linesNb = Integer.parseInt(lines);
        String durationString = Duration.ofHours(Integer.parseInt(duration)).toString();
        loggedCmd(displayPasses);
        String expected = (name+": "+durationString+" [not activated] | card: "+physical);
        ShoppingItemsStepDef.checkAccountPassOrCard(linesNb,expected.toLowerCase());
    }

}
