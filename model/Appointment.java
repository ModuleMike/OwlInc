package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.AppointmentsDAO;
import Lubomski_WGU_C195.helper.Time;
import Lubomski_WGU_C195.helper.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;

/**
 * Models an appointment and provides functions related to appointment data management.
 */
public class Appointment {


    private Integer appointmentID, appointmentCustomerID, appointmentUserID;
    private String appointmentType, appointmentTitle, appointmentDescription, appointmentLocation, appointmentContact,
            appointmentStart, appointmentEnd;;

    public Appointment(Integer appointmentID, String appointmentType, String appointmentTitle, String appointmentDescription,
                       String appointmentStart, String appointmentEnd, String appointmentLocation,
                       Integer appointmentCustomerID, String appointmentContact, Integer appointmentUserID){

        this.appointmentID = appointmentID;
        this.appointmentType = appointmentType;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.appointmentLocation = appointmentLocation;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentContact = appointmentContact;
        this.appointmentUserID = appointmentUserID;
    }

    /**
     * Gets the ID of the appointment.
     *
     * @return The ID of the appointment.
     */
    public Integer getAppointmentID() {
        return appointmentID;
    }

    /**
     * Retrieves the type of the appointment.
     *
     * @return The type of the appointment.
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Retrieves the title of the appointment.
     *
     * @return The title of the appointment.
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * Retrieves the description of the appointment.
     *
     * @return The description of the appointment.
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * Retrieves the start date and time of the appointment.
     *
     * @return The start date and time of the appointment in a formatted string.
     */
    public String getAppointmentStart() {
        return appointmentStart;
    }

    /**
     * Retrieves the end date and time of the appointment.
     *
     * @return The end date and time of the appointment in a formatted string.
     */
    public String getAppointmentEnd() {
        return appointmentEnd;
    }

    /**
     * Retrieves the location of the appointment.
     *
     * @return The location of the appointment.
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * Retrieves the customer ID associated with the appointment.
     *
     * @return The ID of the customer associated with the appointment.
     */
    public Integer getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    /**
     * Retrieves the contact name associated with the appointment.
     *
     * @return The contact name associated with the appointment.
     */
    public String getAppointmentContact() {
        return appointmentContact;
    }

    /**
     * Retrieves the user ID of the user who created the appointment.
     *
     * @return The user ID of the user who created the appointment.
     */
    public Integer getAppointmentUserID() {
        return appointmentUserID;
    }

    /**
     * Retrieves a filtered list of appointments within a specified date range.
     * Filters appointments based on their start date to fall within the given range.
     *
     * @param start The start date of the range.
     * @param end   The end date of the range.
     * @return An ObservableList of filtered appointments.
     */
    public static ObservableList<Appointment> getFilteredAppointments(LocalDate start, LocalDate end) {

        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        try {
            for (Appointment appointment : AppointmentsDAO.appointmentImportSQL()) {
                LocalDate appointmentDate = Time.parseAppointmentDate(appointment.getAppointmentStart());
                if (appointmentDate != null && (appointmentDate.isEqual(start) || appointmentDate.isAfter(start)) &&
                        (appointmentDate.isBefore(end) || appointmentDate.isEqual(end))) {
                    filteredAppointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            sendAlertBox("Error", "Failed to filter appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return filteredAppointments;
    }

    /**
     * Checks for upcoming appointments within the next 15 minutes.
     * Alerts the user if there is an appointment starting soon.
     */
    public static void checkForUpcomingAppointments() {
        boolean upcomingAppointmentFound = false;
        try {
            ObservableList<Appointment> appointments = AppointmentsDAO.appointmentImportSQL();
            LocalDateTime now = LocalDateTime.now();

            for (Appointment appointment : appointments) {
                LocalDateTime appointmentStart = Time.parseAppointmentDateTime(appointment.getAppointmentStart());

                long minutesUntilAppointment = Duration.between(now, appointmentStart).toMinutes();
                if (minutesUntilAppointment >= 0 && minutesUntilAppointment <= 15) {
                    upcomingAppointmentFound = true;
                    Integer appointmentID = appointment.getAppointmentID();
                    Integer customerID = appointment.getAppointmentCustomerID();

                    String customerName = Customer.getCustomerNameById(customerID);

                    ZonedDateTime localAppointmentStart = appointmentStart.atZone(ZoneId.systemDefault());
                    DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    sendAlertBox("Upcoming Appointment", "You have an appointment (ID: " + appointmentID
                            + ") with customer " + customerName + " (ID: " + customerID + ")"
                            + " scheduled at " + localAppointmentStart.format(localFormatter)
                            + " local time, within the next 15 minutes.");
                    break;
                }

            }
            if (!upcomingAppointmentFound) {
                sendAlertBox("No Upcoming Appointments", "There are no appointments scheduled within the next 15 minutes.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of appointment types.
     * Extracts appointment types from all appointments.
     *
     * @return An ObservableList of appointment types.
     */
    public static ObservableList<String> getAppointmentTypes() {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        try {
            ObservableList<Appointment> appointments = AppointmentsDAO.appointmentImportSQL();
            for (Appointment appointment : appointments) {
                String appointmentType = appointment.getAppointmentType();
                if (!appointmentTypes.contains(appointmentType)) {
                    appointmentTypes.add(appointmentType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentTypes;
    }


    /**
     * Confirms and deletes a selected appointment.
     * Asks for confirmation before deleting an appointment and deletes it if confirmed.
     *
     * @param appointment The appointment to be deleted.
     * @return true if the appointment is successfully deleted, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean confirmAndDeleteAppointment(Appointment appointment) throws SQLException {
        Optional<ButtonType> action = Utility.sendConfirmationAlert("Confirm Deletion",
                "Are you sure you want to delete the selected appointment?");

        if (action.isPresent() && action.get() == ButtonType.OK) {
            Integer appointmentID = appointment.getAppointmentID();
            String appointmentType = appointment.getAppointmentType();

            boolean isDeleted = AppointmentsDAO.deleteAppointment(appointmentID);
            if (isDeleted) {
                Utility.sendAlertBox("Appointment Canceled", "Appointment ID: " + appointmentID +
                        ", Type: " + appointmentType + " has been canceled.");
                return true;
            }
        }
        return false;
    }
}