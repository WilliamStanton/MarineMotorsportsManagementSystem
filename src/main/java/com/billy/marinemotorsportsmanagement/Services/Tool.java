package com.billy.marinemotorsportsmanagement.Services;

import com.billy.marinemotorsportsmanagement.Services.Student;

import java.sql.*;
import java.util.ArrayList;

/**
 * The Tool Class allows for various methods of tool manipulation and
 * creation/removal
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
 */
public class Tool extends Student {

    /**
     * The getToolName method returns the name of a tool by id
     *
     * @param toolID the id of the tool
     *
     * @return the name of the tool, otherwise null if not found
     */
    public String getToolName(int toolID) {
        // Initialize variables
        String toolName = null;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[Tool Name] FROM Tool WHERE (((Tool.ID)=" + toolID + "));"); // Get results for SQL Statement

            // Get Tool Name Result
            if (result.next()) {
                toolName = result.getString("Tool Name");
            }

            connection.close(); // Close DB connection

            // Return Tool Name
            return toolName;
        } catch (SQLException ex) {
            // If cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return tool not found
        return null;
    }

    /**
     * The getToolBorrowerID method returns the id of the person borrowing a
     * tool currently
     *
     * @param toolID the tool id to get the borrower name for
     *
     * @return the name of the borrower, otherwise empty if the tool isn't being
     * borrowed
     */
    public ArrayList<Integer> getToolBorrowerIDS(int toolID) {
        // Initialize Variables
        ArrayList<Integer> borrowers = new ArrayList<>();
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Borrow.[Student ID] FROM Borrow WHERE (((Borrow.[Tool ID])=" + toolID + ") AND ((Borrow.Returned)=No));"); // Get results for SQL Statement

            // Add all results
            while (result.next()) {
                borrowers.add(result.getInt("Student ID"));
            }
            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return borrower, otherwise null if not found
        return borrowers;
    }

