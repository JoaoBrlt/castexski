package fr.unice.polytech.isa.shopping.interfaces;

import fr.unice.polytech.isa.common.entities.shopping.catalog.CardCatalog;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassCatalog;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;

import javax.ejb.Local;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Local
public interface CatalogExplorer {
    Optional<CardCatalog> findCard(String name, boolean type);
    Optional<PassCatalog> findPass(String name, Duration duration);
    Optional<ItemCatalog> findCatalogEntryById(int id);
    Item pickCard(String name, boolean type, int quantity) throws  UnknownCatalogEntryException, NullQuantityException;
    Item pickPass(String name, Duration duration, boolean isChildren, int quantity) throws UnknownCatalogEntryException, NullQuantityException;
    List<ItemCatalog> displayCatalog();
    List<PassCatalog> displayPassCatalog();
    List<ItemCatalog> displayPrivateCatalog();
    }
