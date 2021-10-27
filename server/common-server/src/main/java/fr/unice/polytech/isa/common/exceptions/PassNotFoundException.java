package fr.unice.polytech.isa.common.exceptions;


public class PassNotFoundException extends Exception {

    public PassNotFoundException(String passId) {
        super("PassNotFoundException: "+ System.lineSeparator() + "pass: " + passId);
    }

    public PassNotFoundException(String email, String passId) {
        super("PassNotFoundException: \nemail: " + email + "\npass: " + passId);
    }
}
