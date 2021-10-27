package features;

import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

import static org.junit.Assert.*;

public class InitiateResortStepDef extends AbstractClientTest {

    public InitiateResortStepDef() throws IOException {
        init();
    }

    @Given("a registered resort named (.*) with mail (.*) in city (.*)")
    public void createResort(String name, String mail, String city) throws Exception {
        init();
        resortName = name;
        cmd(addResort, name, mail, "true", city);
    }

    @When("a new ski lift named (.*) with open at (.*) is created")
    public void createSkiLift(String name, String open) throws Exception {
        cmd(addSkiLift, resortName, name, open);
    }

    @Then("the ski lift (.*) exists")
    public void skiLiftExists(String name) throws Exception {
        cmd(findSkiLiftByName, resortName, name);
        String res = scanner.nextLine();
        assertTrue(res.contains(name));
    }

    @And("the ski lift (.*) does not exist")
    public void notExistingSkiLift(String name) throws Exception {
        boolean hadException = false;
        try {
            cmd(findSkiLiftByName, resortName, name);
        } catch (ServerSOAPFaultException e) {
            hadException = true;
        }
        assertTrue(hadException);
    }

    @And("the ski lift named (.*) is removed")
    public void removeSkiLift(String name) throws Exception {
        cmd(deleteSkiLift, resortName, name);
    }

    @And("the resort (.*) is removed")
    public void removeResort(String name) throws Exception {
        cmd(deleteResort, name);
    }
}
