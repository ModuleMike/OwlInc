package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.ContactsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

/**
 * Models a contact and provides functions related to contact data management.
 */
public class Contact {

    private Integer contactID;
    private String contactName, contactEmail;

    /**
     * Constructor for creating a Contact object.
     * Initializes a Contact with its ID, name, and email.
     *
     * @param contactID The ID of the contact.
     * @param contactName The name of the contact.
     * @param contactEmail The email address of the contact.
     */
    public Contact(Integer contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        }

    /**
     * Gets the contact ID.
     *
     * @return The ID of the contact.
     */
    public Integer getContactID() {
        return contactID;
    }

    /**
     * Gets the contact name.
     *
     * @return The name of the contact.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Gets the contact email.
     *
     * @return The email address of the contact.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Retrieves a list of all contact names.
     * Calls the ContactsDAO to import all contacts and extracts their names into a list.
     *
     * @return An ObservableList of contact names.
     */
    public static ObservableList<String> getContactNames() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try {
            ObservableList<Contact> contacts = ContactsDAO.contactImportSQL();
            for (Contact contact : contacts) {
                contactNames.add(contact.getContactName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactNames;
    }

    /**
     * Retrieves the ID of a contact based on the contact name.
     * Searches for the contact with the name and returns their ID.
     *
     * @param customerName The name of the contact to retrieve the ID for.
     * @return The ID of the contact if found, otherwise null.
     */
    public static Integer getContactIDByName(String customerName) {
        try {
            ObservableList<Contact> contacts = ContactsDAO.contactImportSQL();
            for (Contact contact : contacts) {
                if (contact.getContactName().equalsIgnoreCase(customerName)) {
                    return contact.getContactID();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}