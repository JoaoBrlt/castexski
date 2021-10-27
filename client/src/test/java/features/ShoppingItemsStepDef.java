package features;

import cli.framework.Shell;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import static org.junit.Assert.*;

import java.io.IOException;

public class ShoppingItemsStepDef extends AbstractClientTest {

    public ShoppingItemsStepDef() throws IOException {
        init();
    }

    @And("(.*) logs in")
    public void login(String mail) {
        shell.internShell = new Shell<>();
        shell.internShell.system = shell.system;
        shell.internShell.userEmail = mail;
    }

    @And("he exits")
    public void exit() {
        shell.internShell = null;
    }

    @And("he removes (.*) (.*) for which super card is (.*) from his cart")
    public void removeCardFromCart(String quantity, String name, String superCard) throws Exception {
        loggedCmd(removeCardFromCart, name, superCard, quantity);
    }

    @And("he removes (.*) (.*) which lasts (.*) hours and with children at (.*) from his cart")
    public void removePassFromCart(String quantity, String name, String duration, String isChildren) throws Exception {
        loggedCmd(removePassFromCart, name, duration, isChildren, quantity);
    }

    @And("he adds (.*) (.*) for which super card is (.*) to his cart")
    public void addCardToCart(String quantity, String name, String superCart) throws Exception {
        loggedCmd(addCardToCart, name, superCart, quantity);
    }

    @And("he adds (.*) (.*) which lasts (.*) hours and with children at (.*)")
    public void addPassToCart(String quantity, String name, String duration, String isChildren) throws Exception {
        loggedCmd(addPassToCart, name, duration, isChildren, quantity);
    }

    @And("he adds the credit card (.*) of number (.*), security code (.*), expire month (.*) and expire year (.*) to his account")
    public void addCreditCard(String name, String number, String security, String expireMonth, String expireYear) throws Exception {
        loggedCmd(addACreditCard, name, number, security, expireMonth, expireYear, "true");
    }

    @And("he pays for his cart")
    public void validateAndPay() throws Exception {
        loggedCmd(validateCart);
    }

    @And("for (.*) card, his account displays a (.*)")
    public void checkAccountCard(String lines, String name) throws Exception {
        loggedCmd(displayCards);
        checkAccountPassOrCard(Integer.parseInt(lines), ("] "+name+" | card: ").toLowerCase());
    }

    @And("for (.*) pass, his account displays a (.*)")
    public void checkAccountPass(String lines, String name) throws Exception {
        loggedCmd(displayPasses);
        checkAccountPassOrCard(Integer.parseInt(lines), (name+": ").toLowerCase());
    }

    public static void checkAccountPassOrCard(int linesNb, String expected) {
        StringBuilder res = new StringBuilder(scanner.nextLine().toLowerCase())
            .append(scanner.nextLine().toLowerCase());
        for (int i = 0; i < linesNb; i++) res.append(scanner.nextLine().toLowerCase());
        assertTrue(res.toString().contains(expected));
    }

    @Then("(.*) exists and has the credit card (.*)")
    public void checkAccountCreditCard(String mail, String name) throws Exception {
        cmd(findCustomerByMail, mail);
        String res = scanner.nextLine()+scanner.nextLine()+scanner.nextLine()+scanner.nextLine()+scanner.nextLine()+scanner.nextLine();
        assertTrue(res.contains(mail) && res.contains(name));
    }

    @Then("for (.*) item, his cart displays (.*) (.*) for price (.*) as a (.*)")
    public void checkDisplay(String lines, String quantity, String name, String price, String type) throws Exception {
        loggedCmd(displayCart);
        int linesNb = Integer.parseInt(lines);
        String expected = (type+" -> "+name+"("+price+".0€) x "+quantity+" = ").toLowerCase();
        StringBuilder res = new StringBuilder(scanner.nextLine().toLowerCase());
        for (int i = 0; i <= linesNb; i++) res.append(scanner.nextLine().toLowerCase());
        assertTrue(res.toString().contains(expected));
    }

    @Then("his cart is empty")
    public void checkEmpty() throws Exception {
        loggedCmd(displayCart);
        String res = scanner.nextLine()+"\n"+scanner.nextLine();
        assertTrue(res.contains("Empty cart!"));
    }
}
