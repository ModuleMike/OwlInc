package Lubomski_WGU_C195.controller;

import Lubomski_WGU_C195.DAO.AppointmentsDAO;
import Lubomski_WGU_C195.helper.Time;
import Lubomski_WGU_C195.model.Appointment;
import Lubomski_WGU_C195.model.Contact;
import Lubomski_WGU_C195.model.Customer;
import Lubomski_WGU_C195.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

import static Lubomski_WGU_C195.helper.Utility.navigateNewScene;
import static Lubomski_WGU_C195.helper.Time.*;
import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;
import static Lubomski_WGU_C195.model.Contact.getContactIDByName;
import static Lubomski_WGU_C195.model.Customer.getCustomerIDByName;

/**
 * Controller for the Update Appointment screen of the application.
 * Manages the user interactions on the update appointment screen including loading, saving, and canceling appointment updates.
 */
public class UpdateAppointmentController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private ComboBox<String> updateAppointment_UserID_ComboBox, updateAppointment_Contact_ComboBox, updateAppointment_Type_ComboBox,
            updateAppointment_Customer_ComboBox, updateAppointment_StartTime_ComboBox, updateAppointment_EndTime_ComboBox;

    @FXML
    private Button updateAppointment_Cancel_Button, updateAppointment_Save_Button;

    @FXML
    private TextField updateAppointment_Description_Text;

    @FXML
    private DatePicker updateAppointment_EndDate_DatePicker, updateAppointment_StartDate_DatePicker;


    @FXML
    private TextField updateAppointment_ID_Text, updateAppointment_Title_Text,
            updateAppointment_Location_Text;


    /**
     * Initializes the controller class.
     * This method is called after the FXML file has been loaded and is used to initialize
     * the controller class.
     *
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {

        this.resourceBundle = resources;
    }

    /**
     * Loads data into ComboBoxes.
     * Populates the ComboBoxes with data for appointment types, customer names, contact names, and user IDs.
     */
    @FXML
    private void loadComboBoxes() {
        updateAppointment_Type_ComboBox.setItems(Appointment.getAppointmentTypes());
        ObservableList<String> customerNames = Customer.getCustomerNames();
        updateAppointment_Customer_ComboBox.setItems(customerNames);
        ObservableList<String> contactNames = Contact.getContactNames();
        updateAppointment_Contact_ComboBox.setItems(contactNames);
        ObservableList<String> userIds = User.getUserIDs();
        updateAppointment_UserID_ComboBox.setItems(userIds);
        updateAppointment_StartTime_ComboBox.setItems(Time.getPreComputedTimes());
        updateAppointment_EndTime_ComboBox.setItems(Time.getPreComputedTimes());
    }

    /**
     * Loads existing appointment data into the form for editing.
     * Loads the form fields with the data from the selected appointment.
     *
     * @param appointment The appointment data to load into the form.
     */
    @FXML
    public void loadAppointmentData(Appointment appointment) {
        loadComboBoxes();
        updateAppointment_ID_Text.setText(appointment.getAppointmentID().toString());
        updateAppointment_Type_ComboBox.getSelectionModel().select(appointment.getAppointmentType());
        updateAppointment_Title_Text.setText(appointment.getAppointmentTitle());
        updateAppointment_Location_Text.setText(appointment.getAppointmentLocation());
        updateAppointment_Description_Text.setText(appointment.getAppointmentDescription());
        updateAppointment_Customer_ComboBox.getSelectionModel().select(Customer.getCustomerNameById(appointment.getAppointmentCustomerID()));
        updateAppointment_Contact_ComboBox.getSelectionModel().select(appointment.getAppointmentContact());
        updateAppointment_UserID_ComboBox.getSelectionModel().select(appointment.getAppointmentUserID().toString());
        updateAppointment_StartDate_DatePicker.setValue(updateFormatDate(appointment.getAppointmentStart()));
        updateAppointment_StartTime_ComboBox.getSelectionModel().select(Time.setStartTime(appointment.getAppointmentStart()));
        updateAppointment_EndTime_ComboBox.getSelectionModel().select(Time.setStartTime(appointment.getAppointmentEnd()));
        updateAppointment_EndDate_DatePicker.setValue(updateFormatDate(appointment.getAppointmentEnd()));
    }

    /**
     * Saves the updated appointment data.
     * Validates the input fields for business hours, and existing appointments, and updates the appointment data in the database.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    public void onAction_UpdateAppt_Save(ActionEvent event) throws SQLException {
        if (updateAppointment_Title_Text.getText().isEmpty() ||
                updateAppointment_Description_Text.getText().isEmpty() ||
                updateAppointment_Location_Text.getText().isEmpty() ||
                updateAppointment_Type_ComboBox.getValue() == null ||
                updateAppointment_StartDate_DatePicker.getValue() == null ||
                updateAppointment_EndDate_DatePicker.getValue() == null ||
                updateAppointment_StartTime_ComboBox.getValue() == null ||
                updateAppointment_EndTime_ComboBox.getValue() == null ||
                updateAppointment_Customer_ComboBox.getValue() == null ||
                updateAppointment_UserID_ComboBox.getValue() == null ||
                updateAppointment_Contact_ComboBox.getValue() == null) {

            sendAlertBox("Incomplete Information", "Please fill out all required fields.");
            return;
        }

        Integer id = Integer.parseInt(updateAppointment_ID_Text.getText());
        String title = updateAppointment_Title_Text.getText();
        String description = updateAppointment_Description_Text.getText();
        String location = updateAppointment_Location_Text.getText();
        String type = updateAppointment_Type_ComboBox.getValue();
        Timestamp start = dateTimeCompiler(updateAppointment_StartDate_DatePicker.getValue(), updateAppointment_StartTime_ComboBox.getValue());
        Timestamp end = dateTimeCompiler(updateAppointment_EndDate_DatePicker.getValue(), updateAppointment_EndTime_ComboBox.getValue());
        Timestamp last_updated = Timestamp.from(Instant.now());
        String last_Updated_By = User.getCurrentLoggedInUserName();
        Integer user_Id = Integer.parseInt(updateAppointment_UserID_ComboBox.getValue());
        Integer contact_Id = getContactIDByName(updateAppointment_Contact_ComboBox.getValue());

        Integer customer_Id = getCustomerIDByName(updateAppointment_Customer_ComboBox.getValue());
        if (customer_Id == null) {
            sendAlertBox("Error", "Invalid customer selection.");
            return;
        }

        LocalDateTime startDateTime = Time.parseUTCDateTimeForUpdate(start.toString());
        LocalDateTime endDateTime = Time.parseUTCDateTimeForUpdate(end.toString());

        if (!endDateTime.isAfter(startDateTime)) {
            sendAlertBox("Invalid Time", "Appointment's End time must be after start time.");
            return;
        }

        ObservableList<Appointment> customerAppointments = AppointmentsDAO.getAppointmentsByCustomerId(customer_Id);
        for (Appointment existingAppointment : customerAppointments) {
            if (existingAppointment.getAppointmentID().equals(id)) {
                continue;
            }

            LocalDateTime existingStart = Time.parseAppointmentDateTime(existingAppointment.getAppointmentStart());
            LocalDateTime existingEnd = Time.parseAppointmentDateTime(existingAppointment.getAppointmentEnd());

            if (startDateTime.isBefore(existingEnd) && endDateTime.isAfter(existingStart)) {
                sendAlertBox("Overlapping Appointment", "This customer already has an overlapping appointment.");
                return;
            }
        }

        if (!Time.isAppointmentWithinBusinessHours(startDateTime, endDateTime)) {
            sendAlertBox("Outside Business Hours", "Appointment must be between 8 AM and 10 PM Eastern Time.");
            return;
        }

        AppointmentsDAO.updateAppointment(id, title, description, location, type, start, end, last_Updated_By, customer_Id, user_Id, contact_Id);

        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

    /**
     * Cancels the update operation and navigates back to the main menu.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_updateAppt_Cancel(ActionEvent event) {
        navigateNewScene( event,"/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

}