package Lubomski_WGU_C195.DAO;

import Lubomski_WGU_C195.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;

/**
 * Data Access Object for handling contact related database operations.
 * Provides methods to retrieve contact information and import contact data.
 */
public class ContactsDAO {

    /**
     * Imports all contacts from the database.
     * Retrieves all contact records from the database and converts them into Contact objects.
     *
     * @return An ObservableList of Contact objects.
     * @throws SQLException If a database access error occurs.
     */
    public static ObservableList<Contact> contactImportSQL() throws SQLException {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email")
                );
                contactsList.add(contact);
            }
        } catch (SQLException e) {
            sendAlertBox("Error Import Contact Data", "Unable to import Contact SQL Data");
        }
        return contactsList;
    }

    /**
     * Retrieves the name of a contact based on the contact ID.
     * Queries the database for the contact's name for the given contact ID.
     *
     * @param contactID The ID of the contact to be retrieved.
     * @return The name of the contact if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public static String contactName(Integer contactID) throws SQLException{

        try {
            String sql = "SELECT * FROM CONTACTS WHERE CONTACT_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Contact_Name");
            }
        }catch (SQLException ePrompt){
            sendAlertBox("Error Import Contact Data", "Unable to import Contact SQL Data");
        }
        return null;
    }

}