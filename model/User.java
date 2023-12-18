package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

/**
 * Models a user and handles user-related functions.
 * This class includes methods for user data retrieval and user authentication.
 */
public class User {
    private Integer userID;
    private String userName, userPassword;
    private static String currentLoggedInUserName;

    /**
     * Constructor for User object.
     *
     * @param userID       The user's ID.
     * @param userName     The user's name.
     * @param userPassword The user's password.
     */
    public User(Integer userID, String userName, String userPassword){
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * Gets the user's ID.
     *
     * @return The ID of the user.
     */
    public Integer getUserID(){
        return userID;
    }

    /**
     * Gets the user's name.
     *
     * @return The name of the user.
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Gets the user's password.
     *
     * @return The password of the user.
     */
    public String getUserPassword(){
        return userPassword;
    }

    /**
     * Retrieves a list of all user IDs.
     * Calls the UserDAO to import all users and extracts their IDs into a list.
     *
     * @return An ObservableList of user IDs as strings.
     */
    public static ObservableList<String> getUserIDs() {
        ObservableList<String> userIds = FXCollections.observableArrayList();
        try {
            ObservableList<User> users = UserDAO.userImportSQL();
            for (User user : users) {
                userIds.add(user.getUserID().toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    /**
     * Sets the current logged-in user's name.
     * Stores the username of the currently logged-in user.
     *
     * @param userName The name of the user currently logged in.
     */
    public static void setCurrentLoggedInUserName(String userName) {
        User.currentLoggedInUserName = userName;
    }

    /**
     * Gets the current logged-in user's name.
     *
     * @return The name of the user currently logged in.
     */
    public static String getCurrentLoggedInUserName() {
        return User.currentLoggedInUserName;
    }

    /**
     * Validates user credentials.
     * Checks if the provided username and password match an existing user in the database.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the credentials are valid, false otherwise.
     */
    public static boolean validateUser(String username, String password) {
        try {
            ObservableList<User> users = UserDAO.userImportSQL();
            for (User user : users) {
                if (user.getUserName().equals(username) && user.getUserPassword().equals(password)) {
                    User.setCurrentLoggedInUserName(user.getUserName()); // Set the current logged-in userName
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}