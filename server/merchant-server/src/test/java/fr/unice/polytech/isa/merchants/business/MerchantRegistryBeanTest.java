package fr.unice.polytech.isa.merchants.business;

import arquillian.AbstractMerchantTest;
import fr.unice.polytech.isa.accounts.exceptions.UnavailableEmailException;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.merchants.exceptions.MerchantNotFoundException;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.merchants.interfaces.MerchantSubscription;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class MerchantRegistryBeanTest extends AbstractMerchantTest {
    @EJB
    private MerchantSubscription merchantRegistry;

    @EJB
    private MerchantFinder merchantFinder;

    @Rule
    public final ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void registerMerchant() throws UnavailableEmailException {
        // Create the merchant.
        Merchant merchant = new Merchant(
            "John",
            "Doe",
            "john.doe@example.org",
            "John Fisheries"
        );

        // Register the merchant.
        merchantRegistry.registerMerchant(merchant);

        // The merchant has been registered.
        Optional<Merchant> maybeMerchant = merchantFinder.findMerchant(merchant.getEmail());
        assertTrue(maybeMerchant.isPresent());

        // The merchant is the same.
        Merchant newMerchant = maybeMerchant.get();
        assertEquals(merchant, newMerchant);
    }

    @Test
    public void removeMerchant() throws UnavailableEmailException, MerchantNotFoundException {
        // Create the merchant.
        Merchant merchant = new Merchant(
            "Jane",
            "Smith",
            "jane.smith@example.org",
            "Smith Ltd."
        );

        // Register the merchant.
        merchantRegistry.registerMerchant(merchant);

        // Remove the merchant.
        merchantRegistry.removeMerchant(merchant.getEmail());

        // The merchant has been removed.
        Optional<Merchant> maybeMerchant = merchantFinder.findMerchant(merchant.getEmail());
        assertFalse(maybeMerchant.isPresent());
    }
}
