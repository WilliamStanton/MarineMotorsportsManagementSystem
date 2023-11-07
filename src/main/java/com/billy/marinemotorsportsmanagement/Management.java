package com.billy.marinemotorsportsmanagement;

import java.sql.*;

/**
 * The Management Class handles Admin Login/Session Verification as well as acts
 * as the main Object Class which others extend off.
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
 */
public class Management {

    // Declare variables
    public static String databaseURL = "jdbc:ucanaccess://C://Users//Moofie//Documents//MMMS.accdb"; // db dir
//    public static String databaseURL = "jdbc:ucanaccess://C://Users//Hello//Documents//MMMS//MMMS.accdb"; // db dir
    private boolean activeSession;
    private final String username;
    private final String password;

    // No-arg Constructor
    public Management() {
        activeSession = false;
        // default admin login
        username = "admin";
        password = "password";
    }

    /**
     * The login method validates admin user login attempts
     *
     * @param username the username entered
     * @param password the password entered
     * @return true if successful, false if unsuccessful attempt
     */
    public boolean login(String username, String password) {
        // validate login attempt
        if (this.username.equals(username) && this.password.equals(password)) {
            activeSession = true;
            return true; // successful attempt
        } else {
            return false; // unsuccessful attempt
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
            return true; // successful attempt
        } else {
            return false; // unsuccessful attempt
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
