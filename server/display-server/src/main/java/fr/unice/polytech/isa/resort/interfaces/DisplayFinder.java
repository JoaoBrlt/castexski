package fr.unice.polytech.isa.resort.interfaces;

import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.resort.exceptions.DisplayPanelNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DisplayFinder {
    DisplayPanel findDisplayById(String displayPanelId) throws DisplayPanelNotFoundException;

    List<DisplayPanel> findDisplaysByResort(String resortId) throws ResortNotFoundException;

    List<DisplayPanel> findDisplaysBySkiTrail(String skiTrailId) throws SkiTrailNotFoundException;
}
