package Lubomski_WGU_C195.DAO;

import Lubomski_WGU_C195.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for handling user-related database operations.
 * Provides methods to import user data.
 */
public class UserDAO {

    /**
     * Imports all users from the database.
     * Retrieves all user records from the database and converts them into User objects.
     *
     * @return An ObservableList of User objects.
     * @throws SQLException If a database access error occurs.
     */
    public static ObservableList<User> userImportSQL() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}