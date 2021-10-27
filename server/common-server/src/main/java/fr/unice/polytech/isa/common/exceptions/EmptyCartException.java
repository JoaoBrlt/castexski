package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class EmptyCartException extends Exception implements Serializable {
    private String email;

    public EmptyCartException() {}

    public EmptyCartException(String customerEmail) {
        super(customerEmail + ": EmptyCartException");
        this.email = customerEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
