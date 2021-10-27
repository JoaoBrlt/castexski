package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/resort")
@Stateless(name = "ResortWS")
public class ResortServiceImpl implements ResortService {
    @EJB
    private ResortRegister resortRegister;

    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiLiftRegister skiLiftRegister;

    @EJB
    private SkiLiftFinder skiLiftFinder;

    @EJB
    private SkiTrailRegister skiTrailRegister;

    @EJB
    private SkiTrailFinder skiTrailFinder;

    @EJB
    private OpennessChanger opennessChanger;

    @Override
    public void addResort(String name, String email, boolean isOpen, String cityName) throws UnavailableNameException {
        resortRegister.registerResort(name, email, isOpen, cityName);
    }

    @Override
    public void removeResort(String id) throws ResortNotFoundException {
        resortRegister.deleteResort(id);
    }

    @Override
    public String findResortByName(String name) throws ResortNotFoundException {
        return resortFinder.findByName(name).getId();
    }

    @Override
    public void addSkiLift(String resortId, String name, boolean isOpen) throws UnavailableNameException, ResortNotFoundException {
        skiLiftRegister.registerSkiLift(resortId, name, isOpen);
    }

    @Override
    public void addDoubleSkiLift(String resortId, String name, boolean isOpen, String timeLimit) throws UnavailableNameException, ResortNotFoundException {
        skiLiftRegister.registerDoubleSkiLift(resortId, name, isOpen, timeLimit);
    }

    @Override
    public void removeSkiLift(String id) throws SkiLiftNotFoundException {
        skiLiftRegister.deleteSkiLift(id);
    }

    @Override
    public String findSkiLiftByName(String resortId, String name) throws ResortNotFoundException, SkiLiftNotFoundException {
        return skiLiftFinder.findByName(resortId, name).getId();
    }

    @Override
    public void addSkiTrail(String resortId, String name, boolean isOpen) throws UnavailableNameException, ResortNotFoundException {
        skiTrailRegister.registerSkiTrail(resortId, name, isOpen);
    }

    @Override
    public void removeSkiTrail(String resortId, String id) throws SkiTrailNotFoundException, ResortNotFoundException {
        skiTrailRegister.deleteSkiTrail(resortId, id);
    }

    @Override
    public String findSkiTrailByName(String resortId, String name) throws ResortNotFoundException, SkiTrailNotFoundException {
        return skiTrailFinder.findByName(resortId, name).getId();
    }

    @Override
    public void changeResortOpenness(String resortId, boolean isOpen) throws ResortNotFoundException {
        opennessChanger.changeResortOpenness(resortId, isOpen);
    }

    @Override
    public void changeSkiLiftOpenness(String skiLiftId, boolean isOpen) throws SkiLiftNotFoundException {
        opennessChanger.changeSkiLiftOpenness(skiLiftId, isOpen);
    }

    @Override
    public void changeSkiTrailOpenness(String skiTrailId, boolean isOpen) throws SkiTrailNotFoundException {
        opennessChanger.changeSkiTrailOpenness(skiTrailId, isOpen);
    }
}
