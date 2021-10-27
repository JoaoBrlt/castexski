package features;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;

import static org.junit.Assert.*;

public class InitiateUsersStepDef extends AbstractClientTest {

    public InitiateUsersStepDef() throws IOException {
        init();
    }

    @When("a user named (.*) (.*) with email (.*) is created")
    public void customerRegister(String firstName, String lastName, String mail) throws Exception {
        cmd(customerRegister, firstName, lastName, mail);
    }

    @And("the user for mail (.*) is removed")
    public void deleteCustomer(String mail) throws Exception {
        cmd(deleteCustomer, mail);
    }

    @Then("the user for mail (.*) exists")
    public void userExists(String mail) throws Exception {
        cmd(findCustomerByMail, mail);
        String res = scanner.nextLine()+"\n"+scanner.nextLine()+"\n"+scanner.nextLine();
        assertTrue(res.toLowerCase().contains(("- mail : " + mail).toLowerCase()));
    }

    @And("the user for mail (.*) does not exist")
    public void userDontExist(String mail) throws Exception {
        cmd(findCustomerByMail, mail);
        String res = scanner.nextLine();
        assertTrue(res.toLowerCase().contains(("Unknown user: " + mail).toLowerCase()));
    }

}
