package fr.unice.polytech.isa.statistics.exceptions;

public class HourlyStatisticsAlreadyExists extends Exception {
    private String hour;

    public HourlyStatisticsAlreadyExists(int hour){
        super(String.valueOf(hour));
        this.hour = String.valueOf(hour);
    }
}
