package Lubomski_WGU_C195.controller;

import Lubomski_WGU_C195.DAO.AppointmentsDAO;
import Lubomski_WGU_C195.helper.Time;
import Lubomski_WGU_C195.model.Appointment;
import Lubomski_WGU_C195.model.Contact;
import Lubomski_WGU_C195.model.Customer;
import Lubomski_WGU_C195.model.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import static Lubomski_WGU_C195.helper.Utility.*;
import static Lubomski_WGU_C195.helper.Utility.navigateNewScene;

/**
 * Controller class for the Reports screen in the application.
 * Manages the display and interaction of various reports related to appointments, customers, and contacts.
 */
public class ReportsController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private TableView<Time> reports_MonthlyType_TableView;

    @FXML
    private TableColumn<Time, String> report1_Month_Column;


    @FXML
    private TableColumn<Appointment, Integer> report1_MonthlyTotal_Column, report1_Type1_Column, report1_Type2_Column;


    @FXML
    private Button reports_Exit_Button, reports_LogOff_Button1, reports_LogOff_Button;

    @FXML
    private TableView<Appointment> reports_Customer_TableView;

    @FXML
    private TableView<Appointment> reports_Contact_TableView;

    @FXML
    private Text report2_Total_Text, report3_CustomerTotal_Text;

    @FXML
    private TableColumn<Appointment, Integer> report3_UserID_Column, report3_Start_Column, report3_End_Column, report3_AppointmentID_Column;

    @FXML
    private TableColumn<Appointment, Integer> report2_AppointmentID_Column, report2_CustomerID_Column,
            report2_UserID_Column;
    @FXML
    private TableColumn<Appointment, String> report2_Type_Column, report2_Title_Column, report2_Description_Column,
            report2_End_Column, report2_Start_Column, report2_Location_Column;
    @FXML
    private TableColumn<Appointment, String> report3_Description_Column, report3_Location_Column,
            report3_Title_Column, report3_Type_Column, report3_Contact_Column;

    @FXML
    private ComboBox<String> report2_SelectContact_ComboBox;

    @FXML
    private ComboBox<String> report3_SelectCustomer_ComboBox;

    /**
     * Initializes the controller class.
     * This method is called after all FXML annotated fields are populated. It initializes
     * tables, loads combo boxes and sets up the initial state of the reports screen.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        populateTable();
        loadContactComboBox();
        loadCustomerComboBox();
        initializeContactTableView();
        initializeCustomerTableView();
    }

    /**
     * Initializes the table view for contacts.
     * Sets up the columns and loads the appointments related to contacts.
     */
    private void initializeContactTableView() {
        try {
            ObservableList<Appointment> appointmentFullList = AppointmentsDAO.appointmentImportSQL();
            reports_Contact_TableView.setItems(appointmentFullList);
        } catch (SQLException e) {
            sendAlertBox("Error Import Appointment Data", "Unable to import appointment list from import data method");
        }

        report2_AppointmentID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        report2_Type_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        report2_Title_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        report2_Description_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        report2_Start_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        report2_End_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        report2_Location_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        report2_CustomerID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        report2_UserID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentUserID"));
    }

    /**
     * Initializes the table view for customers.
     * Sets up the columns and loads the appointments related to customers.
     */
    private void initializeCustomerTableView() {
        try {
            ObservableList<Appointment> appointmentFullList = AppointmentsDAO.appointmentImportSQL();
            reports_Customer_TableView.setItems(appointmentFullList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        report3_AppointmentID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        report3_Type_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        report3_Title_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        report3_Description_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        report3_Start_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        report3_End_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        report3_Location_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        report3_Contact_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        report3_UserID_Column.setCellValueFactory(new PropertyValueFactory<>("appointmentUserID"));
    }

    /**
     * Lambda Expression
     *
     * Loads contact names into the ComboBox for selection.
     * This method populates the ComboBox with contact names from the database. It uses a lambda expression
     * in the valueProperty listener of the contact ComboBox. The lambda expression offers a concise and effective
     * way to handle changes in the ComboBox's value, allowing for straightforward updating of
     * the appointment list to reflect appointments related to the selected contact.
     */
    private void loadContactComboBox() {
        ObservableList<String> contactNames = Contact.getContactNames();
        report2_SelectContact_ComboBox.setItems(contactNames);
        report2_SelectContact_ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterAppointmentsByContacts(newValue);
            }
        });
    }

    /**
     * Lambda Expression
     *
     * Loads customer names into the ComboBox for selection.
     * This method populates the ComboBox with customer names from the database. It uses a lambda expression
     * in the valueProperty listener of the customer ComboBox. The lambda expression is useful
     * here for its direct approach in handling value changes, enabling the dynamic filtering of
     * appointments based on the selected customer, updating the appointment list.
     */
    private void loadCustomerComboBox() {
        ObservableList<String> customerNames = Customer.getCustomerNames();
        report3_SelectCustomer_ComboBox.setItems(customerNames);
        report3_SelectCustomer_ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterAppointmentsByCustomer(Customer.getCustomerIDByName(newValue));
            }
        });
    }

    /**
     * Populates the report table with data.
     * Gathers and displays data about planning and debriefing sessions and counts.
     */
    private void populateTable() {
        Map<String, Integer> planningSessionCounts = Reports.getPlanningSessionCountsByMonth();
        Map<String, Integer> deBriefingSessionCounts = Reports.getDeBriefingSessionCountsByMonth();

        ObservableList<Time> reportData = FXCollections.observableArrayList();
        String[] months = Time.getMonthsArray();

        for (String month : months) {
            int planningCount = planningSessionCounts.getOrDefault(month, 0);
            int deBriefingCount = deBriefingSessionCounts.getOrDefault(month, 0);
            reportData.add(new Time(month, planningCount, deBriefingCount));
        }

        report1_Month_Column.setCellValueFactory(new PropertyValueFactory<>("month"));
        report1_Type1_Column.setCellValueFactory(new PropertyValueFactory<>("planningSessionCount"));
        report1_Type2_Column.setCellValueFactory(new PropertyValueFactory<>("deBriefingSessionCount"));
        report1_MonthlyTotal_Column.setCellValueFactory(new PropertyValueFactory<>("combinedTotal"));
        reports_MonthlyType_TableView.setItems(reportData);
    }

    /**
     * Filters the appointment list based on the selected contact.
     * Updates the table view and the total count text to reflect the filtered appointments.
     *
     * @param contactName The name of the contact to filter appointments by.
     */
    private void filterAppointmentsByContacts(String contactName) {
        try {
            ObservableList<Appointment> allAppointments = AppointmentsDAO.appointmentImportSQL();
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if (appointment.getAppointmentContact().equals(contactName)) {
                    filteredAppointments.add(appointment);
                }
            }

            report2_Total_Text.setText(String.valueOf(filteredAppointments.size()));
            reports_Contact_TableView.setItems(filteredAppointments);
        } catch (SQLException e) {
            sendAlertBox("Error", "Failed to filter appointments: " + e.getMessage());
        }
    }

    /**
     * Filters the appointment list based on the selected customer.
     * Updates the table view and the total count text to reflect the filtered appointments.
     *
     * @param customerId The ID of the customer to filter appointments by.
     */
    private void filterAppointmentsByCustomer(int customerId) {
        try {
            ObservableList<Appointment> allAppointments = AppointmentsDAO.appointmentImportSQL();
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if (appointment.getAppointmentCustomerID().equals(customerId)) {
                    filteredAppointments.add(appointment);
                }
            }
            report3_CustomerTotal_Text.setText(String.valueOf(filteredAppointments.size()));
            reports_Customer_TableView.setItems(filteredAppointments);
        } catch (SQLException e) {
            sendAlertBox("Error", "Failed to filter appointments: " + e.getMessage());
        }
    }

    /**
     * Handles the action to exit the application.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_Reports_Exit(ActionEvent event) {
        exitApplication(event);
    }

    /**
     * Handles the action to log off and return to the login screen.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_Reports_LogOff(ActionEvent event) {
        navigateNewScene(event, "/Lubomski_WGU_C195/view/LoginScreen.fxml", resourceBundle);
    }

    /**
     * Handles the action to return to the main menu.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_Reports_MainMenu(ActionEvent event) {
        navigateNewScene( event,"/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

}