package com.billy.marinemotorsportsmanagement;

import java.sql.*;
import java.util.ArrayList;

/**
 * The Student Class allows for various methods of student manipulation and
 * creation/removal
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
 */
public class Student extends Management {

    /**
     * The isStudent method checks if a student exists by ID
     *
     * @param studentID the student ID to check
     * 
     * @return true if the student was found, otherwise false
     */
    public boolean isStudent(int studentID) {
        // Initialize Variables
        boolean found = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.ID FROM Tool INNER JOIN (Student INNER JOIN Borrow ON Student.ID = Borrow.[Student ID]) ON Tool.ID = Borrow.[Tool ID] WHERE (((Student.ID)=" + studentID + "));"); // Get results for SQL Statement

            // Check if student exists
            if (result.next()) {
                found = true;
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return true if student was found, otherwise false
        return found;
    }

    /**
     * The overloaded isStudent method checks if a student exists by ID and is
     * active or inactive (specifically)
     *
     * @param studentID the student ID to check
     * @param status true for active students, false for inactive students
     * 
     * @return true if the student was found, otherwise false
     */
    public boolean isStudent(int studentID, boolean status) {
        // Initialize Variables
        boolean found = false;

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.ID, Student.Status FROM Student WHERE (((Student.ID)=" + studentID + ") AND ((Student.Status)=" + status + "));"); // Get results for SQL Statement
            // Check if student exists and has specified status
            if (result.next()) {
                found = true;
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // return true if the student was found, otherwise false
        return found;
    }
    
    /**
     * The getStudentID method returns the name of a student from their id, otherwise returns null if not found
     * 
     * @param studentID the id of the student to get the name for
     * 
     * @return the id of the student, otherwise 0 if not found
     */
    public String getStudentName(int studentID) {
        // Initialize Variables
        String studentName = null;
        
        // Attempt to connect to db
        try ( Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Student Name] FROM Student WHERE (((Student.ID)=" + studentID + "));"); // Get results for SQL Statement
            // If student exists, get student name
            if (result.next()) {
                studentName = result.getString("Student Name");
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }
        
        // Return Student Name, otherwise null
        return studentName;
    }
    
    /**
     * The getStudentSession method returns the session of a student from their id, otherwise returns null if not found
     * @param studentID the id of the student to get the session for
     * 
     * @return the session (AM or PM) otherwise null if not found
     */
    public String getStudentSession(int studentID) {
        // Initialize Variables
        String session = null;
        
        // Attempt to connect to db
        try ( Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Session] FROM Student WHERE (((Student.ID)=" + studentID + "));"); // Get results for SQL Statement
            // If student found, get session (AM/PM)
            if (result.next()) {
                session = result.getString("Session");
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Student Session, otherwise null
        return session;
    }

    public ArrayList<Integer> getStudentToolIDList(int studentID) {
        // Initialize Variables
        ArrayList<Integer> toolIDList = new ArrayList<>();

        // Attempt to connect to db
        try ( Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Borrow.[Tool ID] FROM Tool INNER JOIN (Student INNER JOIN Borrow ON Student.ID = Borrow.[Student ID]) ON Tool.ID = Borrow.[Tool ID] WHERE (((Borrow.[Student ID])=" + studentID + ") AND ((Borrow.Returned)=False));"); // Get results for SQL Statement
            // If tools found, add to list
            while (result.next()) {
                toolIDList.add(result.getInt("Tool ID"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Student Session, otherwise null
        return toolIDList;
    }

    /**
     * The studentStatus method checks if a student is inactive or active
     *
     * @param studentID the student ID to check
     * 
     * @return true if active, false if inactive/not found
     */
    public boolean studentStatus(int studentID) {
        return isStudent(studentID, true);
    }

    /**
     * The addStudent method adds a student (admin only)
     *
     * @param name the student name
     * @param session specifies AM/PM session
     * @return true if successfully created, else false
     */
    public boolean addStudent(String name, String session) {
        if (session.toUpperCase().contains("AM") || session.toUpperCase().contains("PM") && sessionStatus()) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Student ( [Student Name], [Session], [Status] ) VALUES (\"" + name + "\", \"" + session.toUpperCase() + "\",\"True\");"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            return true; // Student Successfully added
        } else {
            return false; // Student Unsuccessfully added
        }
    }

    /**
     * The disableStudent method deactivates a student
     *
     * @param studentID the student ID to deactivate
     * 
     * @return true if successfully deactivated, else false
     */
    public boolean disableStudent(int studentID) {
        if (isStudent(studentID, true)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Student SET Student.Status = False WHERE (((Student.ID)=" + studentID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
            return true; // Student Successfully disabled
        } else {
            return false; // Student Unsuccessfully disabled
        }
    }

    /**
     * The deactivateAllStudents method deactivates every active student
     * @return true if successful, else false
     */
    public boolean deactivateAllStudents() {
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Student SET Student.Status = False WHERE (((Student.Status)=True));"); // Create SQL Statement
            preparedStatement.executeUpdate(); // execute statement
            connection.close(); // Close DB connection

            // Students sucessfully deactivated
            return true;
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Students unsuccessfully deactivated
        return false;
    }
    
    /**
     * The enableStudent method reactivates a student that is currently inactive
     *
     * @param studentID the student ID to reactivate
     * 
     * @return true if successfully reactivated, else
     */
    public boolean enableStudent(int studentID) {
        // Ensure that the tool is disabled
        if (!studentStatus(studentID)) {
            // Attempt to connect to db
            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Student SET Student.Status = True WHERE (((Student.ID)=" + studentID + "));"); // Create SQL Statement
                preparedStatement.executeUpdate(); // execute statement
                connection.close(); // Close DB connection

                // Student Successfully reactivated
                return true;
            } catch (SQLException ex) {
                // IF cannot connect to DB, print exception
                ex.printStackTrace();
            }
        }
        
        // Student Unsuccessfully reactivated
        return false;
    }

    /**
     * The studentNameList method returns an arraylist of inactive or active
     * students names
     *
     * @param status true if active, false if inactive
     * 
     * @return list of students with specified status names
     */
    public ArrayList<String> studentNameList(boolean status) {
        // Initialize Variables
        ArrayList<String> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Student Name] FROM Student WHERE (((Student.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any students found with specified status, add the names to an arraylist
            while (result.next()) {
                students.add(result.getString("Student Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Student Name List with specified status
        return students;
    }

    /**
     * The overloaded studentNameList method returns an arraylist of inactive or active, and am/pm
     * students names
     *
     * @param status true if active, false if inactive
     * @param session specifies AM or PM session
     * 
     * @return list of students with specified status names
     */
    public ArrayList<String> studentNameList(boolean status, String session) {
        // Initialize Variables
        ArrayList<String> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[Student Name] FROM Student WHERE (((Student.Status)=" + status + ") AND ((Student.Session)=\"" + session + "\"));"); // Get results for SQL Statement

            // Check if any students found with specified status/session, add the names to an arraylist
            while (result.next()) {
                students.add(result.getString("Student Name"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }
        
        // Return Student Name List with specified status/session
        return students;
    }
    
    /**
     * The studentIDList method returns an arraylist of inactive or active, and
     * am/pm students ids
     *
     * @param status true if active, false if inactive
     * 
     * @return list of students with specified status ids
     */
    public ArrayList<Integer> studentIDList(boolean status) {
        // Initialize Variables
        ArrayList<Integer> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[ID] FROM Student WHERE (((Student.Status)=" + status + "));"); // Get results for SQL Statement

            // Check if any students found with specified status, add the ids to an arraylist
            while (result.next()) {
                students.add(result.getInt("ID"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Student ID List with specified status
        return students;
    }
    
    /**
     * The overloaded studentIDList method returns an arraylist of inactive or active, and
     * am/pm students ids
     *
     * @param status true if active, false if inactive
     * @param session specifies AM or PM session
     * 
     * @return list of students with specified status ids
     */
    public ArrayList<Integer> studentIDList(boolean status, String session) {
        // Initialize Variables
        ArrayList<Integer> students = new ArrayList<>();

        // Attempt to connect to db
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            Statement statement = connection.createStatement(); // Create SQL Statement
            ResultSet result = statement.executeQuery("SELECT Student.[ID] FROM Student WHERE (((Student.Status)=" + status + ") AND ((Student.Session)=\"" + session + "\"));"); // Get results for SQL Statement

            // Check if any students found with specified status/session, add the ids to an arraylist
            while (result.next()) {
                students.add(result.getInt("ID"));
            }

            connection.close(); // Close DB connection
        } catch (SQLException ex) {
            // IF cannot connect to DB, print exception
            ex.printStackTrace();
        }

        // Return Student ID List with specified status/session
        return students;
    }
}