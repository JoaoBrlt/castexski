package features;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

import java.io.IOException;

public class CheckLiftsStepDef extends AbstractClientTest {

    public CheckLiftsStepDef() throws IOException {
        init();
    }

    @And("an (.*) access for the pass (.*)")
    public void linkAccess(String lift, String pass) throws Exception {
        cmd(addAccess, resortName, pass, lift);
    }

    @When("he check its physical card (.*) to the ski lift (.*)")
    public void checkCard(String physical, String lift) throws Exception {
        cmd(checkCard, resortName, lift, physical);
    }

    @Then("the access terminal says (.*)")
    public void checkAccess(String msg) {
        assertTrue(scanner.nextLine().contains(msg));
    }

}
