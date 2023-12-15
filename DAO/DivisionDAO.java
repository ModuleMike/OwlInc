package Lubomski_WGU_C195.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Lubomski_WGU_C195.helper.Utility.sendAlertBox;

/**
 * Data Access Object for handling first-level division related database operations.
 * Provides methods to retrieve division names, IDs, and country IDs.
 */
public class DivisionDAO {

    /**
     * Retrieves the country ID of a division based on the division ID.
     * Queries the database for the country's ID given the division ID.
     *
     * @param divisionID The ID of the division to retrieve the country ID for.
     * @return The country ID if found, otherwise null.
     */
    public static Integer divisionCountryID(Integer divisionID) {
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Country_ID");
            }
        } catch (SQLException ePrompt) {
            sendAlertBox("Error Import City Data", "Unable to import City SQL Data");
        }
        return null;
    }

    /**
     * Retrieves division names for a specific country ID.
     * Queries the database for all divisions associated with a country ID.
     *
     * @param countryId The ID of the country to retrieve divisions for.
     * @return An ObservableList of strings representing division names.
     */
    public static ObservableList<String> getDivisionNamesByCountryId(Integer countryId) {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                divisionNames.add(rs.getString("Division"));
            }
        } catch (SQLException e) {
            sendAlertBox("Error", "Unable to load divisions for the selected country.");
        }
        return divisionNames;
    }

    /**
     * Retrieves the division ID based on the division name.
     * Queries the database for the division's ID corresponding to the given division name.
     *
     * @param divisionName The name of the division to retrieve the ID for.
     * @return The division ID if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public static Integer getDivisionIdByName(String divisionName) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, divisionName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Division_ID");
            }
        } catch (SQLException e) {
            sendAlertBox("Error", "Unable to fetch division ID for the given name.");
        }
        return null;
    }

    /**
     * Retrieves the division name based on the division ID.
     * Queries the database for the division's name corresponding to the given division ID.
     *
     * @param divisionID The ID of the division to retrieve a name for.
     * @return The division name if found, otherwise null.
     */
    public static String getDivisionName(Integer divisionID) {
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Division");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}