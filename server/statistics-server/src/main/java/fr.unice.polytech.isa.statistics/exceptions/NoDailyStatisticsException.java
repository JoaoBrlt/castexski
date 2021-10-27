package fr.unice.polytech.isa.statistics.exceptions;

import java.time.LocalDate;

public class NoDailyStatisticsException extends Exception {
    private String date;

    public NoDailyStatisticsException(LocalDate date){
        super(String.valueOf(date));
        this.date = String.valueOf(date);
    }
}
