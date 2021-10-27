package fr.unice.polytech.isa.resort.exceptions;

public class SkiLiftNotFoundException extends Exception {
    private String conflictingName;

    public SkiLiftNotFoundException(String name) {
        super(name);
        conflictingName = name;
    }
}
