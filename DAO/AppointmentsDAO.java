package Lubomski_WGU_C195.DAO;

import Lubomski_WGU_C195.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.Instant;

import static Lubomski_WGU_C195.DAO.ContactsDAO.*;
import static Lubomski_WGU_C195.helper.Time.*;
import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;

/**
 * Data Access Object for handling appointment related database operations.
 * Provides methods to import, add, update, and delete appointments, and to retrieve appointment types.
 */
public abstract class AppointmentsDAO {

    /**
     * Imports all appointments from the database.
     * Retrieves all appointment records from the database and converts them into Appointment objects.
     *
     * @return An ObservableList of Appointment objects.
     * @throws SQLException If a database access error occurs.
     */
    public static ObservableList<Appointment> appointmentImportSQL() throws SQLException{
        ObservableList<Appointment> appointmentArrayList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Type"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        utcConvertLocal(rs.getString("Start")),
                        utcConvertLocal(rs.getString("End")),
                        rs.getString("Location"),
                        rs.getInt("Customer_ID"),
                        contactName(rs.getInt("Contact_ID")),
                        rs.getInt("User_ID")
                );

                appointmentArrayList.add(appointment);
            }
        }catch(SQLException ePrompt){
            sendAlertBox("Error Import Appointment Data", "Unable to import Appointment SQL Data");
        }
        return appointmentArrayList;
    }

    /**
     * Adds a new appointment to the database.
     * Inserts a new appointment record into the database.
     *
     * @param title, description, location, type, start, end, create_Date, created_By, last_Updated, last_Updated_By, customer_Id, user_Id, contact_Id Appointment details for the new record.
     * @throws SQLException If a database access error occurs.
     */
    public static void addAppointment(String title, String description, String location, String type, Timestamp start,
                                      Timestamp end, Timestamp create_Date, String created_By, Timestamp last_Updated,
                                      String last_Updated_By, Integer customer_Id, Integer user_Id, Integer contact_Id ) {
        String sql = "INSERT INTO appointments ( Title, Description, Location, Type, Start, End, Create_Date," +
                " Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, create_Date);
            ps.setString(8, created_By);
            ps.setTimestamp(9, last_Updated);
            ps.setString(10, last_Updated_By);
            ps.setInt(11, customer_Id);
            ps.setInt(12, user_Id);
            ps.setInt(13, contact_Id);

            ps.executeUpdate();
        } catch (SQLException e) {
            sendAlertBox("Error Adding Appointment", "Unable to add appointment: " + e.getMessage());
        }
    }

    /**
     * Updates an existing appointment in the database.
     * Updates the details of an existing appointment using the appointmentId.
     *
     * @param appointmentId, title, description, location, type, start, end, lastUpdatedBy, customerId, userId, contactId Updated details for the appointment record.
     * @throws SQLException If a database access error occurs.
     */
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type, Timestamp start, Timestamp end, String lastUpdatedBy, Integer customerId, Integer userId, Integer contactId) {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, Timestamp.from(Instant.now()));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerId);
            ps.setInt(10, userId);
            ps.setInt(11, contactId);
            ps.setInt(12, appointmentId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an appointment from the database.
     * Removes the appointment record identified by the appointmentID from the database.
     *
     * @param appointmentID The ID of the appointment to delete.
     * @return A boolean indicating whether the deletion was successful.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean deleteAppointment(Integer appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)){

            ps.setInt(1, appointmentID);
            ps.executeUpdate();
        }
        return true;
    }

    /**
     * Retrieves appointments for a specific customer.
     * Queries the database for all appointments associated with the customer ID.
     *
     * @param customerId The ID of the customer to retrieve appointments for.
     * @return An ObservableList of Appointment objects for the customer.
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerId(int customerId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Type"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        utcConvertLocal(rs.getString("Start")),
                        utcConvertLocal(rs.getString("End")),
                        rs.getString("Location"),
                        rs.getInt("Customer_ID"),
                        contactName(rs.getInt("Contact_ID")),
                        rs.getInt("User_ID")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendAlertBox("Error", "Unable to retrieve appointments for the customer.");
        }
        return appointments;
    }

    /**
     * Retrieves all unique appointment types from the database.
     * Queries the database for distinct appointment types and returns them in a list.
     *
     * @return An ObservableList of strings representing the distinct appointment types.
     */
    public static ObservableList<String> getAppointmentTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Type FROM appointments";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
                while (rs.next()) { String type = rs.getString("Type");
                    types.add(type);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                sendAlertBox("Error", "Unable to retrieve appointment types.");
            }
            return types;
        }

}