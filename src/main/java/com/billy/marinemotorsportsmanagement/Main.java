package com.billy.marinemotorsportsmanagement;

import java.util.ArrayList;
import javax.swing.*;

/**
 * This Main Class operates the Marine Motorsports Management Program
 *
 * @author Billy Stanton
 * @version 1.0
 * @date 11/1/23
 */
public class Main {

    public static void main(String[] args) {
        // initiate api
        Tool api = new Tool();
        // load main menu
        mainMenu(api);
    }

    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     *
     * @param api the api
     */
    public static void mainMenu(Tool api) {
        String[] options = {"Tool Master", "Admin", "Exit"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System\nWhat user would you like to login as?", "Marine Motorsports Management System", 0, 3, null, options, options[0]);
        switch (selection) {
            case 0:
                toolMaster(api);
                break;

            case 1:
                // load admin panel
                admin(api);
                break;

            case 2:
                // exit application
                api.exit();
        }
    }

    /**
     * The toolMaster method controls various different functions that are
     * utilized by the toolMaster
     *
     * @param api contains methods for management
     */
    public static void toolMaster(Tool api) {
        // Display options
        String[] options = {"Borrow Tool", "Return Tool", "Tool Report", "Back to Main Menu"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to do?", "Tool Master Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            case 0:
                // Borrow Tool

                break;

            case 1:
                // Return Tool

                break;

            case 2:
                // Tool Report
                toolReport(api);
                break;

            case 3:
                // Back to Main Menu
                mainMenu(api);
                break;
        }
    }

    /**
     * The toolReport method provides various reports for students and tools
     *
     * @param api contains methods for management
     */
    public static void toolReport(Tool api) {
        // Display Options
        String[] options = {"Available Tools", "Unavailable Tools", "Back to Tool Master Panel"};
        String[] sessionOptions = {"AM Class", "PM Class", "All Classes", "Back"};
        int selection = JOptionPane.showOptionDialog(null, "Please select an action", "Tool Master Panel - Tool Report", 0, 3, null, options, options[0]);
        switch (selection) {
            case 0:
                // Available Tools
                int availableSession = JOptionPane.showOptionDialog(null, "Please select the Class Session to view", "Tool Master Panel - Tool Report (Unavailable Tools)", 0, 3, null, sessionOptions, sessionOptions[0]);
                switch (availableSession) {
                    case 0:
                        // AM Class

                        
                        
                                                
                       break;

                    case 1:
                        // PM Class
                        break;

                    case 2:
                        // All Classes
                        // Check if any available tools
                        if (!api.toolIDList(true, true).isEmpty()) {
                            // Get all tool ids
                            ArrayList<Integer> allToolIDSAvailable = api.toolIDList(true, true);

                            // Get all tool names
                            ArrayList<String> allToolNamesAvailable = new ArrayList<>();
                            for (int i = 0; i < allToolIDSAvailable.size(); i++) {
                                allToolNamesAvailable.add(api.getToolName(allToolIDSAvailable.get(i)));
                            }

                            // Build list of available tools
                            String allClassAvailable = "";
                            for (int i = 0; i < allToolIDSAvailable.size(); i++) {
                                allClassAvailable += i+1 + ") Tool Name: " + allToolNamesAvailable.get(i)
                                        + "\n     - Tool ID: " + allToolIDSAvailable.get(i) + "\n\n";
                            }

                            // Display list of unavailable tools
                            JOptionPane.showMessageDialog(null, "Available Tools - All Classes\n\n" + allClassAvailable, "Tool Master Panel - Tool Report (All Classes, Available Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                        } 
                        
                        // Else no tools available
                        else {
                            JOptionPane.showMessageDialog(null, "No tools are currently available in All Classes", "Tool Master Panel - Tool Report (All Classes, No Available Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool report
                        toolReport(api);
                        break;

                    case 3:
                        // Back to Tool Report
                        toolReport(api);
                        break;
                }
                break;

            case 1:
                // Unavailable Tools                
                int unavailableSession = JOptionPane.showOptionDialog(null, "Please select the Class Session to view", "Tool Master Panel - Tool Report (Available Tools)", 0, 3, null, sessionOptions, sessionOptions[0]);
                switch (unavailableSession) {
                    case 0:
                        // AM Class
                        break;

                    case 1:
                        break;

                    case 2:
                        // All Classes
                        // Check if any unavailable tools
                        if (!api.toolIDList(true, false).isEmpty()) {
                            // Get all tool ids
                            ArrayList<Integer> allToolIDSUnavailable = api.toolIDList(true, false);

                            // Get all tool names
                            ArrayList<String> allToolNamesUnavailable = new ArrayList<>();
                            for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                                allToolNamesUnavailable.add(api.getToolName(allToolIDSUnavailable.get(i)));
                            }

                            // Get all borrower names
                            ArrayList<String> allBorrowerNamesUnavailable = new ArrayList<>();
                            for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                                allBorrowerNamesUnavailable.add(api.getStudentName(api.getToolBorrowerID(allToolIDSUnavailable.get(i))));
                            }

                            // Build list of unavailable tools
                            String allClassUnavailable = "";
                            for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                                allClassUnavailable += i+1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                        + "\n     - Tool ID: " + allToolIDSUnavailable.get(i) 
                                        + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                                        + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                            }
                            
                            // Display list of unavailable tools
                            JOptionPane.showMessageDialog(null, "Unavailable Tools - All Classes\n\n" + allClassUnavailable, "Tool Master Panel - Tool Report (All Classes, Unavailable Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                        } 
                        
                        // Else no tools available
                        else {
                            JOptionPane.showMessageDialog(null, "No tools are currently unavailable for the All Class", "Tool Master Panel - Tool Report (All Classes, No Unavailable Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool report
                        toolReport(api);
                        break;

                    case 3:
                        // Back to Tool Report
                        toolReport(api);
                        break;
                }
                break;

            case 2:
                toolMaster(api);
                break;
        }
    }

    /**
     * The admin method controls authentication and various different admin
     * functions for the admin panel
     *
     * @param api contains methods for management
     */
    public static void admin(Tool api) {
        // Login Field
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        Object[] loginField = {
            "Username:", username,
            "Password:", password
        };

        // Attempt login till session status is active
        while (!api.sessionStatus()) {
            // Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                JOptionPane.showMessageDialog(null, "Welcome, Login Successful", "Admin Login", JOptionPane.INFORMATION_MESSAGE, null);
            } // If no, exit to menu
            else if (option == JOptionPane.CANCEL_OPTION) {
                mainMenu(api);

                // Else, login was incorrect
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Admin Login", JOptionPane.ERROR_MESSAGE, null);
            }
        }

        // Display options
        String[] options = {"Student Management", "Tool Management", "Logout to Main Menu"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to do?", "Admin Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            case 0:
                // Manage Students
                studentManagement(api);
                break;

            case 1:
                // Manage Tools
                manageTools(api);
                break;

            case 2:
                // Return to main menu and logout
                api.logout();
                mainMenu(api);
                break;
        }
    }

    /**
     * The studentManagement method provides options for managing students and
     * is used only in the admin class
     *
     * @param api contains methods for management
     */
    public static void studentManagement(Tool api) {
        // Select Add or Remove or Back
        String[] studentOptions = {"Add a Student", "Remove a Student", "Back to Admin Panel"};
        int studentSelection = JOptionPane.showOptionDialog(null, "Please select an action", "Admin Panel - Student Management", 0, JOptionPane.QUESTION_MESSAGE, null, studentOptions, studentOptions[0]);
        switch (studentSelection) {
            case 0:
                // Add student
                // Student Field
                JTextField fullName = new JTextField();
                JTextField studentSession = new JTextField();
                Object[] addStudentField = {
                    "Full Name", fullName,
                    "Session (AM or PM):", studentSession
                };

                // 
                int addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION);
                // If Ok, attempt student creation
                if (addStudentInfo == JOptionPane.OK_OPTION) {
                    boolean added = false;
                    // If student added
                    if (api.addStudent(fullName.getText(), studentSession.getText())) {
                        String successMessage = "Student successfully added:\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                        JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                    } // Else, if student not added, try again till added or cancelled
                    else {
                        while (!added) {
                            JOptionPane.showMessageDialog(null, "Student has not been added, please ensure you enter a valid session.", "Add Student", JOptionPane.ERROR_MESSAGE, null);
                            addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Admin Panel - Add Student", JOptionPane.OK_CANCEL_OPTION);
                            // if yes, continue trying to add student
                            if (addStudentInfo == JOptionPane.YES_OPTION) {
                                if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                    added = true;
                                    String successAddMessage = "Student successfully added:\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Student", JOptionPane.INFORMATION_MESSAGE, null);
                                }
                            } // if no, exit loop and return to menu
                            else {
                                break;
                            }
                        }
                    }

                    // Return to student management panel
                    studentManagement(api);
                } // If No, return to student management panel
                else {
                    studentManagement(api);
                }
                break;

            case 1:
                // Remove student
                // if any students are active
                if (!api.studentNameList(true).isEmpty()) {
                    // Get all students names
                    ArrayList<String> studentNames = api.studentNameList(true);
                    // Get all student ids
                    ArrayList<Integer> studentIds = api.studentIDList(true);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < studentNames.size(); i++) {
                        tempArray.add(studentNames.get(i) + ", ID: " + studentIds.get(i));
                    }

                    // Arraylist -> Array
                    String[] studentList = new String[tempArray.size()];
                    studentList = tempArray.toArray(studentList);

                    // Select and remove student
                    String studentToRemove = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Remove Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToRemove != null) {
                        // Strip String to only ID
                        String studentID = studentToRemove.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.removeStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully removed: " + studentToRemove;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Remove Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been removed. Error.", "Admin Panel - Remove Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagement(api);
                    }
                } // if no active students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to remove.", "Admin Panel - Remove Student", JOptionPane.ERROR_MESSAGE, null);
                }
                // Return to student management panel once completed
                studentManagement(api);
                break;

            case 2:
                // Return back to admin panel
                admin(api);
                break;
        }
    }