    /**
     * The toolStatus method checks if a tool is enabled or disabled
     *
     * @param toolID the tool id to check
     *
     * @return true if enabled, false if disabled/not found
     */
    public boolean toolStatus(int toolID) {
        // Initialize Variables
        boolean status = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status FROM Tool WHERE (((Tool.ID)=" + toolID + "));"); // Get results for SQL Statement

            // Get Tool Status
            if (result.next()) {
                status = result.getBoolean("Status");
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Tool Status
        return status;
    }

    /**
     * The toolAvailability method checks if a specified tool is available or
     * not
     *
     * @param toolID the tool to check availability
     *
     * @return the tool availability
     */
    public boolean toolAvailability(int toolID) {
        // Initialize Variables
        boolean availability = false;
        ArrayList<Boolean> unavail = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Borrow.Returned FROM Borrow WHERE (((Borrow.[Tool ID])=" + toolID + " ) AND ((Borrow.Returned)=No));"); // Get results for SQL Statement

            // Add all results for specified tool to ArrayList
            while (result.next()) {
                unavail.add(result.getBoolean("Returned"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }


        // Check if unavailable is less than quantity
        if (unavail.size() != getToolQuantity(toolID) ) {
            availability = true;
        }

        // Return tool availability
        return availability;
    }

    /**
     * The updateToolQuantity method updates the quantity of a specified tool
     *
     * @param toolID the tool id to set the quantity
     * @param quantity the quantity to set for the tool id
     *
     * @return true if successful, false if unsuccessful
     */
    public boolean updateToolQuantity(int toolID, int quantity) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.Quantity = \"" + quantity + "\" WHERE (((Tool.ID)=" + toolID + "));\n"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            connection.close(); // Close DB connection

            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * The getToolQuantity method returns the amount of tools that exist in inventory
     * for a specified tool
     *
     * @param toolID the tool id to check
     * @return the quantity for the specified tool
     */
    public int getToolQuantity(int toolID) {
        // init var
        int quantity = 0;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement

            // Get quantity
            ResultSet result = statement.executeQuery("SELECT Tool.Quantity FROM Tool WHERE (((Tool.ID)=" + toolID + "));"); // Get results for SQL Statement
            while (result.next()) {
                quantity = result.getInt("Quantity");
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        return quantity;
    }

    /**
     * The getToolAvailibilityQuantity method returns the amount of unavailable tools (if availability is false), otherwise
     * returns the amount of available tools (if availibility is true)
     *
     * @param toolID
     * @param availability determines if method returns the amount of unavailable tools (if availability is false), otherwise it
     * returns the amount of available tools (if availibility is true)
     *
     * @return
     */
    public int getToolAvailablityQuantity(int toolID, boolean availability) {
        // init var
        int quantity = 0;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement

            // Get quantity
            ResultSet result = statement.executeQuery("SELECT Borrow.[Tool ID], Borrow.Returned FROM Tool INNER JOIN Borrow ON Tool.ID = Borrow.[Tool ID] WHERE (((Borrow.[Tool ID])=" + toolID + ") AND ((Borrow.Returned)=No));"); // Get results for SQL Statement
            while (result.next()) {
                    quantity++;
            }

            // if true, get the amount of available tools instead of unavailable tools
            if (availability) {
                quantity = getToolQuantity(toolID) - quantity;
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // amount of unavailable or available tools
        return quantity;
    }



    /**
     * The borrowTool method allows borrowing of a tool that is currently
     * available
     *
     * @param studentID the student id borrowing the tool
     * @param toolID the tool id of the tool being borrowed
     *
     * @return true if successfully borrowed, else false
     */
    public boolean borrowTool(int studentID, int toolID) {
        if (studentStatus(studentID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Borrow ( [Student ID], [Tool ID] ) VALUES (\"" + studentID + "\",\"" + toolID + "\");"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            // Successful Borrow
            return true;
        }
        // Unsuccessful Borrow
        return false;
    }

    /**
     * The forceReturnTool method allows returns all the quantity of the tool id
     *
     * @param toolID the tool id of all the tool being force returned
     *
     * @return true if successfully returned, else false
     */
    public boolean forceReturnTools(int toolID) {
        if (getToolBorrowerIDS(toolID).size() > 0) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Borrow SET Borrow.[Returned] = True WHERE Borrow.[Tool ID] = " + toolID + ";"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }

            // Successful return
            return true;
        }

        // Unsuccessful return
        return false;
    }

    /**
     * The returnTool method allows returns a tool of id from pool
     *
     * @param toolID the tool id to be returned
     *
     * @return true if successfully returned, else false
     *
     */
    public boolean returnTool(int toolID, int studentID) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Borrow.Returned FROM Borrow WHERE (((Borrow.Returned)=False) AND ((Borrow.[Student ID])=" + studentID + ") AND ((Borrow.[Tool ID])=" + toolID + "));", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT); // Create SQL Statement
            ResultSet result = preparedStatement.executeQuery();
            // Check if any tools found with specified status, add the names to an arraylist
            if (result.next()) {
                result.updateBoolean("Returned", true);
                result.updateRow();
                connection.close(); // Close DB connection
                return true;
            } else {
                connection.close(); // Close DB connection
                return false;
            }
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Unsuccessful return
        return false;
    }

    /**
     * The createTool method adds a tool
     *
     * @param toolName the name of the tool to add
     * @param quantity the quantity of the tool
     *
     * @return tool id if successfully added, else 0
     */
    public int createTool(String toolName, int quantity) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            // Add the tool
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tool ( [Tool Name], Status, Quantity ) VALUES (\"" + toolName + "\",True," + quantity + ");"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement

            // Get the ids of all tools
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.ID FROM Tool WHERE (((Tool.Status)=True));"); // Get results for SQL Statement

            // Get the last index, result is the id of the latest tool added
            int id = 0;
            while (result.next()) {
                id = result.getInt("ID");
            }

            connection.close(); // Close DB connection
            return id; // Successful creation, return id of latest added tool

        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }
        return 0; // Unsuccessful creation, return 0
    }

    /**
     * The disableTool method disables a tool by marking it as inactive
     *
     * @param toolID the tool ID to disable
     *
     * @return true if successfully disabled, else false
     */
    public boolean disableTool(int toolID) {
        if (toolStatus(toolID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.Status = False WHERE (((Tool.ID)=" + toolID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            return true; // Tool disable successful
        } else {
            return false; // Tool disable unsuccessful
        }
    }

    /**
     * The enableTool method re-enables a tool that is currently disabled
     *
     * @param toolID the tool ID to re-enable
     *
     * @return true if successfully re-enabled, else
     */
    public boolean enableTool(int toolID) {
        // Ensure that the tool is disabled, before re-enabling it
        if (!toolStatus(toolID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.Status = True WHERE (((Tool.ID)=" + toolID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

                // Tool re-enable successful
                return true;
            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
        }

        // Tool re-enable unsuccessful
        return false;
    }

    /**
     * The toolNameList method returns the names of all active/inactive tools
     *
     * @param status true if active, false if inactive
     *
     * @return the list of active/inactive tools names
     */
    public ArrayList<String> toolNameList(boolean status) {
        // Initialize Variables
        ArrayList<String> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[Tool Name] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any tools found with specified status, add the names to an arraylist
            while (result.next()) {
                tools.add(result.getString("Tool Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Tool Name List with specified status
        return tools;
    }

    /**
     * The toolNameList method returns the names of all active/inactive and
     * available/unavailable tools
     *
     * @param status true if active, false if inactive
     * @param availability true if tool available, false if tool has at least quantity unavailable
     *
     * @return list of names of active/inactive tools
     */
    public ArrayList<String> toolNameList(boolean status, boolean availability) {
        // Initialize Variables
        ArrayList<String> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status, Tool.ID, Tool.[Tool Name] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any tools found with specified availability/status, add the names to an arraylist
            while (result.next()) {
                // If looking for available tools, add available tools tools to arraylist with specified status
                if (availability) {
                    if (toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getString("Tool Name"));
                    }
                } // If looking for unavailable tools, add available tools to arraylist with specified status
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

        // Return Tool Name List with specified availability and status
        return tools;
    }

    /**
     * The toolIDList method returns the ids of all active/inactive tools
     *
     * @param status true if active, false if inactive
     *
     * @return list of active/inactive tools ids
     */
    public ArrayList<Integer> toolIDList(boolean status) {
        // Initialize Variables
        ArrayList<Integer> tools = new ArrayList<>();

        // Check if any tools found with specified availability/status, add the ids to an arraylist
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[ID] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any tools found with specified status, add the ids to an arraylist
            while (result.next()) {
                tools.add(result.getInt("ID"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Tool ID List with specified status
        return tools;
    }

    /**
     * The toolIDList method returns the names of all active/inactive and
     * available/unavailable tools
     *
     * @param status true if active, false if inactive
     * @param availability true if tool available, false if tool unavailable
     *
     * @return list of names of active/inactive tools
     */
    public ArrayList<Integer> toolIDList(boolean status, boolean availability) {
        // Initialize Variables
        ArrayList<Integer> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status, Tool.ID FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any tools found with specified availability/status, add the ids to an arraylist
            while (result.next()) {
                // If looking for available tools, add available tools to arraylist with specified status
                if (availability) {
                    if (toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getInt("ID"));
                    }
                } // If looking for unavailable tools, add available tools to arraylist with specified status
                else {
                    if (getToolAvailablityQuantity(result.getInt("ID"), false) > 0) {
                        tools.add(result.getInt("ID"));
                    }
                }
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Tool ID List with specified availability and status
        return tools;
    }
}
