package features;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.time.Duration;

import static org.junit.Assert.*;

public class InitiateCatalogStepDef extends AbstractClientTest {

    public InitiateCatalogStepDef() throws IOException {
        init();
    }

    @Then("the catalog is empty")
    public void emptyCatalog() throws Exception {
        cmd(displayCatalog);
        String res = scanner.nextLine();
        assertTrue(res.contains("Empty catalog!"));
    }

    @When("a new entryCard named (.*) for which super card is (.*) and cost is (.*) is created")
    public void addEntryCard(String name, String isSuper, String price) throws Exception {
        cmd(addEntryCard, name, isSuper, price, "false");
    }

    @When("a new entryPass named (.*) which lasts (.*) hours and costs (.*) for children and (.*) for adults is created")
    public void addEntryPass(String name, String duration, String childrenPrice, String adultPrice) throws Exception {
        cmd(addEntryPass, name, adultPrice, childrenPrice, duration, "false");
    }

    @Then("for (.*) item, the catalog displays the (.*) (.*) for price (.*)")
    public void entryCardExists(String lines, String type, String name, String price) throws Exception {
        int linesNb = Integer.parseInt(lines);
        String needed = ("["+type+"]:"+name+" -> adults: "+price+".0€ | children: "+price+".0€").toLowerCase();
        checkExistence(linesNb, needed);
    }

    @Then("for (.*) item, the catalog displays the pass (.*) which lasts (.*) hours and costs (.*) for children and (.*) for adults")
    public void entryPassExists(String lines, String name, String duration, String childrenPrice, String adultPrice) throws Exception {
        int linesNb = Integer.parseInt(lines);
        String d = Duration.ofHours(Integer.parseInt(duration)).toString();
        String needed = ("[pass/"+d+"]:"+name+" -> adults: "+adultPrice+".0€ | children: "+childrenPrice+".0€").toLowerCase();
        checkExistence(linesNb, needed);
    }

    @And("the entryCard named (.*) for which super card is (.*) is removed")
    public void deleteEntryCard(String name, String isSuper) throws Exception {
        cmd(deleteEntryCard, name, isSuper);
    }

    @And("the entryPass named (.*) which lasts (.*) hours is removed")
    public void deleteEntryPass(String name, String duration) throws Exception {
        cmd(deleteEntryPass, name, duration);
    }

    private void checkExistence(int lines, String expected) throws Exception {
        cmd(displayCatalog);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i <= lines; i++) res.append(scanner.nextLine().toLowerCase()).append("\n");
        assertTrue(res.toString().contains(expected));
    }

}
