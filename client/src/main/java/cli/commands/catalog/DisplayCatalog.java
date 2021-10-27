package cli.commands.catalog;


import api.CastexSkiAPI;
import cli.framework.Command;
import cli.framework.Shell;
import stubs.catalog.CatalogWebService;

import java.util.List;

public class DisplayCatalog extends Command<CastexSkiAPI> {

    @Override
    public String identifier() {
        return "displayCatalog";
    }

    @Override
    public void execute() throws Exception {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < Shell.NB_INDENT; i++) { indent.append(Shell.INDENT); }
        CatalogWebService service = shell.system.catalogService;
        List<Integer> items = service.displayCatalog();
        StringBuilder res = new StringBuilder(indent + "Catalog:");
        indent.append("    ");
        for (int item : items) {
            String type = service.getEntryTypeById(item);
            String name = service.getEntryNameById(item);
            double adultPrice = service.getEntryRegularPriceById(item);
            double childPrice = service.getEntryChildrenPriceById(item);
            res.append("\n").append(indent).append("[").append(type);
            if (type.equalsIgnoreCase("PASS")) {
                res.append("/").append(service.getPassDurationById(item));
            }
            res.append("]:").append(name).append(" -> adults: ")
                .append(adultPrice).append("€ | children: ").append(childPrice).append("€");
        }
        if (items.isEmpty()) {
            res.append(indent).append("Empty catalog!");
        }
        System.out.println(res);
    }

    @Override
    public String describe() {
        return "# to display the CastexSki catalog";
    }
}
