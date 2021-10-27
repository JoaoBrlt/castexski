package fr.unice.polytech.isa.resort.exceptions;

public class UnavailableNameException extends Exception {
    private String conflictingName;

    public UnavailableNameException(String name) {
        super(name);
        conflictingName = name;
    }
}
