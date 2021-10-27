package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.ejb.Local;

@Local
public interface DisplayRegister {
    String addDisplayPanelToResort(String resortId) throws ResortNotFoundException, ExternalServiceException;

    String addDisplayPanelToSkiTrail(String resortId, String skiTrailId) throws ResortNotFoundException, SkiTrailNotFoundException, ExternalServiceException;

    void deleteDisplayPanel(String resortId, String displayPanelId) throws ResortNotFoundException, DisplayPanelNotFoundException;
}
