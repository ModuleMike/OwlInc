package Lubomski_WGU_C195.DAO;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provides database connectivity.
 * Handles opening and closing connections to the MySQL database.
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    public static Connection connection;

    /**
     * Opens a connection to the database.
     * Establishes a connection to the MySQL database using the specified URL, username, and password.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Database Connected...");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Closes the connection to the database.
     * Closes the existing database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Database Connection Closed...");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

}