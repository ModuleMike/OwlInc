package Lubomski_WGU_C195.model;

/**
 * Models a division (such as a state or province) and its associated data.
 */
public class Division {
    private Integer divisionID, divisionCountryID;
    private String divisionName;

    /**
     * Constructor for creating a Division object.
     * Initializes a Division with its ID, name, and associated country ID.
     *
     * @param divisionID The ID of the division.
     * @param divisionName The name of the division.
     * @param divisionCountryID The ID of the country that division belongs to.
     */
    public Division(Integer divisionID, String divisionName, Integer divisionCountryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.divisionCountryID = divisionCountryID;
    }

    /**
     * Gets the division ID.
     *
     * @return The ID of the division.
     */
    public Integer getDivisionID() {
        return divisionID;
    }

    /**
     * Gets the division name.
     *
     * @return The name of the division.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Gets the ID of the country for the division.
     *
     * @return The country ID from the division.
     */
    public Integer getDivisionCountryID(){
        return divisionCountryID;
    }
}