package Lubomski_WGU_C195.DAO;

import Lubomski_WGU_C195.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;

/**
 * Data Access Object for handling country related database operations.
 * Provides methods to import country data, retrieve country names and IDs.
 */
public class CountryDAO {

    /**
     * Imports all countries from the database.
     * Retrieves all country records from the database and converts them into Country objects.
     *
     * @return An ObservableList of Country objects.
     * @throws SQLException If a database access error occurs.
     */
    public static ObservableList<Country> countryImportSQL() throws SQLException {
        ObservableList<Country> countryArrayList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM countries";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Country country = new Country(
                        rs.getInt("Country_ID"),
                        rs.getString("Country")
                );
                countryArrayList.add(country);
            }
        } catch (SQLException e) {
            sendAlertBox("Error Import Customer Data", "Unable to import Customer SQL Data");
        }
        return countryArrayList;
    }

    /**
     * Retrieves the name of a country based on the country ID.
     * Queries the database for the country's name given the country ID.
     *
     * @param countryID The ID of the country to be retrieved.
     * @return The name of the country if found, otherwise null.
     */
    public static String countryName(Integer countryID) {
        try {
            String sql = "SELECT * FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Country");
            }
        } catch (SQLException ePrompt) {
            sendAlertBox("Error Import Country Data", "Unable to import Country SQL Data");
        }
        return null;
    }

    /**
     * Retrieves the name of a country based on the country name.
     * Queries the database for the country's name corresponding to the given country name.
     *
     * @param countryName The name of the country to be retrieved.
     * @return The name of the country if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public static String countryName(String countryName) {

        try {
            String sql = "SELECT * FROM countries WHERE Country = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, countryName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Country");
            }
        }catch (SQLException ePrompt){
            sendAlertBox("Error Import Contact Data", "Unable to import Contact SQL Data");
        }
        return null;
    }

    /**
     * Retrieves the ID of a country based on the country name.
     * Queries the database for the country's ID for the given country name.
     *
     * @param countryName The name of the country to retrieve the ID.
     * @return The ID of the country if found, otherwise null.
     */
    public static Integer getCountryID(String countryName) {
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, countryName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Country_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all country names from the database.
     * Queries the database for all country names and returns them in a list.
     *
     * @return An ObservableList of strings representing the country names.
     */
    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        String sql = "SELECT Country FROM countries";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countryNames.add(rs.getString("Country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryNames;
    }

}