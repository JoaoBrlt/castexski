package fr.unice.polytech.isa.common.entities.resort;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Resort
 * <p>
 *     Represents a resort, containing all the information about its ski lifts, trails, lift accesses and display signs
 * </p>
 */
@Entity
public class Resort implements Serializable {

    /**
     * The resort identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The resort's name
     */
    @NotNull
    private String name;

    /**
     * Whether the resort is opened or not
     */
    private boolean isOpen;

    /**
     * The email address of the resort where alerts will be sent.
     */
    private String resortEmail;

    /**
     * The city in which the resort is setup
     */
    private String cityName;

    //TODO private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<LiftAccess> accesses = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resort")
    private Set<SkiLift> lifts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<SkiTrail> trails = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<DisplayPanel> displayPanels = new HashSet<>();

    /**
     * Default constructor
     */
    public Resort() {
    }

    /**
     * Constructs a resort
     * @param name the resort's name
     * @param isOpen whether the resort is opened or not
     * @param resortEmail the email of the resort where alerts will be sent.
     */
    public Resort(String name, boolean isOpen, String resortEmail, String cityName) {
        this.name = name;
        this.isOpen = isOpen;
        this.resortEmail = resortEmail;
        this.cityName = cityName;
    }

    /**
     * Returns the resort's id
     * @return the resort's id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the resort's id
     * @param id the resort's id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the resort's name
     * @return the resort's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the resort's name
     * @param name the resort's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns whether the resort is opened or not
     * @return whether the resort is opened or not
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Sets the resorts openness
     * @param open whether the resort is going to be opened or not
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * Returns the accesses allowed in this resort
     * @return the accesses allowed in this resort
     */
    public Set<LiftAccess> getAccesses() {
        return accesses;
    }

    /**
     * Sets the accesses allowed in this resort
     * @param accesses the accesses allowed in this resort
     */
    public void setAccesses(Set<LiftAccess> accesses) {
        this.accesses = accesses;
    }

    /**
     * Add a LiftAccess to the list
     * @param access a LiftAccess to be added to the list
     */
    public void addAccess(LiftAccess access) {
        this.accesses.add(access);
    }


    /**
     * Removes an LiftAccess from the list
     * @param access a LiftAccess to be removed from the list
     */
    public void removeAccess(LiftAccess access) {
        this.accesses.remove(access);
    }

    /**
     * Returns the skiLifts in this resort
     * @return the skiLifts in this resort
     */
    public Set<SkiLift> getLifts() {
        return lifts;
    }

    /**
     * Sets the skiLifts in this resort
     * @param lifts the skiLifts in this resort
     */
    public void setLifts(Set<SkiLift> lifts) {
        this.lifts = lifts;
    }

    /**
     * Add an SkiLift to the list
     * @param skiLift a SkiLift to be added to the list
     */
    public void addLift(SkiLift skiLift) {
        this.lifts.add(skiLift);
    }

    /**
     * Removes an SkiLift from the list
     * @param skiLift a SkiLift to be removed from the list
     */
    public void removeLift(SkiLift skiLift) {
        this.lifts.remove(skiLift);
    }

    /**
     * Returns the skiTrails in this resort
     * @return the skiTrails in this resort
     */
    public Set<SkiTrail> getTrails() {
        return trails;
    }

    /**
     * Sets new skiTrails in this resort
     * @param trails the new skiTrails in this resort
     */
    public void setTrails(Set<SkiTrail> trails) {
        this.trails = trails;
    }

    /**
     * Adds a skiTrail to the set of trails
     * @param skiTrail the skiTrail to be added
     */
    public void addTrail(SkiTrail skiTrail) {
        this.trails.add(skiTrail);
    }

    /**
     * Removes a skiTrail from the set of trails
     * @param skiTrail the skiTrail to be removed
     */
    public void removeTrail(SkiTrail skiTrail) {
        this.trails.remove(skiTrail);
    }

    /**
     * Returns the set of display panels in this resort
     * @return the set of display panels in this resort
     */
    public Set<DisplayPanel> getDisplayPanels() {
        return displayPanels;
    }

    /**
     * Sets the new set of display panels in this resort
     * @param displayPanels the new set of display panels in this resort
     */
    public void setDisplayPanels(Set<DisplayPanel> displayPanels) {
        this.displayPanels = displayPanels;
    }

    /**
     * Adds a displayPanel to the set of display panels
     * @param displayPanel the skiTrail to be added
     */
    public void addDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanels.add(displayPanel);
    }

    /**
     * Removes a displayPanel from the set of display panels
     * @param displayPanel the displayPanel to be removed
     */
    public void removeDisplayPanel(DisplayPanel displayPanel) {
        this.displayPanels.remove(displayPanel);
    }

    /**
     * Returns the email of the resort
     * @return the email of the resort
     */
    public String getResortEmail() {
        return resortEmail;
    }

    /**
     * Sets the new email of the resort
     * @param resortEmail the new email of the resort
     */
    public void setResortEmail(String resortEmail) {
        this.resortEmail = resortEmail;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resort resort = (Resort) o;
        return isOpen == resort.isOpen
                && Objects.equals(name, resort.name)
                && Objects.equals(accesses, resort.accesses)
                && Objects.equals(lifts, resort.lifts)
                && Objects.equals(trails, resort.trails)
                && Objects.equals(displayPanels, resort.displayPanels)
                && Objects.equals(getResortEmail(), resort.getResortEmail())
                && Objects.equals(getCityName(), resort.getCityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isOpen, accesses, lifts, trails, displayPanels, resortEmail, cityName);
    }
}
