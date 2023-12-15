package Lubomski_WGU_C195.controller;

import Lubomski_WGU_C195.DAO.CountryDAO;
import Lubomski_WGU_C195.DAO.CustomerDAO;
import Lubomski_WGU_C195.DAO.DivisionDAO;
import Lubomski_WGU_C195.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ResourceBundle;

import static Lubomski_WGU_C195.helper.Utility.navigateNewScene;

/**
 * Controller for the Update Customer screen of the application.
 * Manages the user interactions for updating customer information including loading, saving, and canceling customer updates.
 */
public class UpdateCustomerController implements Initializable {
    private ResourceBundle resourceBundle;

    @FXML
    private TextField updateCustomer_Name_Text, updateCustomer_Address_Text, updateCustomer_ID_Text, updateCustomer_PhoneNumber_Text, updateCustomer_PostalCode_Text;

    @FXML
    private ComboBox<String> updateCustomer_ProvinceState_ComboBox, updateCustomer_Country_ComboBox;


    /**
     * Initializes the controller class.
     * This method is called after all FXML annotated fields are populated. It initializes
     * the controller class.
     *
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        resourceBundle = resources;
    }

    /**
     * Lambda Expression
     *
     * Loads country and division names into ComboBoxes.
     * This method populates the country ComboBox with country names from the database. It uses a lambda expression
     * in the valueProperty listener of the country ComboBox. The lambda expression is used for its effective handling
     * of changes in the ComboBox's value, enabling dynamic loading of divisions into the division ComboBox based on the
     * selected country. This approach allows for direct updating of the division data.
     */
    private void loadComboBoxes() {
        updateCustomer_Country_ComboBox.setItems(CountryDAO.getAllCountryNames());
        updateCustomer_Country_ComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateCustomer_ProvinceState_ComboBox.setItems(DivisionDAO.getDivisionNamesByCountryId(CountryDAO.getCountryID(newValue)));
        });
    }

    /**
     * Loads the selected customer's data into the form fields for editing.
     * Loads the form fields with the data of the customer selected for update.
     *
     * @param customer The customer object with data to be loaded into the form.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    public void loadCustomerData(Customer customer) throws SQLException {
        loadComboBoxes();
        updateCustomer_ID_Text.setText(customer.getCustomerID().toString());
        updateCustomer_Name_Text.setText(customer.getCustomerName());
        updateCustomer_PhoneNumber_Text.setText(customer.getCustomerPhoneNum());
        updateCustomer_Address_Text.setText(customer.getCustomerAddress());
        updateCustomer_PostalCode_Text.setText(customer.getCustomerPostalCode());
        updateCustomer_Country_ComboBox.getSelectionModel().select(CountryDAO.countryName(customer.getCustomerCountry()));
        updateCustomer_ProvinceState_ComboBox.getSelectionModel().select(DivisionDAO.getDivisionName(customer.getCustomerDivision()));
        }

    /**
     * Saves the updated customer data.
     * Validates the input fields and updates the customer data in the database.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    void onAction_UpdateCustomer_Save(ActionEvent event) throws SQLException {
        if (updateCustomer_Name_Text.getText().isEmpty() || updateCustomer_ID_Text.getText().isEmpty()) {
            return;
        }
        Integer id = Integer.parseInt(updateCustomer_ID_Text.getText());
        String name = updateCustomer_Name_Text.getText();
        String address = updateCustomer_Address_Text.getText();
        String postalCode = updateCustomer_PostalCode_Text.getText();
        String phoneNumber = updateCustomer_PhoneNumber_Text.getText();
        Timestamp lastUpdate = Timestamp.from(Instant.now());
        String lastUpdatedBy = User.getCurrentLoggedInUserName();
        Integer divisionID = DivisionDAO.getDivisionIdByName(updateCustomer_ProvinceState_ComboBox.getValue());
        CustomerDAO.updateCustomer(id, name, address, postalCode, phoneNumber, lastUpdate, lastUpdatedBy, divisionID);
        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

    /**
     * Cancels the update action and navigates back to the main menu.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_UpdateCustomer_Cancel(ActionEvent event) {
        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }
}