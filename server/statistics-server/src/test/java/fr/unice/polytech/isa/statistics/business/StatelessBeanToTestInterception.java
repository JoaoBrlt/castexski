package fr.unice.polytech.isa.statistics.business;

import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.interceptors.CardCounter;
import fr.unice.polytech.isa.statistics.interceptors.PurchaseCounter;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * This class is here to test the interceptors
 */
@Stateless
public class StatelessBeanToTestInterception {

    @Interceptors({CardCounter.class})
    public boolean methodToInterceptTrue(SkiLift skiLift, Card card){
        return true;
    }

    @Interceptors({CardCounter.class})
    public boolean methodToInterceptFalse(SkiLift skiLift, Card card){
        return false;
    }

    @Interceptors({PurchaseCounter.class})
    public void methodToInterceptCustomerCart(Customer customer){}
}
