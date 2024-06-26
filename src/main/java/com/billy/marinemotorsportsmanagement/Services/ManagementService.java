package com.billy.marinemotorsportsmanagement.Services;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

/**
 * The Management Class handles Admin Login/Session Verification as well as acts
 * as the main Object Class which others extend off.
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
 */
public class ManagementService {
    // Load .env from documents folder of current user
    Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\" + System.getProperty("user.name") + "\\Documents").load();
    
    // Declare variables

    /**
     * SQL Database URL
     */
    public final String databaseURL = dotenv.get("DB_URL"); // db dir
    private boolean activeSession;
    private final String username;
    private final String password;

    // No-arg Constructor
    public ManagementService() {
        activeSession = false;
        // default admin login
        username = dotenv.get("ADMIN_USERNAME");
        password = dotenv.get("ADMIN_PASSWORD");
    }
    
    /**
     * The databaseConnection method verifies that the database
     * is accessible
     * 
     * @return true if accessible, otherwise false
     */
    public boolean databaseConnection() {
        // Initialize boolean
        boolean status = false;
        
        // Test Database connection
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            status = true;
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        // Return the Database Connection Status
        return status;
    }

    /**
     * The login method validates admin user login
     *
     * @param username the username entered
     * @param password the password entered
     * 
     * @return true if successful, false if unsuccessful attempt
     */
    public boolean login(String username, String password) {
        // Validate login attempt
        if (this.username.equals(username) && this.password.equals(password)) {
            activeSession = true;
            return true; // successful login attempt
        } else {
            return false; // unsuccessful login attempt
        }
    }

    /**
     * The logout method attempts to logout the admin user
     *
     * @return true if successful attempt, false if unsuccessful attempt
     */
    public boolean logout() {
        // check if session is active
        if (activeSession) {
            activeSession = false;
            return true; // successful logout attempt
        } else {
            return false; // unsuccessful logout attempt
        }
    }

    /**
     * The sessionStatus method returns the current status of the admin user
     *
     * @return true if active session, false if inactive session
     */
    public boolean sessionStatus() {
        return activeSession;
    }

    /**
     * The exit method exits the application
     */
    public void exit() {
        System.exit(0);
    }
}