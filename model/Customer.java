package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.CustomerDAO;
import Lubomski_WGU_C195.helper.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Models a customer and provides functions related to customer data management.
 */
public class Customer {

        private Integer customerID, customerDivision;
        private String customerName, customerAddress, customerPhoneNum, customerPostalCode, customerCountry;

    /**
     * Constructor for creating a Customer object.
     * Initializes a Customer with its details including ID, name, address, postal code, phone number, division, and country.
     *
     * @param customerID The ID of the customer.
     * @param customerName The name of the customer.
     * @param customerAddress The address of the customer.
     * @param customerPostalCode The postal code of the customer.
     * @param customerPhoneNum The phone number of the customer.
     * @param customerDivision The division ID of the customer.
     * @param customerCountry The country of the customer.
     */
        public Customer(Integer customerID, String customerName, String customerAddress, String customerPostalCode,
                        String customerPhoneNum, Integer customerDivision, String customerCountry) {

            this.customerID = customerID;
            this.customerName = customerName;
            this.customerAddress = customerAddress;
            this.customerPostalCode = customerPostalCode;
            this.customerPhoneNum = customerPhoneNum;
            this.customerDivision = customerDivision;
            this.customerCountry = customerCountry;
        }

        public Integer getCustomerID() {
            return customerID;
        }
        public String getCustomerName() {
            return customerName;
        }
        public String getCustomerAddress(){
            return customerAddress;
        }
        public String getCustomerPostalCode(){
            return customerPostalCode;
        }
        public String getCustomerPhoneNum(){
            return customerPhoneNum;
        }
        public Integer getCustomerDivision(){
            return customerDivision;
        }
        public String getCustomerCountry() {return customerCountry; }

    /**
     * Retrieves a list of all customer names.
     * Calls the CustomerDAO to import all customers and extracts their names into a list.
     *
     * @return An ObservableList of customer names.
     */
    public static ObservableList<String> getCustomerNames() {
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        try {
            ObservableList<Customer> customers = CustomerDAO.customerImportSQL();
            for (Customer customer : customers) {
                customerNames.add(customer.getCustomerName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerNames;
    }

    /**
     * Retrieves the ID of a customer for the customer name.
     * Searches for the customer with the name and returns their ID.
     *
     * @param customerName The name of the customer to retrieve an ID for.
     * @return The ID of the customer if found, otherwise null.
     */
    public static Integer getCustomerIDByName(String customerName) {
        try {
            ObservableList<Customer> customers = CustomerDAO.customerImportSQL();
            for (Customer customer : customers) {
                if (customer.getCustomerName().equalsIgnoreCase(customerName)) {
                    return customer.getCustomerID();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the name of a customer based on the customer ID.
     * Searches for the customer with the ID and returns their name.
     *
     * @param customerId The ID of the customer to be retrieved.
     * @return The name of the customer if found, otherwise null.
     */
    public static String getCustomerNameById(Integer customerId) {
        try {
            ObservableList<Customer> customers = CustomerDAO.customerImportSQL();
            for (Customer customer : customers) {
                if (customer.getCustomerID().equals(customerId)) {
                    return customer.getCustomerName();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Confirms and initiates deleting a customer.
     * Displays a confirmation dialog and, if confirmed, attempts to delete the customer.
     *
     * @param customer The customer to be deleted.
     * @return True if the customer was successfully deleted, false otherwise.
     */
    public static boolean confirmAndInitiateDeletion(Customer customer) {
        Optional<ButtonType> action = Utility.sendConfirmationAlert("Confirm Deletion",
                "Are you sure you want to delete " + customer.getCustomerName() + "?");

        if (action.isPresent() && action.get() == ButtonType.OK) {
            try {
                return CustomerDAO.deleteCustomer(customer.getCustomerID());
            } catch (SQLException e) {
                Utility.sendAlertBox("Error", "Failed to delete the customer: " + e.getMessage());
            }
        }
        return false;
    }
}