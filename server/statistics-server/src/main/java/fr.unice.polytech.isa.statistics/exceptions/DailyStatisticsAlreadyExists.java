package fr.unice.polytech.isa.statistics.exceptions;

import java.time.LocalDate;

public class DailyStatisticsAlreadyExists extends Exception {
    private String date;

    public DailyStatisticsAlreadyExists(LocalDate date){
        super(String.valueOf(date));
        this.date = String.valueOf(date);
    }
}
