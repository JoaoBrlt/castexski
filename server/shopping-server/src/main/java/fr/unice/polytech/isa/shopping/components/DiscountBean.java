package fr.unice.polytech.isa.shopping.components;


import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassCatalog;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.exceptions.NullQuantityException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.SuperCartexDiscount;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Stateless
public class DiscountBean implements SuperCartexDiscount {

    @EJB
    protected CatalogExplorer catalogExplorer;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Item findSuperCartexPass(SuperCartex superCartex) throws NullQuantityException, UnknownCatalogEntryException {
        superCartex = entityManager.merge(superCartex);
        //is first swipe of the day?
        if (superCartex.getLastSwipe() == null || superCartex.getLastSwipe().isBefore(LocalDateTime.now().with(LocalTime.MIN))){
            return catalogExplorer.pickPass("SUPER_FREE_HOUR", Duration.ofHours(1), false, 1);
        }
        //is it the eighth consecutive day ?
        if (superCartex.getFirstSwipe().with(LocalTime.MIN).isEqual(LocalDateTime.now().minusDays(7).with(LocalTime.MIN))){
            return catalogExplorer.pickPass("SUPER_FREE_EIGHTH", Duration.ofDays(1), false, 1);
        }

        Item pass = catalogExplorer.pickPass("SUPER_ORIGINAL", Duration.ofDays(1), false, 1);

        //check if there is a day discount
        Optional<PassCatalog> dayDiscount = catalogExplorer.findPass("SUPER_DISCOUNT_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), Duration.ofDays(1));
        if (dayDiscount.isPresent()){
            PassCatalog p = dayDiscount.get();
            if (p.getPrice() < pass.getTotalPrice()) {
                return catalogExplorer.pickPass(p.getName(), p.getMaxDuration(), false, 1);
            }
        }

        //check if there is a month discount
        Optional<PassCatalog> monthDiscount = catalogExplorer.findPass("SUPER_DISCOUNT_" + LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy")), Duration.ofDays(1));
        if (monthDiscount.isPresent()){
            PassCatalog p = monthDiscount.get();
            if (p.getPrice() < pass.getTotalPrice()) {
                return catalogExplorer.pickPass(p.getName(), p.getMaxDuration(), false, 1);
            }
        }
        return pass;
    }
}
