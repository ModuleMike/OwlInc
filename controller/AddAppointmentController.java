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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Lubomski_WGU_C195.helper.Utility.navigateNewScene;
import static Lubomski_WGU_C195.DAO.AppointmentsDAO.*;
import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;
import static Lubomski_WGU_C195.model.Customer.*;
import static Lubomski_WGU_C195.model.Contact.*;
import static Lubomski_WGU_C195.helper.Time.*;

/**
 * Controller class for adding appointments in the application.
 * This class handles all the user interactions for adding a new appointment.
 */
public class AddAppointmentController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private ComboBox<String> addAppointment_UserID_ComboBox, addAppointment_Contact_ComboBox,
            addAppointment_Customer_ComboBox, addAppointment_StartTime_ComboBox, addAppointment_EndTime_ComboBox, addAppointment_Type_ComboBox;

    @FXML
    private TextField addAppointment_Description_Text;

    @FXML
    private DatePicker addAppointment_EndDate_DatePicker, addAppointment_StartDate_DatePicker;

    @FXML
    private TextField  addAppointment_Title_Text,
            addAppointment_Location_Text;

    /**
     * Initializes the controller class.
     * This method is called after the FXML file has been loaded and is used to initialize
     * the controller class. It sets the resource bundle and loads the data into the ComboBoxes.
     *
     * @param location The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        loadComboBoxes();
    }

    /**
     * Loads data into the ComboBoxes of the appointment form.
     * This method is used to populate the ComboBoxes with data from the database.
     */
    private void loadComboBoxes() {
        addAppointment_Type_ComboBox.setItems(AppointmentsDAO.getAppointmentTypes());
        addAppointment_Customer_ComboBox.setItems(Customer.getCustomerNames());
        addAppointment_Contact_ComboBox.setItems(Contact.getContactNames());
        addAppointment_UserID_ComboBox.setItems(User.getUserIDs());
        addAppointment_StartTime_ComboBox.setItems(Time.getPreComputedTimes());
        addAppointment_EndTime_ComboBox.setItems(Time.getPreComputedTimes());
    }

    /**
     * Handles the action of saving a new appointment.
     * This method is triggered when the save button is clicked. It validates the input,
     * checks for overlapping appointments and business hours, and then saves the appointment.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    void onAction_AddAppointment_Save(ActionEvent event) throws SQLException {

        if (addAppointment_Title_Text.getText().isEmpty() ||
                addAppointment_Description_Text.getText().isEmpty() ||
                addAppointment_Location_Text.getText().isEmpty() ||
                addAppointment_Type_ComboBox.getValue() == null ||
                addAppointment_StartDate_DatePicker.getValue() == null ||
                addAppointment_EndDate_DatePicker.getValue() == null ||
                addAppointment_StartTime_ComboBox.getValue() == null ||
                addAppointment_EndTime_ComboBox.getValue() == null ||
                addAppointment_Customer_ComboBox.getValue() == null ||
                addAppointment_UserID_ComboBox.getValue() == null ||
                addAppointment_Contact_ComboBox.getValue() == null) {

            sendAlertBox("Incomplete Information", "Please fill out all required fields.");
            return;
        }

        Integer customer_Id = getCustomerIDByName(addAppointment_Customer_ComboBox.getValue());
        if (customer_Id == null) {
            sendAlertBox("Error", "Invalid customer selection.");
            return;
        }

        LocalTime startTime = LocalTime.parse(addAppointment_StartTime_ComboBox.getValue(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime startDateTime = LocalDateTime.of(addAppointment_StartDate_DatePicker.getValue(), startTime);
        LocalTime endTime = LocalTime.parse(addAppointment_EndTime_ComboBox.getValue(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime endDateTime = LocalDateTime.of(addAppointment_EndDate_DatePicker.getValue(), endTime);

        if (!endDateTime.isAfter(startDateTime)) {
            sendAlertBox("Invalid Time", "Appointment's End time must be after start time.");
            return;
        }

        ObservableList<Appointment> customerAppointments = AppointmentsDAO.getAppointmentsByCustomerId(customer_Id);
        for (Appointment existingAppointment : customerAppointments) {
            LocalDateTime existingStart = parseAppointmentDateTime(existingAppointment.getAppointmentStart());
            LocalDateTime existingEnd = parseAppointmentDateTime(existingAppointment.getAppointmentEnd());

            if (startDateTime.isBefore(existingEnd) && endDateTime.isAfter(existingStart)) {
                sendAlertBox("Overlapping Appointment", "This customer already has an overlapping appointment.");
                return;
            }
        }

        if (!isAppointmentWithinBusinessHours(startDateTime, endDateTime)) {
            sendAlertBox("Outside Business Hours", "Appointment must be between 8 AM and 10 PM Eastern Time.");
            return;
        }

        String title = addAppointment_Title_Text.getText();
        String description = addAppointment_Description_Text.getText();
        String location = addAppointment_Location_Text.getText();
        String type = addAppointment_Type_ComboBox.getValue();
        Timestamp start = dateTimeCompiler(addAppointment_StartDate_DatePicker.getValue(), addAppointment_StartTime_ComboBox.getValue());
        Timestamp end = dateTimeCompiler(addAppointment_EndDate_DatePicker.getValue(), addAppointment_EndTime_ComboBox.getValue());
        Timestamp create_Date = Timestamp.from(Instant.now());
        String created_By = User.getCurrentLoggedInUserName();
        Timestamp last_updated = Timestamp.from(Instant.now());
        String last_Updated_By = User.getCurrentLoggedInUserName();
        Integer user_Id = Integer.parseInt(addAppointment_UserID_ComboBox.getValue());
        Integer contact_Id = getContactIDByName(addAppointment_Contact_ComboBox.getValue());

        addAppointment(title, description, location, type, start, end, create_Date, created_By, last_updated, last_Updated_By, customer_Id, user_Id, contact_Id);

        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

    /**
     * Handles the action of canceling adding an appointment.
     * This method is triggered when the cancel button is clicked. It navigates back to the main menu.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_AddAppointment_Cancel(ActionEvent event) {
        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }
}