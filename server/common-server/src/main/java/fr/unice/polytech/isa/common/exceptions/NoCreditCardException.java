package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class NoCreditCardException extends Exception implements Serializable {
    private String email;

    public NoCreditCardException() {}

    public NoCreditCardException(String customerEmail) {
        super(customerEmail + ": NoCreditCardException");
        this.email = customerEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
