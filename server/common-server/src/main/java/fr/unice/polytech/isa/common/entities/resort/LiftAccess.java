package fr.unice.polytech.isa.common.entities.resort;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *      Association class between a pass and ski lifts that allows one to fo through them using said pass
 * </p>
 */
@Entity
public class LiftAccess {
    /**
     * The pass name used to identify the skiLifts that allow to go through them
     */
    @NotNull
    private String passName;

    /**
     * The ski lifts (represented by their ids) that allow one to go through them if said one has a pass which name is passname
     */
    @NotNull
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "allowedLiftsIds")
    private Set<String> allowedLiftsIds = new HashSet<>();

    /**
     * Default constructor
     */
    public LiftAccess() {
    }

    /**
     * Full constructor for this class
     * @param passName
     * @param allowedLiftsIds
     */
    public LiftAccess(String passName, Set<String> allowedLiftsIds) {
        this.passName = passName;
        this.allowedLiftsIds = allowedLiftsIds;
    }

    /**
     * Returns the pass name
     * @return the pass name
     */
    public String getPassName() {
        return passName;
    }

    /**
     * Sets the pass name
     * @param passName the pass name
     */
    public void setPassName(String passName) {
        this.passName = passName;
    }

    /**
     * Returns the set containing all the allowed SkiLiftsIds
     * @return the set containing all the allowed SkiLiftsIds
     */
    public Set<String> getAllowedLiftsIds() {
        return allowedLiftsIds;
    }

    /**
     * Sets the set containing all the allowed SkiLiftsIds
     * @param allowedLiftsIds the set containing all the allowed SkiLiftsIds
     */
    public void setAllowedLiftsIds(Set<String> allowedLiftsIds) {
        this.allowedLiftsIds = allowedLiftsIds;
    }

    public void addAllowedLiftId(String allowedLiftId) {
        allowedLiftsIds.add(allowedLiftId);
    }

    public void removeAllowedLiftId(String notAllowedAnymoreLiftName) {
        allowedLiftsIds.remove(notAllowedAnymoreLiftName);
    }
}
