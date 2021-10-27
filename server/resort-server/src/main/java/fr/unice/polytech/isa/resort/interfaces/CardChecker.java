package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;

import javax.ejb.Local;

@Local
public interface CardChecker {
    boolean checkCard(SkiLift lift, Card card);
}
