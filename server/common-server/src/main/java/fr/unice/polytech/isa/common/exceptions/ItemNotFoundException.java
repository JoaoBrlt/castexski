package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class ItemNotFoundException extends Exception implements Serializable {
    private String email;

    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String email) {
        super(email + ": ItemNotFoundException");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
