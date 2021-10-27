package fr.unice.polytech.isa.resort.exceptions;

public class SkiTrailNotFoundException extends Exception {
    private String conflictingName;

    public SkiTrailNotFoundException(String name) {
        super(name);
        conflictingName = name;
    }
}
