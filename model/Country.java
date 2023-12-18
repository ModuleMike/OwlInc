package Lubomski_WGU_C195.model;

import Lubomski_WGU_C195.DAO.CountryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

/**
 * Models a country and provides functions related to country data management.
 */
public class Country {
    private Integer countryID;
    private String countryName;

    /**
     * Constructor for creating a Country object.
     * Initializes a Country with its ID and name.
     *
     * @param countryID The ID of the country.
     * @param countryName The name of the country.
     */
    public Country(Integer countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * Gets the country ID.
     *
     * @return The ID of the country.
     */
    public Integer getCountryID() {
        return countryID;
    }

    /**
     * Gets the country name.
     *
     * @return The name of the country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Gets a list of all country names.
     * Calls the CountryDAO to import all countries and extracts their names into a list.
     *
     * @return An ObservableList of country names.
     */
    public static ObservableList<String> getCountryNames() {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            ObservableList<Country> countries = CountryDAO.countryImportSQL();
            for (Country country : countries) {
                countryNames.add(country.getCountryName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryNames;
    }

    /**
     * Gets the ID of a country based on the country name.
     * Searches for the country with the name and returns its ID.
     *
     * @param countryName The name of the country with the ID to be retrieved.
     * @return The ID of the country if found, otherwise null.
     */
    public static Integer getCountryIdByName(String countryName) {
        try {
            ObservableList<Country> countries = CountryDAO.countryImportSQL();
            for (Country country : countries) {
                if (country.getCountryName().equalsIgnoreCase(countryName)) {
                    return country.getCountryID();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}