    /**
     * The manageStudents method provides options for managing students and is
     * used only in the admin class
     *
     * @param api contains methods for management
     */
    public static void manageTools(Tool api) {
        // Select Add or Remove or Back
        String[] toolOptions = {"Add a tool", "Remove a tool", "Back to Admin Panel"};
        int toolSelection = JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - Tool Management", 0, 3, null, toolOptions, toolOptions[0]);
        switch (toolSelection) {
            case 0:
                // Add a tool
                String toolName = JOptionPane.showInputDialog(null, "Enter a name for the new tool", "Admin Panel - Add Tool", JOptionPane.QUESTION_MESSAGE);
                // If yes, add 
                if (toolName != null) {
                    // If tool added
                    int toolID = api.createTool(toolName);
                    if (toolID != 0) {
                        String successAddMessage = "Tool successfully added: " + toolName + ", ID: " + toolID;
                        JOptionPane.showMessageDialog(null, successAddMessage, "Admin Panel - Add Tool", JOptionPane.INFORMATION_MESSAGE);
                    } // Else if tool not added,
                    else {
                        JOptionPane.showMessageDialog(null, "Tool has not been added, error.", "Admin Panel - Add Tool", JOptionPane.ERROR_MESSAGE);
                    }
                    // If no, return to admin panel
                } else {
                    manageTools(api);
                }

                // Return to tool management once completed
                manageTools(api);
                break;

            case 1:
                // Remove a tool
                // Get active tool list
                if (!api.toolNameList(true).isEmpty()) {
                    ArrayList<String> toolNameList = api.toolNameList(true);
                    ArrayList<Integer> toolIDList = api.toolIDList(true);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < toolNameList.size(); i++) {
                        tempArray.add(toolNameList.get(i) + ", ID: " + toolIDList.get(i));
                    }

                    // Arraylist -> Array
                    String[] toolList = new String[tempArray.size()];
                    toolList = tempArray.toArray(toolList);

                    // Select and remove tool
                    String toolToRemove = (String) JOptionPane.showInputDialog(null, "Select a Tool", "Admin Panel - Remove Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToRemove != null) {
                        // Strip String to only ID
                        String toolID = toolToRemove.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.removeTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully removed: " + toolToRemove;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Remove Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been removed. Error.", "Admin Panel - Remove Tool", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool management once completed
                        manageTools(api);
                        break;
                    } // If No, return to tool management
                    else {
                        manageTools(api);
                    }
                } // if no active tools
                else {
                    JOptionPane.showMessageDialog(null, "There are no tools to remove.", "Admin Panel - Remove Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                manageTools(api);
                break;

            case 2:
                // Back to admin panel
                admin(api);
                break;
        }

    }
}
