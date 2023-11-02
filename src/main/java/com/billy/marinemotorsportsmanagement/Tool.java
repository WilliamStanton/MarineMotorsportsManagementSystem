package com.billy.marinemotorsportsmanagement;

import java.sql.*;
import java.util.ArrayList;

/**
 * The Tool Class allows for various methods of tool manipulation and
 * creation/removal
 *
 * @author Billy Stanton
 * @version 1.0
 * @date 11/1/23
 */
public class Tool extends Student {

    /**
     * The toolList method returns a list of active/inactive tools
     *
     * @param status true if active, false if inactive
     * @return the list of active/inactive tools
     */
    public ArrayList<String> toolList(boolean status) {
        // declare variables
        ArrayList<String> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[Tool Name] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // check if any results found
            while (result.next()) {
                tools.add(result.getString("Tool Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return tools;
    }

    /**
     * The toolList method returns a list of active/inactive tools
     *
     * @param status true if active, false if inactive
     * @param availability true if tool available, false if tool unavailable
     * @return the list of active/inactive tools
     */
    public ArrayList<String> toolList(boolean status, boolean availability) {
        // declare variables
        ArrayList<String> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status, Tool.ID, Tool.[Tool Name] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // get results
            while (result.next()) {
                // available tools
                if (availability) {
                    if (toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getString("Tool Name"));
                    }
                } // unavailable tools
                else {
                    if (!toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getString("Tool Name"));
                    }
                }
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return existence
        return tools;
    }

    /**
     * The toolStatus method checks if a tool is inactive or active
     *
     * @param toolID the tool id to check
     * @return true if active, false if inactive/not found
     */
    public boolean toolStatus(int toolID) {
        // declare variables
        boolean status = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status FROM Tool WHERE (((Tool.ID)=" + toolID + "));"); // Get results for SQL Statement

            // get status
            if (result.next()) {
                status = result.getBoolean("Status");
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return status
        return status;
    }

    /**
     * The toolAvailability method checks if a specified tool is available or
     * not
     *
     * @param toolID the tool to check availability
     * @return the tool availability
     */
    public boolean toolAvailability(int toolID) {
        // declare variables
        boolean availability = false;
        ArrayList<Boolean> avail = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Borrow.Returned FROM Borrow WHERE (((Borrow.[Tool ID])=" + toolID + "));"); // Get results for SQL Statement

            // add all results
            while (result.next()) {
                avail.add(result.getBoolean("Returned"));
            }
            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // check last result for answer, if no results, then available
        if (avail.size() <= 0) {
            availability = true;
        } else {
            availability = avail.get(avail.size() - 1);
        }

        // return availability
        return availability;
    }

    /**
     * The borrowTool method allows borrowing of a tool that is currently
     * available
     *
     * @param studentID the student id borrowing the tool
     * @param toolID the tool id of the tool being borrowed
     * @return true if successfully borrowed, else false
     */
    public boolean borrowTool(int studentID, int toolID) {
        if (toolAvailability(toolID) && studentStatus(studentID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Borrow ( [Student ID], [Tool ID] ) VALUES (\"" + studentID + "\",\"" + toolID + "\");"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            // successful borrow
            return true;
        }
        // unsuccessful borrow
        return false;
    }

    /**
     * The returnTool method allows returning a tool that is currently borrowed
     *
     * @param toolID the tool id of the tool being returned
     * @return true if successfully returned, else false
     */
    public boolean returnTool(int toolID) {
        if (!toolAvailability(toolID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Borrow SET Borrow.[Returned] = True WHERE Borrow.[Tool ID] = " + toolID + ";"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }

            // successful return
            return true;
        }

        // unsuccessful return
        return false;
    }

    /**
     * The createTool method adds a tool
     *
     * @param toolName the name of the tool to add
     * @return true if successfully added, else false
     */
    public boolean createTool(String toolName) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tool ( [Tool Name] ) VALUES (\"" + toolName + "\");"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            connection.close(); // Close DB connection
            return true; // succesful attempt

        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }
        return false; // unsuccessful attempt
    }

    /**
     * The removeTool method removes a tool
     *
     * @param toolID the tool ID to remove
     * @return true if successfully removed, else false
     */
    public boolean removeTool(int toolID) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.[Status] = False WHERE Tool.[ID] = " + toolID + ";"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            connection.close(); // Close DB connection

            // successful removal
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // unsuccessful removal
        return false;
    }
}
