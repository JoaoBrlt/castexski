package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiTrailNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/resort")
public interface ResortService {
    @WebMethod
    void addResort(
        @WebParam(name = "name") String name,
        @WebParam(name= "email") String email,
        @WebParam(name = "isOpen") boolean isOpen,
        @WebParam(name= "cityName") String cityName
    ) throws UnavailableNameException;

    @WebMethod
    void removeResort(
        @WebParam(name = "id") String id
    ) throws ResortNotFoundException;

    @WebMethod
    String findResortByName(
        @WebParam(name = "name") String name
    ) throws ResortNotFoundException;

    @WebMethod
    void addSkiLift(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "name") String name,
        @WebParam(name = "isOpen") boolean isOpen
    ) throws UnavailableNameException, ResortNotFoundException;

    @WebMethod
    void addDoubleSkiLift(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "name") String name,
        @WebParam(name = "isOpen") boolean isOpen,
        @WebParam(name= "timeLimit") String timeLimit
    ) throws UnavailableNameException, ResortNotFoundException;

    @WebMethod
    void removeSkiLift(
        @WebParam(name = "id") String id
    ) throws SkiLiftNotFoundException;

    @WebMethod
    String findSkiLiftByName(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "name") String name
    ) throws ResortNotFoundException, SkiLiftNotFoundException;

    @WebMethod
    void addSkiTrail(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "name") String name,
        @WebParam(name = "isOpen") boolean isOpen
    ) throws UnavailableNameException, ResortNotFoundException;

    @WebMethod
    void removeSkiTrail(
        @WebParam(name = "resortId")String resortId,
        @WebParam(name = "id") String id
    ) throws SkiTrailNotFoundException, ResortNotFoundException;

    @WebMethod
    String findSkiTrailByName(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "name") String name
    ) throws ResortNotFoundException, SkiTrailNotFoundException;

    @WebMethod
    void changeResortOpenness(
        @WebParam(name = "resortId") String resortId,
        @WebParam(name = "isOpen") boolean isOpen
    ) throws ResortNotFoundException;

    @WebMethod
    void changeSkiLiftOpenness(
        @WebParam(name = "skiLiftId") String skiLiftId,
        @WebParam(name = "isOpen") boolean isOpen
    ) throws SkiLiftNotFoundException;

    @WebMethod
    void changeSkiTrailOpenness(
        @WebParam(name = "skiTrailId") String skiTrailId,
        @WebParam(name = "isOpen") boolean isOpen
    ) throws SkiTrailNotFoundException;
}
