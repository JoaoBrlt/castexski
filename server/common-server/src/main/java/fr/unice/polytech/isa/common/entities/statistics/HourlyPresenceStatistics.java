package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name="HOURLY_PRESENCE_STATISTICS")
@DiscriminatorValue(value="P") // P For Presence
@PrimaryKeyJoinColumn(name="HOURLY_ID")
public class HourlyPresenceStatistics extends HourlyStatistics {
    /**
     * The cards beeped and the number of times they were beeped
     */
    private HashMap<String,Integer> beepedCards;

    /**
     * The number of cards beeped, not persisted because it is redundant information
     * This represent the number of persons that are present
     * Created for comfort/easiness  to access the information
     */
    @Transient
    private int numberOfCardsBeeped;

    /**
     * The number of passage, count the total times a ski lift was used (a same person
     * can use a ski lift multiple times, but it still counts as one person)
     * It is not persisted because it is redundant information
     * Created for comfort/easiness to access the information
     */
    @Transient
    private int numberOfPassage;

    /**
     * Default constructor.
     */
    public HourlyPresenceStatistics() {}

    public HourlyPresenceStatistics(int startingHour){
        super(startingHour);
        this.beepedCards = new HashMap<>();
        this.numberOfPassage = 0;
        this.numberOfCardsBeeped = 0;
    }

    @PostLoad
    public void init() {
        if(this.beepedCards.equals(null)){
            this.numberOfPassage = 0;
            this.numberOfCardsBeeped = 0;
        }
        else {
            this.numberOfCardsBeeped = beepedCards.size();
            this.numberOfPassage = getPassage();
        }
    }

    /**
     * Returns the hourly statistics' beepedCards.
     * @return The hourly statistics' beepedCards.
     */
    public HashMap<String, Integer> getBeepedCards() {
        return beepedCards;
    }

    /**
     * Sets the beepedCards, with their physical id
     * @param beepedCards The beepedCards.
     */
    public void setBeepedCards(HashMap<String, Integer> beepedCards) {
        this.beepedCards = beepedCards;
    }

    /**
     * Sets the total number of cards beeped
     * @param numberOfCardsBeeped the number of beeped cards
     */
    public void setNumberOfCardsBeeped(int numberOfCardsBeeped) {
        this.numberOfCardsBeeped = numberOfCardsBeeped;
    }

    /**
     * @return the number of cards beeped, aka the number of people
     */
    public int getNumberOfCardsBeeped() {
        return numberOfCardsBeeped;
    }

    /**
     * @return the frequency of passage
     */
    public int getNumberOfPassage() {
        return numberOfPassage;
    }

    /**
     * Sets the frequency of people passing
     * @param numberOfPassage the frequency
     */
    public void setNumberOfPassage(int numberOfPassage) {
        this.numberOfPassage = numberOfPassage;
    }

    /**
     * Adds a card to the beepedCards or increments the associated
     * integer if it is already present
     * @param cardPhysicalId the card to add or increment
     */
    public void addCardToBeepedCards(String cardPhysicalId){
        if (beepedCards.containsKey(cardPhysicalId)) {
            int beeped = beepedCards.get(cardPhysicalId);
            this.beepedCards.replace(cardPhysicalId,beeped+1);
        }
        else {
            this.beepedCards.put(cardPhysicalId,1);
            this.numberOfCardsBeeped++;
        }
        this.numberOfPassage++;
    }

    /**
     * Removes a card from the beepedCards
     * @param cardPhysicalId the card to remove
     */
    public void removeCardFromBeepedCards(String cardPhysicalId){
        if(this.beepedCards.containsKey(cardPhysicalId)){
            this.numberOfPassage = this.numberOfPassage - beepedCards.get(cardPhysicalId);
            beepedCards.remove(cardPhysicalId);
            this.numberOfCardsBeeped--;
        }
    }

    /**
     * @return the total number of times that all cards were scanned
     */
    private int getPassage(){
        int totalPassage = 0;
        for(Integer i : beepedCards.values()){
            totalPassage = totalPassage+i;
        }
        return totalPassage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourlyPresenceStatistics that = (HourlyPresenceStatistics) o;
        return numberOfCardsBeeped == that.numberOfCardsBeeped && numberOfPassage == that.numberOfPassage && Objects.equals(beepedCards, that.beepedCards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beepedCards, numberOfCardsBeeped, numberOfPassage);
    }

    @Override
    public String toString() {
        return "HourlyPresenceStatistics{" +
                "beepedCards=" + beepedCards +
                ", numberOfCardsBeeped=" + numberOfCardsBeeped +
                ", numberOfPassage=" + numberOfPassage +
                '}';
    }
}
