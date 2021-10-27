package fr.unice.polytech.isa.accounts.exceptions;

public class CardNotFoundException extends Exception {

    private String conflictingId;

    public CardNotFoundException(String id) {
        super(id);
        this.conflictingId = id;
    }
}
