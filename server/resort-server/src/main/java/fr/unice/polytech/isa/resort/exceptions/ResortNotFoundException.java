package fr.unice.polytech.isa.resort.exceptions;

public class ResortNotFoundException extends Exception {
    private String conflictingName;

    public ResortNotFoundException(String name) {
        super(name);
        conflictingName = name;
    }
}
