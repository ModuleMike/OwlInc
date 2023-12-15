package Lubomski_WGU_C195.DAO;

import Lubomski_WGU_C195.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;
import static Lubomski_WGU_C195.DAO.DivisionDAO.*;
import static Lubomski_WGU_C195.DAO.CountryDAO.*;

/**
 * Data Access Object for handling customer related database operations.
 * Provides methods to import, add, update, and delete customers, and to check for customer appointments.
 */
public class CustomerDAO {

    /**
     * Imports all customers from the database.
     * Retrieves all customer records from the database and converts them into Customer objects.
     *
     * @return An ObservableList of Customer objects.
     * @throws SQLException If a database access error occurs.
     */
    public static ObservableList<Customer> customerImportSQL() throws SQLException {
        ObservableList<Customer> customerArrayList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getInt("Division_ID"),
                        countryName(divisionCountryID(rs.getInt("Division_ID")))
                );
                customerArrayList.add(customer);
            }
        } catch (SQLException e) {
            sendAlertBox("Error Import Customer Data", "Unable to import Customer SQL Data");
        }
        return customerArrayList;
    }

    /**
     * Adds a new customer to the database.
     * Inserts a new customer record into the database using the provided details.
     *
     * @param name, address, postalCode, phoneNumber, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID Customer details for the new record.
     * @throws SQLException If a database access error occurs.
     */
    public static void addCustomer(String name, String address, String postalCode, String phoneNumber,Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, Integer divisionID) {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setTimestamp(5, createDate);
            ps.setString(6, createdBy);
            ps.setTimestamp(7, lastUpdate);
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, divisionID);

            ps.executeUpdate();
        } catch (SQLException e) {
            sendAlertBox("Error Adding Appointment", "Unable to add appointment: " + e.getMessage());
        }
    }


    /**
     * Updates an existing customer in the database.
     * Updates the details of an existing customer record using the customer ID.
     *
     * @param id, name, address, postalCode, phoneNumber, lastUpdate, lastUpdatedBy, divisionID Updated details for the customer record.
     * @throws SQLException If a database access error occurs.
     */
    public static void updateCustomer(int id, String name, String address, String postalCode, String phoneNumber, Timestamp lastUpdate , String lastUpdatedBy, Integer divisionID) {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setTimestamp(5, lastUpdate);
            ps.setString(6, lastUpdatedBy);
            ps.setInt(7, divisionID); // Set last updated time to now
            ps.setInt(8, id);


            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a customer from the database.
     * Removes the customer record by using the customer ID from the database.
     *
     * @param customerID The ID of the customer to delete.
     * @return A boolean indicating whether the deletion was successful.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean deleteCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a customer has any appointments.
     * Queries the database to determine if the customer, using the customer ID has any appointments.
     *
     * @param customerId The ID of the customer to check for appointments.
     * @return A boolean indicating whether the customer has appointments.
     */
    public static boolean customerHasAppointments(int customerId) {
        String sql = "SELECT COUNT(*) AS appointment_count FROM appointments WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("appointment_count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
