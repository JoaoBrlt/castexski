package fr.unice.polytech.isa.shopping.webservices;


import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemCatalog;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassCatalog;
import fr.unice.polytech.isa.common.exceptions.ItemAlreadyExistException;
import fr.unice.polytech.isa.common.exceptions.UnknownCatalogEntryException;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CatalogModifier;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/catalog")
@Stateless(name = "CatalogWS")
public class CatalogWebServiceImpl implements CatalogWebService {
    @EJB
    private CatalogExplorer explorer;

    @EJB
    private CatalogModifier modifier;

    @Override
    public List<Integer> displayCatalog() {
        return explorer.displayCatalog().stream().map(ItemCatalog::getId).collect(Collectors.toList());
    }


    @Override
    public void addCard(String name, boolean isSuperCartex, double price, boolean isPrivateItem) throws ItemAlreadyExistException {
        modifier.addCard(name, isSuperCartex, price, isPrivateItem);
    }

    @Override
    public void addPass(String name, double regularPrice, double childrenPrice, String duration, boolean isPrivateItem) throws ItemAlreadyExistException {
        modifier.addPass(name, regularPrice, childrenPrice,Duration.parse(duration), isPrivateItem);
    }

    @Override
    public void deletePass(String name, String duration) throws UnknownCatalogEntryException {
        modifier.deletePass(name, Duration.parse(duration));
    }

    @Override
    public void deleteCard(String name, boolean isSuperCartex) throws UnknownCatalogEntryException {
        modifier.deleteCard(name, isSuperCartex);
    }

    @Override
    public String getEntryNameById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getName();
    }

    @Override
    public double getEntryRegularPriceById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getPrice();
    }

    @Override
    public double getEntryChildrenPriceById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getChildrenPrice();
    }

    @Override
    public String getEntryTypeById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getItemTypeName().name();
    }

    @Override
    public String getPassDurationById(int id) throws UnknownCatalogEntryException {
        ItemCatalog itemCatalog=  readCatalogEntry(id);
        if (itemCatalog.getItemTypeName().equals(ItemTypeName.PASS)){
            return ((PassCatalog) itemCatalog).getMaxDurationRaw();
        }
        throw new UnknownCatalogEntryException(id);
    }

    @Override
    public boolean isSuperCartexById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getItemTypeName().equals(ItemTypeName.SUPERCARTEX);
    }

    @Override
    public boolean isCardById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getItemTypeName().equals(ItemTypeName.CARD);
    }

    @Override
    public boolean isPassById(int id) throws UnknownCatalogEntryException {
        return readCatalogEntry(id).getItemTypeName().equals(ItemTypeName.PASS);
    }

    ItemCatalog readCatalogEntry(int id) throws UnknownCatalogEntryException {
        Optional<ItemCatalog> entry = explorer.findCatalogEntryById(id);
        if (entry.isPresent()){
            return entry.get();
        }
        throw new UnknownCatalogEntryException(id);
    }


}
