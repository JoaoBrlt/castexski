package fr.unice.polytech.isa.accounts.components;


import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.*;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class PassRegistryBean implements PassRegistration, CustomerPassFinder {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB
    private CustomerFinder customerFinder;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public String registerPass(String email, ItemType item) throws CustomerNotFoundException {
        Customer c = manager.merge(customerFinder.findByMail(email));
        Pass p = new Pass((PassType) item);
        manager.persist(p);
        c.addPass(p);
        p = manager.merge(p);
        return p.getId();
    }

    @Override
    public void activatePass(Pass pass) {
        pass = manager.merge(pass);
        pass.setActivated(true);
        pass.setStartDate(new Date());
        pass.setEndDate(new Date(pass.getStartDate().getTime() + pass.getType().getMaxDuration().getSeconds() * 1000));
    }


    @Override
    public Optional<Pass> findPassById(String email, String id) throws CustomerNotFoundException {
        return readCustomer(email).getPasses().stream().filter(p->id.equals(p.getId())).findFirst();
    }

    @Override
    public List<Pass> findCustomerPasses(String email) throws CustomerNotFoundException {
        return readCustomer(email).getPasses();
    }

    @Override
    public List<Pass> findNotLinkedPass(String email) throws CustomerNotFoundException {
        return findCustomerPasses(email).stream().filter(p-> Objects.isNull(p.getCard())).collect(Collectors.toList());
    }

    @Override
    public List<Pass> findLinkedPass(String email) throws CustomerNotFoundException {
        return findCustomerPasses(email).stream().filter(p-> Objects.nonNull(p.getCard())).collect(Collectors.toList());
    }

    Customer readCustomer(String email) throws CustomerNotFoundException {
        return manager.merge(customerFinder.findByMail(email));
    }
}
