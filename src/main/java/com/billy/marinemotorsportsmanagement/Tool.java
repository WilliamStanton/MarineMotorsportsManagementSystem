package com.billy.marinemotorsportsmanagement;

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
     * @return the name of the tool, otherwise null if not found
     */
    public String getToolName(int toolID) {
        // declare variables
        String toolName = null;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[Tool Name] FROM Tool WHERE (((Tool.ID)=" + toolID + "));"); // Get results for SQL Statement

            // get result
            if (result.next()) {
                toolName = result.getString("Tool Name");
            }

            connection.close(); // Close DB connection
            
            // return tool name
            return toolName;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return tool not found
        return null;
    }
    
    /**
     * The getToolBorrowerID method returns the id of the person borrowing a tool currently
     * 
     * UNTESTED
     * @param toolID the tool id to get the borrower name for
     * @return the name of the borrower, otherwise 0 if the tool isn't being borrowed
     */
    public int getToolBorrowerID(int toolID) {
        // initialize borrower
        int borrower = 0;
        
        ArrayList<Boolean> avail = new ArrayList<>();
        ArrayList<Integer> id = new ArrayList<>();
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Borrow.Returned, Borrow.[Student ID] FROM Borrow WHERE (((Borrow.[Tool ID])=" + toolID + "));"); // Get results for SQL Statement

            // add all results
            while (result.next()) {
                avail.add(result.getBoolean("Returned"));
                id.add(result.getInt("Student ID"));
            }
            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // check last result for answer, if no results, then available
        if (avail.size() <= 0) {
            return 0;
        } else {
            borrower = id.get(avail.size() - 1);
        }
        
        // return borrower, otherwise null if not found
        return borrower;
    }
    
    /**
     * The toolStatus method checks if a tool is enabled or disabled
     *
     * @param toolID the tool id to check
     * @return true if enabled, false if disabled/not found
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
     * @return tool id if successfully added, else 0
     */
    public int createTool(String toolName) {
        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tool ( [Tool Name] ) VALUES (\"" + toolName + "\");"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            
            // get last tool added id
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.ID FROM Tool WHERE (((Tool.Status)=True));"); // Get results for SQL Statement

            // add all results
            int id = 0;
            while (result.next()) {
                id = result.getInt("ID");
            }
            
            connection.close(); // Close DB connection
            return id; // succesful attempt, return latest added tool
            

        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }
        return 0; // unsuccessful attempt
    }

    /**
     * The disableTool method disables a tool by marking it as inactive
     *
     * @param toolID the tool ID to disable
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
            return true; // succesful attempt
        } else {
            return false; // unsuccesful attempt
        }
    }
    
    /**
     * The enableTool method re-enables a tool that is currently disabled
     *
     * @param toolID the tool ID to re-enable
     * @return true if successfully re-enabled, else 
     */
    public boolean enableTool(int toolID) {
        // ensure that the tool is disabled
        if (!toolStatus(toolID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Tool SET Tool.Status = True WHERE (((Tool.ID)=" + toolID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

                // successful reactivation
                return true;
            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
        }
        
        // unsuccessful reactivation
        return false;
    }
    
    /**
     * The toolNameList method returns the names of all active/inactive tools
     *
     * @param status true if active, false if inactive
     * @return the list of active/inactive tools names
     */
    public ArrayList<String> toolNameList(boolean status) {
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

        // return name list
        return tools;
    }

    /**
     * The toolNameList method returns the names of all active/inactive and available/unavailable tools
     *
     * @param status true if active, false if inactive
     * @param availability true if tool available, false if tool unavailable
     * @return list of names of active/inactive tools
     */
    public ArrayList<String> toolNameList(boolean status, boolean availability) {
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

        // return name list
        return tools;
    }
    
    /**
     * The toolIDList method returns the ids of all active/inactive tools
     *
     * @param status true if active, false if inactive
     * @return list of active/inactive tools ids
     */
    public ArrayList<Integer> toolIDList(boolean status) {
        // declare variables
        ArrayList<Integer> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.[ID] FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // check if any results found
            while (result.next()) {
                tools.add(result.getInt("ID"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return id list
        return tools;
    }
    
    /**
     * The toolNameList method returns the names of all active/inactive and available/unavailable tools
     *
     * @param status true if active, false if inactive
     * @param availability true if tool available, false if tool unavailable
     * @return list of names of active/inactive tools
     */
    public ArrayList<Integer> toolIDList(boolean status, boolean availability) {
        // declare variables
        ArrayList<Integer> tools = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Tool.Status, Tool.ID FROM Tool WHERE (((Tool.Status)=" + status + "));"); // Get results for SQL Statement

            // get results
            while (result.next()) {
                // available tools
                if (availability) {
                    if (toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getInt("ID"));
                    }
                } // unavailable tools
                else {
                    if (!toolAvailability(result.getInt("ID"))) {
                        tools.add(result.getInt("ID"));
                    }
                }
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return name list
        return tools;
    }
}
