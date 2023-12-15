package Lubomski_WGU_C195.controller;

import Lubomski_WGU_C195.DAO.AppointmentsDAO;
import Lubomski_WGU_C195.DAO.CustomerDAO;
import Lubomski_WGU_C195.model.Appointment;
import Lubomski_WGU_C195.model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Lubomski_WGU_C195.helper.Time.calculatePeriod;
import static Lubomski_WGU_C195.helper.Utility.*;

/**
 * Controller for the Main Menu screen.
 * Initializes and handles user interactions on the Main Menu screen.
 */
public class MainMenuController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private TableView<Appointment> mainMenu_AppointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentTable_ApptID_Column, appointmentTable_CustomerID_Column,
            appointmentTable_UserID_Column, appointmentTable_Contact_Column;
    @FXML
    private TableColumn<Appointment, String> appointmentTable_Type_Column, appointmentTable_Title_Column, appointmentTable_Description_Column,
            appointmentTable_StartDateTime_Column, appointmentTable_EndDateTime_Column, appointmentTable_Location_Column;
    @FXML
    private TableView<Customer> mainMenu_Customer_TableView;

    @FXML
    private TableColumn<Customer, String> customerTable_Name_Column, customerTable_PhoneNum_Column, customerTable_PostalCode_Column,
            customerTable_Address_Column, customerTable_Division_Column, customerTable_Country_Column;

    @FXML
    private TableColumn<Customer, Integer> customerTable_ID_Column;

    @FXML
    private RadioButton mainMenu_FilterReset_RadioButton, mainMenu_FilterWeekly_RadioButton, mainMenu_FilterMonthly_RadioButton;
    ;

    @FXML
    private ToggleGroup ApptFilter_Group;

    /**
     * Initializes the controller and loads data into tables.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        try {
            ObservableList<Appointment> appointmentFullList = AppointmentsDAO.appointmentImportSQL();

            mainMenu_AppointmentTableView.setItems(appointmentFullList);

        } catch (SQLException ePrompt) {
            sendAlertBox("Error Import Appointment Data", "Unable to import appointment list from import data method");
        }
        appointmentTable_ApptID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTable_Type_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentTable_Title_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentTable_Description_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentTable_StartDateTime_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentTable_EndDateTime_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentTable_Location_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentTable_CustomerID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        appointmentTable_Contact_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        appointmentTable_UserID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentUserID"));

        try {
            ObservableList<Customer> customerFullList = CustomerDAO.customerImportSQL();
            mainMenu_Customer_TableView.setItems(customerFullList);
        } catch (SQLException ePrompt) {
            sendAlertBox("Error Import Customer Data", "Unable to import Customer list from import data method");
        }
        customerTable_ID_Column.setCellValueFactory((new PropertyValueFactory<>("customerID")));
        customerTable_Name_Column.setCellValueFactory((new PropertyValueFactory<>("customerName")));
        customerTable_PhoneNum_Column.setCellValueFactory((new PropertyValueFactory<>("customerPhoneNum")));
        customerTable_Address_Column.setCellValueFactory((new PropertyValueFactory<>("customerAddress")));
        customerTable_PostalCode_Column.setCellValueFactory((new PropertyValueFactory<>("customerPostalCode")));
        customerTable_Division_Column.setCellValueFactory((new PropertyValueFactory<>("customerDivision")));
        customerTable_Country_Column.setCellValueFactory((new PropertyValueFactory<>("customerCountry")));

    }

    /**
     * Handles the action to add an appointment. Navigates to the Add Appointment screen.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    private void onAction_MainMenu_AddAppointment(ActionEvent event) {
        navigateNewScene(event, "/Lubomski_WGU_C195/view/AddAppointment.fxml", resourceBundle);
    }

    /**
     * Handles the action to update an appointment. Navigates to the Update Appointment screen if an appointment is selected.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_UpdateAppointment(ActionEvent event) {
        Appointment selectedAppointment = mainMenu_AppointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            sendAlertBox("No Selection", "Please select an appointment to update.");
            return;
        }
        navigateToUpdateScene(event, "/Lubomski_WGU_C195/view/UpdateAppointment.fxml", selectedAppointment, resourceBundle);
    }

    /**
     * Handles the action to delete an appointment. Confirms and deletes the selected appointment.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If an SQL error occurs.
     */
    @FXML
    void onAction_MainMenu_DeleteAppointment(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = mainMenu_AppointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            sendAlertBox("No Selection", "Please select an appointment to delete.");
            return;
        }

        boolean isDeleted = Appointment.confirmAndDeleteAppointment(selectedAppointment);
        if (isDeleted) {
            mainMenu_AppointmentTableView.getItems().remove(selectedAppointment);
        }
    }

    /**
     * Handles the action to add a new customer. Navigates to the Add Customer screen.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_AddCustomer(ActionEvent event) {

        navigateNewScene(event, "/Lubomski_WGU_C195/view/AddCustomer.fxml", resourceBundle);
    }

    /**
     * Handles the action to update a customer. Navigates to the Update Customer screen if a customer is selected.
     *
     * @param event The event(button) that triggered this action.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If an SQL error occurs.
     */
    @FXML
    void onAction_MainMenu_UpdateCustomer(ActionEvent event) {

        Customer selectedCustomer = mainMenu_Customer_TableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            sendAlertBox("No Selection", "Please select a customer to update.");
            return;
        }
        navigateToCustomerUpdateScene(event, "/Lubomski_WGU_C195/view/UpdateCustomer.fxml", selectedCustomer, resourceBundle);
    }

    /**
     * Handles the action to delete a customer. Confirms and deletes the selected customer if they have no current appointments.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If an SQL error occurs.
     */
    @FXML
    void onAction_MainMenu_DeleteCustomer(ActionEvent event) throws SQLException {
        Customer selectedCustomer = mainMenu_Customer_TableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            sendAlertBox("No Selection", "Please select a customer to delete.");
            return;
        }

        if (CustomerDAO.customerHasAppointments(selectedCustomer.getCustomerID())) {
            sendAlertBox("Deletion Error", "The customer " + selectedCustomer.getCustomerName() + " has appointments. Delete the appointments before deleting the customer.");
            return;
        }

        boolean deleteConfirmed = Customer.confirmAndInitiateDeletion(selectedCustomer);
        if (deleteConfirmed) {
            mainMenu_Customer_TableView.getItems().remove(selectedCustomer);
            sendAlertBox("Customer Deleted", "Customer " + selectedCustomer.getCustomerName() + " has been successfully deleted.");
        }
    }

    /**
     * Handles the action to log off. Navigates back to the Login screen.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_LogOff(ActionEvent event) {

        navigateNewScene(event, "/Lubomski_WGU_C195/view/LoginScreen.fxml", resourceBundle);
    }

    /**
     * Handles the action to navigate to the Reports screen.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_Reports(ActionEvent event) {

        navigateNewScene(event, "/Lubomski_WGU_C195/view/Reports.fxml", resourceBundle);
    }

    /**
     * Handles the action to exit the application.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_Exit(ActionEvent event) {

        exitApplication(event);
    }

    /**
     * Filters appointments to show only those within the upcoming week.
     *
     * @param event The event(radio button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_FilterWeekly(ActionEvent event) {

        ObservableList<Appointment> filteredAppointments = Appointment.getFilteredAppointments(calculatePeriod("weekly").getKey(),
                calculatePeriod("weekly").getValue());
        mainMenu_AppointmentTableView.setItems(filteredAppointments);
    }

    /**
     * Filters appointments to show only those within the upcoming month.
     *
     * @param event The event(radio button) that triggered this action.
     */
    @FXML
    void onAction_MainMenu_FilterMonthly(ActionEvent event) {

        ObservableList<Appointment> filteredAppointments = Appointment.getFilteredAppointments(calculatePeriod("monthly").getKey(),
                calculatePeriod("monthly").getValue());
        mainMenu_AppointmentTableView.setItems(filteredAppointments);
    }

    /**
     * Resets the appointment filter to show all appointments.
     *
     * @param event The event(radio button) that triggered this action.
     * @throws SQLException If an SQL error occurs.
     */
    @FXML
    void onAction_MainMenu_FilterReset(ActionEvent event) throws SQLException {

        ObservableList<Appointment> appointmentFullList = AppointmentsDAO.appointmentImportSQL();
        mainMenu_AppointmentTableView.setItems(appointmentFullList);
    }
}