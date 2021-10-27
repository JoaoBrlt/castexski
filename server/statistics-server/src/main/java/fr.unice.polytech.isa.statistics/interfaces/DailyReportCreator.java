package fr.unice.polytech.isa.statistics.interfaces;

import javax.ejb.Local;
import java.time.LocalDate;

@Local
public interface DailyReportCreator {
    String constructPresenceReportOfDate(String resortName, LocalDate date);
    String buildSpecificReport(String introduction, String ending, String resortName, LocalDate date);
}
