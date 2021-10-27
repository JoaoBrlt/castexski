package fr.unice.polytech.isa.shopping.interfaces;

import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;

import javax.ejb.Local;
import java.time.Duration;

@Local
public interface CatalogModifier {
    void addPass(String name, double regularPrice, double childrenPrice, Duration duration, boolean isPrivateItem) throws ItemAlreadyExistException;
    void addCard(String name, boolean specialCard, double price, boolean isPrivateItem) throws ItemAlreadyExistException;
    void deletePass(String name, Duration duration) throws UnknownCatalogEntryException;
    void deleteCard(String name, boolean type) throws UnknownCatalogEntryException;
}
