package Lubomski_WGU_C195.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import Lubomski_WGU_C195.model.Appointment;
import static Lubomski_WGU_C195.helper.Utility.*;
import static Lubomski_WGU_C195.model.User.validateUser;

/**
 * Controller for the login screen.
 * Initializes and handles user interactions on the login screen.
 */
public class LoginScreenController implements Initializable {

    private ResourceBundle resourceBundle;

    @FXML
    private Button login_ExitButton, login_SubmitButton;

    @FXML
    private Label login_headerText, login_SystemLocationText, login_LocationLabel,
            login_PasswordLabel, login_UserNameLabel, login_SystemLanguageText, login_LanguageLabel;

    @FXML
    private TextField login_UserNameText, login_PasswordText;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. Sets up the UI components based on the
     * system's locale.
     *
     * @param location The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        this.resourceBundle = resources;
        if (this.resourceBundle == null) {
            sendAlertBox(resourceBundle.getString("invalidFilePathTitle"),
                    resourceBundle.getString("invalidFilePathMessage"));
            return;
        }

        Locale currentLocale = Locale.getDefault();
        String currentCountry = currentLocale.getDisplayCountry();
        String currentLanguage = currentLocale.getDisplayLanguage();
        login_SystemLocationText.setText(currentCountry);
        login_SystemLanguageText.setText(currentLanguage);


        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            login_headerText.setText(this.resourceBundle.getString("login_header"));
            login_UserNameLabel.setText(this.resourceBundle.getString("username"));
            login_UserNameText.setText(this.resourceBundle.getString("username"));
            login_PasswordLabel.setText(this.resourceBundle.getString("password"));
            login_PasswordText.setText(this.resourceBundle.getString("password"));
            login_LanguageLabel.setText(this.resourceBundle.getString("language"));
            login_LocationLabel.setText(this.resourceBundle.getString("location"));
            login_ExitButton.setText(this.resourceBundle.getString("exit"));
            login_SubmitButton.setText(this.resourceBundle.getString("submit"));
        }
    }

    /**
     * Handles the action to submit the login form.
     * Validates user credentials and navigates to the main menu if successful.
     *
     * @param event The ActionEvent triggered by clicking the submit button.
     */
    @FXML
    public void onActionSubmitLogin(ActionEvent event) {
        boolean validatedSubmit = validateUser(login_UserNameText.getText(), login_PasswordText.getText());

        loginActivityLogger(login_UserNameText.getText(), validatedSubmit);
        if (validatedSubmit) {
            Appointment.checkForUpcomingAppointments();
            navigateNewScene( event,"/Lubomski_WGU_C195/view/MainMenu.fxml", resourceBundle);
        }
        else {
            sendAlertBox(resourceBundle.getString("loginFailedTitle"), resourceBundle.getString("loginFailedMessage"));
        }
    }

    /**
     * Handles the action to exit the application.
     * Closes the application when the exit button is clicked.
     *
     * @param event The ActionEvent triggered by clicking the exit button.
     */
    @FXML
    public void onActionExitLogin(ActionEvent event) {

        exitApplication(event);
    }
}