package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.DisplayFinder;
import fr.unice.polytech.isa.resort.interfaces.DisplayAccessor;
import fr.unice.polytech.isa.resort.interfaces.DisplayRegister;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.List;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/displaypanel")
@Stateless(name = "DisplayPanelWS")
public class DisplayPanelServiceImpl implements DisplayPanelService{
    @EJB
    DisplayRegister displayRegister;

    @EJB
    DisplayFinder displayFinder;

    @EJB
    DisplayAccessor displayAccessor;

    @Override
    public String addDisplayPanelToResort(String resortId) throws ResortNotFoundException, ExternalServiceException {
        return displayRegister.addDisplayPanelToResort(resortId);
    }

    @Override
    public String addDisplayPanelToSkiTrail(String resortId, String skiTrailId) throws ResortNotFoundException, SkiTrailNotFoundException, ExternalServiceException {
        return displayRegister.addDisplayPanelToSkiTrail(resortId, skiTrailId);
    }

    @Override
    public void deleteDisplayPanel(String resortId, String displayPanelId) throws ResortNotFoundException, DisplayPanelNotFoundException {
        displayRegister.deleteDisplayPanel(resortId, displayPanelId);
    }

    @Override
    public List<String> findDisplaysByResort(String resortId) throws ResortNotFoundException {
        return displayFinder.findDisplaysByResort(resortId)
                .stream()
                .map(displayPanel -> displayPanel.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findDisplaysBySkiTrail(String skiTrailId) throws SkiTrailNotFoundException {
        return displayFinder.findDisplaysBySkiTrail(skiTrailId)
            .stream()
            .map(displayPanel -> displayPanel.getId())
            .collect(Collectors.toList());
    }

    @Override
    public void modifyDisplayedText(String displayPanelId, String message) throws DisplayPanelNotFoundException, ExternalServiceException {
        displayAccessor.modifyDisplayedText(displayPanelId, message);
    }
}
