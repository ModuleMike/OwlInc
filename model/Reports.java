package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.AppointmentsDAO;
import Lubomski_WGU_C195.helper.Time;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Models Reports and provides functions for generating report-related data.
 * This class includes methods to calculate counts of different types of appointments by month.
 */
public class Reports {

    /**
     * Retrieves and counts the number of "Planning Session" appointments for each month.
     * The method queries all appointments, filters them by the "Planning Session" type, and then counts
     * them by month.
     *
     * @return A Map where each key is a month and the value is the count of "Planning Session" appointments in that month.
     */
    public static Map<String, Integer> getPlanningSessionCountsByMonth() {
        Map<String, Integer> monthCounts = new HashMap<>();
        try {
            ObservableList<Appointment> appointments = AppointmentsDAO.appointmentImportSQL();

            for (Appointment appointment : appointments) {
                if ("Planning Session".equals(appointment.getAppointmentType())) {
                    String month = Time.parseAppointmentDate(appointment.getAppointmentStart()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                    monthCounts.putIfAbsent(month, 0);
                    monthCounts.put(month, monthCounts.get(month) + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthCounts;
    }

    /**
     * Retrieves and counts the number of "De-Briefing" appointments for each month.
     * The method queries all appointments, filters them by the "De-Briefing" type, and then counts
     * them by month.
     *
     * @return A Map where each key is a month and the value is the count of "De-Briefing" appointments in that month.
     */
    public static Map<String, Integer> getDeBriefingSessionCountsByMonth() {
        Map<String, Integer> monthCounts = new HashMap<>();
        try {
            ObservableList<Appointment> appointments = AppointmentsDAO.appointmentImportSQL();
            for (Appointment appointment : appointments) {
                if ("De-Briefing".equals(appointment.getAppointmentType())) {
                    String month = Time.parseAppointmentDate(appointment.getAppointmentStart()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                    monthCounts.putIfAbsent(month, 0);
                    monthCounts.put(month, monthCounts.get(month) + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthCounts;
    }
}