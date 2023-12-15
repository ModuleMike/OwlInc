package Lubomski_WGU_C195.application;

import Lubomski_WGU_C195.DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main class for the Lubomski WGU Appointment Scheduling Application.
 * This class launches the application and sets up the initial stage.
 */
public class Main extends Application {

    /**
     * Initializes the application.
     * This method is called before the application's UI is loaded.
     */
   @Override
   public void init(){
   }

    /**
     * Starts the application.
     * This method sets up the primary stage of the application with the login screen.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if loading the FXML file fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lubomski_WGU_C195/view/LoginScreen.fxml"));
        loader.setResources(ResourceBundle.getBundle("Lubomski_WGU_C195.Localization.Local", Locale.getDefault()));

        Parent root = loader.load();
        primaryStage.setTitle("Lubomski WGU Appointment Scheduling Application - Login Screen");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    /**
     * The method opens a database connection and launches the application.
     *
     * @param args the command line arguments.
     * @throws SQLException if a database access error occurs.
     */
    public static void main(String[] args) throws SQLException {

            JDBC.openConnection();
            launch(args);
    }
}