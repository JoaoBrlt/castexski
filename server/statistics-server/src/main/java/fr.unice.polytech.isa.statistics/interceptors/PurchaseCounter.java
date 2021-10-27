package fr.unice.polytech.isa.statistics.interceptors;

import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsUpdater;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Set;

public class PurchaseCounter implements Serializable {
    @EJB
    private PurchaseStatisticsUpdater updater;

    @PersistenceContext
    private EntityManager entityManager;

    @AroundInvoke
    public Object interceptPurchase(InvocationContext ctx) throws Exception {
        //On récupère les paramètres nécessaires aux statistiques d'achat
        Customer customer = (Customer) ctx.getParameters()[0];
        customer = entityManager.merge(customer);
        Cart cart = customer.getCart();
        cart = entityManager.merge(cart);
        Set<Item> itemSet = cart.getItems();

        //On exécute la méthode interceptée
        Object transaction = ctx.proceed();

        //Si la méthode se déroule sans pb, on peut update les statistiques
        for(Item item : itemSet){
            int quantity = item.getQuantity();
            switch (item.getType().getType()) {
                case CARD:
                    updater.addCard(quantity);
                    break;
                case PASS:
                    PassType passType = (PassType) item.getType();
                    boolean isChildPass = passType.isChildPass();
                    updater.addPass(item.getType().getName(), quantity, isChildPass);
                    break;
                case SUPERCARTEX:
                    updater.addSuperCartex(quantity);
                    break;
                default:
                    break;
            }
        }

        return transaction;
    }
}
