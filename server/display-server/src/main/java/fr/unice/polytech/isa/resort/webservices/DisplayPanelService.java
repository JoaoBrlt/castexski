package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/displaypanel")
public interface DisplayPanelService {
    @WebMethod
    String addDisplayPanelToResort(@WebParam(name = "resortId") String resortId) throws ResortNotFoundException, ExternalServiceException;

    @WebMethod
    String addDisplayPanelToSkiTrail(@WebParam(name = "resortId") String resortId,
                                   @WebParam(name = "skiTrailId") String skiTrailId)
            throws ResortNotFoundException, SkiTrailNotFoundException, ExternalServiceException;

    @WebMethod
    void deleteDisplayPanel(@WebParam(name = "resortId") String resortId,
                            @WebParam(name = "displayPanelId") String displayPanelId)
            throws ResortNotFoundException, DisplayPanelNotFoundException;

    @WebMethod
    List<String> findDisplaysByResort(@WebParam(name = "resortId") String resortId)
            throws ResortNotFoundException;

    @WebMethod
    List<String> findDisplaysBySkiTrail(@WebParam(name = "skiTrailId") String skiTrailId)
            throws SkiTrailNotFoundException;

    @WebMethod
    void modifyDisplayedText(@WebParam(name = "displayPanelId") String displayPanelId,
                             @WebParam(name = "message") String message)
            throws DisplayPanelNotFoundException, ExternalServiceException;
}
