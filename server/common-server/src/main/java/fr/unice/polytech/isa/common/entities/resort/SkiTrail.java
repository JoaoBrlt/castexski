package fr.unice.polytech.isa.common.entities.resort;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class SkiTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @NotNull
    private String name;

    private boolean isOpen;

    //TODO private Location location;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<DisplayPanel> displayPanels = new HashSet<>();

    public SkiTrail() {
    }

    public SkiTrail(String name, boolean isOpen) {
        this.name = name;
        this.isOpen = isOpen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Set<DisplayPanel> getDisplayPanels() {
        return displayPanels;
    }

    public void setDisplayPanels(Set<DisplayPanel> displayPanels) {
        this.displayPanels = displayPanels;
    }

    public void addDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanels.add(displayPanel);
    }

    public void removeDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanels.remove(displayPanel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SkiTrail skiTrail = (SkiTrail) o;
        return isOpen == skiTrail.isOpen &&
                Objects.equals(name, skiTrail.name) &&
                Objects.equals(displayPanels, skiTrail.displayPanels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOpen, name, displayPanels);
    }
}
