package fr.unice.polytech.isa.resort.exceptions;

public class DisplayPanelNotFoundException extends Exception {
    private String conflictingName;

    public DisplayPanelNotFoundException(String name) {
        super(name);
        conflictingName = name;
    }
}
