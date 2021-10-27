package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;

import javax.ejb.Local;

@Local
public interface DisplayAccessor {
    String getDisplayedString(String displayPanelId)throws DisplayPanelNotFoundException, ExternalServiceException;
    boolean getDisplayPanelState(String displayPanelId)throws DisplayPanelNotFoundException, ExternalServiceException;
    void modifyDisplayedText(String displayPanelId, String message) throws DisplayPanelNotFoundException, ExternalServiceException;
}
