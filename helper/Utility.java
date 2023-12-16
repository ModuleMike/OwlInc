package Lubomski_WGU_C195.helper;

import Lubomski_WGU_C195.DAO.JDBC;
import Lubomski_WGU_C195.controller.UpdateAppointmentController;
import Lubomski_WGU_C195.controller.UpdateCustomerController;
import Lubomski_WGU_C195.model.Appointment;
import Lubomski_WGU_C195.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Provides utility methods for logging, alerts, and navigation.
 */
public class Utility {

    /**
     * Logs login activity to a file.
     * Writes the login attempt details including username, time, and status to a log file.
     *
     * @param username     The username used for login.
     * @param loginStatus  The status of the login attempt (true if successful, false otherwise).
     */
    public static void loginActivityLogger(String username, boolean loginStatus) {
        String activityLogFilePath = "./src/Lubomski_WGU_C195/login_activity.txt";
        String activityStatus;

        if (loginStatus){
            activityStatus = "Successful";
        }
        else {
            activityStatus = "Failed";
        }
        try (FileWriter writeLoginActivity = new FileWriter(activityLogFilePath, true);
             PrintWriter printLoginActivity = new PrintWriter(writeLoginActivity)){
             String timeOfActivity = Time.timeFormattedUTC(Instant.now());
             printLoginActivity.println("Login Attempt On : " + timeOfActivity + " UTC" + " | Username: - " + username + " | Login Status - " + activityStatus);
        }
        catch (IOException errorPrompt){
            sendAlertBox("Invalid File Path", "Invalid File Path for Activity Log");
        }
    }

    /**
     * Displays an alert box with a warning message.
     *
     * @param alertTitle   The title of the alert box.
     * @param alertMessage The message to be displayed in the alert box.
     */
    public static void sendAlertBox(String alertTitle, String alertMessage){
        Alert alertMethod = new Alert(Alert.AlertType.WARNING);
        alertMethod.setTitle(alertTitle);
        alertMethod.setHeaderText(null);
        alertMethod.setContentText(alertMessage);
        alertMethod.showAndWait();
    }

    /**
     * Displays a confirmation alert and returns the user's choice.
     *
     * @param title   The title of the confirmation alert.
     * @param message The message to be displayed in the confirmation alert.
     * @return An Optional ButtonType chosen by the user.
     */
    public static Optional<ButtonType> sendConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    /**
     * Navigates to a new scene.
     * Loads and displays the FXML view from the file path.
     *
     * @param event          The action event that triggers the navigation.
     * @param fxmlFilePath   The file path of the FXML view to load.
     * @param resourceBundle The resource bundle to use for localization.
     */
    public static void navigateNewScene(ActionEvent event, String fxmlFilePath, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loadXMLFile = new FXMLLoader(Utility.class.getResource(fxmlFilePath));
                loadXMLFile.setResources(resourceBundle);
                Parent sceneRoot = loadXMLFile.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(sceneRoot);
                stage.setScene(scene);
                stage.show();
        } catch (IOException ePrompt) {
            sendAlertBox(resourceBundle.getString("invalidFilePathTitle"), resourceBundle.getString("invalidFilePathMessage"));
        }
    }

    /**
     * Closes the application.
     * Closes the database connection and exits the application.
     *
     * @param event The action event that triggers the exit.
     */
    public static void exitApplication(ActionEvent event){
        JDBC.closeConnection();
        System.exit(0);
    }

    /**
     * Navigates to the update appointment scene.
     * Loads and displays the update appointment view with the selected appointment's data.
     *
     * @param event          The action event that triggers the navigation.
     * @param fxmlPath       The file path of the FXML view to load.
     * @param appointment    The appointment to be updated.
     * @param resourceBundle The resource bundle to use for localization.
     */
    public static void navigateToUpdateScene(ActionEvent event, String fxmlPath, Appointment appointment, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(Utility.class.getResource(fxmlPath), resourceBundle);
            Parent root = loader.load();

            UpdateAppointmentController updateAppointmentController = loader.getController();
            updateAppointmentController.loadAppointmentData(appointment);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }

    /**
     * Navigates to the update customer scene.
     * Loads and displays the update customer view with the selected customer's data.
     *
     * @param event          The action event that triggers the navigation.
     * @param fxmlPath       The file path of the FXML view to load.
     * @param customer       The customer to be updated.
     * @param resourceBundle The resource bundle to use for localization.
     */
    public static void navigateToCustomerUpdateScene(ActionEvent event, String fxmlPath, Customer customer, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(Utility.class.getResource(fxmlPath), resourceBundle);
            Parent root = loader.load();

            UpdateCustomerController updateCustomerController = loader.getController();
            updateCustomerController.loadCustomerData(customer);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
        }
    }
}