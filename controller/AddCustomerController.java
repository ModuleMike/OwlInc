package Lubomski_WGU_C195.controller;

import Lubomski_WGU_C195.DAO.DivisionDAO;
import Lubomski_WGU_C195.model.*;
import javafx.collections.ObservableList;
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
import static Lubomski_WGU_C195.DAO.CustomerDAO.*;

/**
 * Controller class for adding customers in the application.
 * This class manages user interactions for adding a new customer.
 */
public class AddCustomerController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private TextField addCustomer_Address_Text, addCustomer_PostalCode_Text, addCustomer_PhoneNumber_Text,
            addCustomer_Name_Text;

    @FXML
    private ComboBox<String> addCustomer_ProvinceState_Text, addCustomer_Country_Text;

    /**
     * Initializes the controller class.
     * This method is called after the FXML file has been loaded. It sets the resource bundle
     * and initializes the ComboBoxes with relevant data.
     *
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {

        this.resourceBundle = resources;
        loadComboBoxes();
    }

    /**
     * Lambda Expression
     *
     * Loads data into the ComboBoxes for country and state/province selections.
     * This method populates the ComboBoxes with data from the database. It uses a lambda expression
     * in the valueProperty listener of the country ComboBox. The use of a lambda expression here is
     * beneficial for handling events like value changes, allowing for a clear and direct implementation of
     * the required action (in this case, updating the state/province ComboBox based on the selected country).
     */
    private void loadComboBoxes(){
        ObservableList<String> countryNames = Country.getCountryNames();
        addCustomer_Country_Text.setItems(countryNames);
        addCustomer_Country_Text.valueProperty().addListener((observable, oldValue, newValue) -> {
            addCustomer_ProvinceState_Text.setItems(DivisionDAO.getDivisionNamesByCountryId(Country.getCountryIdByName(newValue)));
        });
    }

    /**
     * Handles the action of saving a new customer.
     * This method is triggered when the save button is clicked. It collects input data from the form,
     * validates and saves the new customer, then navigates back to the main menu.
     *
     * @param event The event(button) that triggered this action.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    void onAction_AddCustomer_Save(ActionEvent event) throws SQLException {

        String name = addCustomer_Name_Text.getText();
        String phoneNumber = addCustomer_PhoneNumber_Text.getText();
        String address = addCustomer_Address_Text.getText();
        String postalCode = addCustomer_PostalCode_Text.getText();
        String country = addCustomer_Country_Text.getValue();
        Integer division = DivisionDAO.getDivisionIdByName(addCustomer_ProvinceState_Text.getValue());
        Timestamp createDate = Timestamp.from(Instant.now());
        String createdBy = User.getCurrentLoggedInUserName();
        Timestamp lastUpdated = Timestamp.from(Instant.now());
        String lastUpdatedBy = User.getCurrentLoggedInUserName();
        addCustomer(name, address, postalCode, phoneNumber, createDate, createdBy, lastUpdated, lastUpdatedBy, division);

        navigateNewScene(event, "/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

    /**
     * Handles the action of canceling adding a customer.
     * This method is triggered when the cancel button is clicked. It navigates back to the main menu.
     *
     * @param event The event(button) that triggered this action.
     */
    @FXML
    void onAction_AddCustomer_Cancel(ActionEvent event) {
        navigateNewScene( event,"/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
    }

}