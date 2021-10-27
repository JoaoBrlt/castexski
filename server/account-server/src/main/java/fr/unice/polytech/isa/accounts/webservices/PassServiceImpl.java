package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerPassFinder;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/pass")
@Stateless(name = "PassWS")
public class PassServiceImpl implements PassService {

    @EJB protected CustomerPassFinder customerPassFinder;

    @Override
    public List<String> getNotLinkedPass(String email) throws CustomerNotFoundException {
        return customerPassFinder.findNotLinkedPass(email).stream().map(Pass::getId).collect(Collectors.toList());
    }

    @Override
    public List<String> getLinkedPass(String email) throws CustomerNotFoundException {
        return customerPassFinder.findLinkedPass(email).stream().map(Pass::getId).collect(Collectors.toList());
    }

    @Override
    public String getPassNameById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getType().getName();
    }

    @Override
    public double getPassPriceById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getType().getPrice();
    }

    @Override
    public String getPassDurationById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getType().getMaxDurationRaw();
    }

    @Override
    public Date getPassStartDateById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getStartDate();
    }

    @Override
    public Date getPassEndDateById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getEndDate();
    }

    @Override
    public boolean isPassLinkedById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return Objects.nonNull(customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getCard());
    }

    @Override
    public String getPassPhysicalCardLinkedById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getCard().getPhysicalId();
    }

    @Override
    public boolean isChildPassById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).getType().isChildPass();
    }

    @Override
    public boolean isActivatedPassById(String email, String id) throws CustomerNotFoundException, PassNotFoundException {
        return customerPassFinder.findPassById(email, id).orElseThrow(()-> new PassNotFoundException(email, id)).isActivated();
    }


}
