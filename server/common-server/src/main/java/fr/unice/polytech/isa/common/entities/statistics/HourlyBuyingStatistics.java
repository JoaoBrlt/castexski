package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name="HOURLY_BUYING_STATISTICS")
@DiscriminatorValue(value="B") // B For Buying
@PrimaryKeyJoinColumn(name="HOURLY_ID")
public class HourlyBuyingStatistics extends HourlyStatistics {
    /**
     * The number of cards bought in this hour
     */
    private int nbOfCards;

    /**
     * The number of Cartex bought in this hour
     */
    private int nbOfCartex;

    /**
     * The number of child passes bought
     */
    private int nbOfChildPasses;

    /**
     * The different types of passes bought and their quantities
     */
    private HashMap<String,Integer> nbOfPasses;

    /**
     * Default constructor.
     */
    public HourlyBuyingStatistics(){}

    /**
     * The HourlyBuyingStatistic constructor with a starting hour of the day
     * @param startingHour the hour the statistics starts
     */
    public HourlyBuyingStatistics(int startingHour){
        super(startingHour);
        this.nbOfPasses = new HashMap<>();
        this.nbOfCards = 0;
        this.nbOfCartex = 0;
        this.nbOfChildPasses = 0;
    }

    /**
     * @return the number of cards bought
     */
    public int getNbOfCards() {
        return nbOfCards;
    }

    /**
     * Sets the number of cards bought
     * @param nbOfCards the number of cards bought
     */
    public void setNbOfCards(int nbOfCards) {
        this.nbOfCards = nbOfCards;
    }

    /**
     * Adds cards to the number of cards bought
     * @param nbOfCards the number of cards to add
     */
    public void addNbOfCards(int nbOfCards){this.nbOfCards+=nbOfCards;}

    /**
     * Removes cards from the number of cards bought
     * @param nbOfCards the number of cards to remove
     */
    public void removeCards(int nbOfCards){
        if(nbOfCards>=this.nbOfCards){
            this.nbOfCards = 0;
        }
        else {
            this.nbOfCards-=nbOfCards;
        }
    }

    /**
     * @return the number of Cartex bought
     */
    public int getNbOfCartex() {
        return nbOfCartex;
    }

    /**
     * Sets the number of Cartex cards bought
     * @param nbOfCartex the number of Cartex cards bought
     */
    public void setNbOfCartex(int nbOfCartex) {
        this.nbOfCartex = nbOfCartex;
    }

    /**
     * Adds cartex to the number of cartex bought
     * @param nbOfCartex the number of cartex to add
     */
    public void addNbOfCartex(int nbOfCartex) {this.nbOfCartex+=nbOfCartex;}

    /**
     * Removes cartex from the number of cartex bought
     * @param nbOfCartex the number of cartex to remove
     */
    public void removeNbOfCartex(int nbOfCartex){
        if(nbOfCartex>=this.nbOfCartex){
            this.nbOfCartex=0;
        }
        else {
            this.nbOfCartex-=nbOfCartex;
        }
    }

    /**
     * To get the different names of the passes bought and their corresponding quantities
     * @return the hashmap of passes
     */
    public HashMap<String, Integer> getNbOfPasses() {
        return nbOfPasses;
    }

    /**
     * Sets the different passes bought with their name and their quantities
     * @param nbOfPasses the number of passes bought
     */
    public void setNbOfPasses(HashMap<String, Integer> nbOfPasses) {
        this.nbOfPasses = nbOfPasses;
    }

    /**
     * Add pass if not contained, or updates the current pass
     * @param passName the pass to add or update
     * @param quantity the quantity to add
     */
    public void addPass(String passName, int quantity){
        if(this.nbOfPasses.containsKey(passName)) {
            int oldValue = this.nbOfPasses.get(passName);
            this.nbOfPasses.replace(passName,oldValue+quantity);
        }
        else {
            this.nbOfPasses.put(passName,quantity);
        }
    }

    /**
     * Removes a pass if contained
     * @param passName the pass to remove
     */
    public void removePass(String passName){
        this.nbOfPasses.remove(passName);
    }

    /**
     * @return the number of child passes bought
     */
    public int getNbOfChildPasses() {
        return nbOfChildPasses;
    }

    /**
     * Sets the number of child passes bought
     * @param nbOfChildPasses the number of child passes
     */
    public void setNbOfChildPasses(int nbOfChildPasses) {
        this.nbOfChildPasses = nbOfChildPasses;
    }

    /**
     * Adds child passes to the number of child passes bought
     * @param nbOfChildPasses the number of child passes to add
     */
    public void addNbOfChildPasses(int nbOfChildPasses){this.nbOfChildPasses+= nbOfChildPasses;}

    public void removeNbOfChildPasses(int nbOfChildPasses){
        if(nbOfChildPasses>=this.nbOfChildPasses){
            this.nbOfChildPasses=0;
        }
        else {
            this.nbOfChildPasses-=nbOfChildPasses;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HourlyBuyingStatistics that = (HourlyBuyingStatistics) o;
        return nbOfCards == that.nbOfCards && nbOfCartex == that.nbOfCartex && nbOfChildPasses == that.nbOfChildPasses && Objects.equals(nbOfPasses, that.nbOfPasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbOfCards, nbOfCartex, nbOfChildPasses, nbOfPasses);
    }

    @Override
    public String toString() {
        return "HourlyBuyingStatistics{" +
                "nbOfCards=" + nbOfCards +
                ", nbOfCartex=" + nbOfCartex +
                ", nbOfChildPasses=" + nbOfChildPasses +
                ", nbOfPasses=" + nbOfPasses +
                '}';
    }
}
