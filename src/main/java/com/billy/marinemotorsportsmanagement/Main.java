package com.billy.marinemotorsportsmanagement;

import java.util.ArrayList;
import javax.swing.*;

/**
 * This Main Class operates the Marine Motorsports Management Program
 *
 * @author Billy Stanton
 * @version 1.0
 * @since 11/1/23
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
        // Initialize menu options
        String[] options = {"Tool Master", "Admin", "Exit"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System\nWhat user would you like to login as?", "Marine Motorsports Management System", 0, 3, null, options, options[0]);
        switch (selection) {
            // load tool master panel
            case 0:
                toolMaster(api);
                break;

            // load admin panel
            case 1:
                admin(api);
                break;

            // exit application
            case 2:

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
        // Initialize menu options
        String[] options = {"Borrow Tool", "Return Tool", "Tool Report", "Back to Main Menu"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to do?", "Tool Master Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            // Borrow Tool
            case 0:
                borrowTool(api);
                break;

            // Return Tool    
            case 1:
                returnTool(api);
                break;

            // Tool Report
            case 2:
                toolReport(api);
                break;

            // Back to Main Menu
            case 3:
                mainMenu(api);
                break;
        }
    }

    /**
     * The borrowTool method allows the tool master to handle returning a tool
     *
     * @param api contains methods for management
     */
    public static void returnTool(Tool api) {
        // Check if any tools available to borrow
        if (api.toolIDList(true, true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No available tools found", "Tool Master Panel - Return Tool (No Unavailable Tools Found)", JOptionPane.ERROR_MESSAGE, null);
            toolMaster(api);
        } // else continue
        else {
            // build unavailable tool list
            Integer[] toolIDList = new Integer[api.toolIDList(true, false).size()];
            toolIDList = api.toolIDList(true, false).toArray(toolIDList);
            String[] toolList = new String[toolIDList.length];
            toolList = api.toolNameList(true, false).toArray(toolList);

            // add id to to list that already contains name
            for (int i = 0; i < toolList.length; i++) {
                toolList[i] += ", ID: " + toolIDList[i] + ", Borrower: " + api.getStudentName(api.getToolBorrowerID(toolIDList[i]));
            }

            // select a tool from dropdown
            String toolSelected = (String) JOptionPane.showInputDialog(null, "Please select a tool to return", "Tool Master Panel - Return Tool (Select Tool)", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
            if (toolSelected != null) {
                toolSelected = toolSelected.replaceAll("[^0-9]+", "");
                // borrow the tool
                if (api.returnTool(Integer.parseInt(toolSelected))) {
                    JOptionPane.showMessageDialog(null, "Tool Successfully Returned:\n\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)), "Tool Master Panel - Return Tool", JOptionPane.INFORMATION_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(null, "Tool has not been returned, unknown error.", "Tool Master Panel - Return Tool", JOptionPane.ERROR_MESSAGE, null);
                }
            } else {
                // return to tool master panel
                toolMaster(api);
            }
        }

        // return back to tool master panel once all completed
        toolMaster(api);
    }

    /**
     * The borrowTool method allows the tool master to handle borrowing a tool
     * for the student requiring it
     *
     * @param api contains methods for management
     */
    public static void borrowTool(Tool api) {
        // Check if any tools available to borrow
        if (api.toolIDList(true, true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No available tools found", "Tool Master Panel - Borrow Tool (No Available Tools Found)", JOptionPane.ERROR_MESSAGE, null);
            toolMaster(api);
        } // else continue 
        else {
            // Choose session
            String[] sessionOptions = {"AM", "PM", "Back"};
            int session = JOptionPane.showOptionDialog(null, "Please select the current session", "Tool Master Panel - Borrow Tool", 0, 3, null, sessionOptions, sessionOptions[0]);

            // if 0 then AM, if 1 then PM
            if (session == 0 || session == 1) {
                // set AM or PM
                String sessionSelected = "";
                if (session == 0) {
                    sessionSelected = "AM";
                } else {
                    sessionSelected = "PM";
                }

                // build student list
                String[] studentList = new String[api.studentNameList(true, sessionSelected).size()];
                studentList = api.studentNameList(true, sessionSelected).toArray(studentList);
                Integer[] studentIDList = new Integer[studentList.length];
                studentIDList = api.studentIDList(true, sessionSelected).toArray(studentIDList);

                // add id to student list that already contains name
                for (int i = 0; i < studentList.length; i++) {
                    studentList[i] += ", ID: " + studentIDList[i];
                }

                // select a student from dropdown
                String studentSelected = (String) JOptionPane.showInputDialog(null, "Please select the student borrowing a tool", "Tool Master Panel - Borrow Tool (Select Student)", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                if (studentSelected != null) {
                    studentSelected = studentSelected.replaceAll("[^0-9]+", ""); // strip everything but id

                    // build available tool list
                    Integer[] toolIDList = new Integer[api.toolIDList(true, true).size()];
                    toolIDList = api.toolIDList(true, true).toArray(toolIDList);
                    String[] toolList = new String[toolIDList.length];
                    toolList = api.toolNameList(true, true).toArray(toolList);

                    // add id to to list that already contains name
                    for (int i = 0; i < toolList.length; i++) {
                        toolList[i] += ", ID: " + toolIDList[i];
                    }

                    // select a tool from dropdown
                    String toolSelected = (String) JOptionPane.showInputDialog(null, "Please select a tool to borrow", "Tool Master Panel - Borrow Tool (Select Tool)", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    if (toolSelected != null) {
                        toolSelected = toolSelected.replaceAll("[^0-9]+", "");
                        // borrow the tool
                        if (api.borrowTool(Integer.parseInt(studentSelected), Integer.parseInt(toolSelected))) {
                            JOptionPane.showMessageDialog(null, "Tool Successfully Borrowed:\n\nTool Name: " + api.getToolName(Integer.parseInt(toolSelected)) + "\nStudent: " + api.getStudentName(Integer.parseInt(studentSelected)), "Tool Master Panel - Borrow Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been added, unknown error.", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // else return to tool master panel
                    else {
                        toolMaster(api);
                    }
                } // else return to tool master panel
                else {
                    toolMaster(api);
                }
            } // else return to tool master panel
            else {
                toolMaster(api);
            }
        }

        // return back to tool master panel once all completed
        toolMaster(api);
    }

    /**
     * The toolReport method provides various reports for students and tools
     *
     * @param api contains methods for management
     */
    public static void toolReport(Tool api) {
        // Initialize options
        String[] options = {"Available Tools", "Unavailable Tools", "Back to Tool Master Panel"};
        int selection = JOptionPane.showOptionDialog(null, "Please select an action", "Tool Master Panel - Tool Report", 0, 3, null, options, options[0]);

        switch (selection) {
            // Available Tools
            case 0:
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
                        allClassAvailable += i + 1 + ") Tool Name: " + allToolNamesAvailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSAvailable.get(i) + "\n\n";
                    }

                    // Display list of available tools
                    JOptionPane.showMessageDialog(null, "Available Tools \n\n" + allClassAvailable, "Tool Master Panel - Tool Report (Available Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                } // Else no tools available
                else {
                    JOptionPane.showMessageDialog(null, "No available tools found", "Tool Master Panel - Tool Report (Available Tools)", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool report
                toolReport(api);
                break;

            // Unavailable Tools
            case 1:
                unavailableTools(api);
                break;

            // Back to tool master panel
            case 2:
                toolMaster(api);
                break;
        }
    }

    /**
     * The unavailableTools method provides the methods for viewing the various
     * unavailable tool reports
     *
     * @param api contains methods for management
     */
    public static void unavailableTools(Tool api) {
        // Initialize options
        String[] sessionOptions = {"AM Class", "PM Class", "All Classes", "Back"};

        // Check if any unavailable tools
        if (api.toolIDList(true, false).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No unavailable tools found", "Tool Master Panel - Tool Report (No Unavailable Tools Found)", JOptionPane.ERROR_MESSAGE, null);
            toolMaster(api);
        }

        // Load all Unavailable Tools
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

        // Get all borrower sessions
        ArrayList<String> allBorrowerSessionsUnavailable = new ArrayList<>();
        for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
            allBorrowerSessionsUnavailable.add(api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))));
        }

        int unavailableSession = JOptionPane.showOptionDialog(null, "Please select the Class Session to view", "Tool Master Panel - Tool Report (Unavailable Tools)", 0, 3, null, sessionOptions, sessionOptions[0]);
        switch (unavailableSession) {
            // AM Class - Unavailable Tools
            case 0:
                // Build list of AM Class unavailable tools
                String amClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("AM")) {
                        amClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                                + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                    }
                }
                // Display list of unavailable tools
                JOptionPane.showMessageDialog(null, "Unavailable Tools - AM Class\n\n" + amClassUnavailable, "Tool Master Panel - Tool Report (AM Class, Unavailable Tools)", JOptionPane.INFORMATION_MESSAGE, null);

                // Return to unavailable tools panel
                unavailableTools(api);
                break;

            // PM Class - Unavailable Tools
            case 1:
                // Build list of PM Class unavailable tools
                String pmClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    if (allBorrowerSessionsUnavailable.get(i).equals("PM")) {
                        pmClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                                + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                                + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                                + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                    }
                }

                // Display list of unavailable tools
                JOptionPane.showMessageDialog(null, "Unavailable Tools - PM Class\n\n" + pmClassUnavailable, "Tool Master Panel - Tool Report (PM Class, Unavailable Tools)", JOptionPane.INFORMATION_MESSAGE, null);

                // Return to unavailable tools panel
                unavailableTools(api);
                break;

            // All Classes - Unavailable Tools
            case 2:
                // Build list of all unavailable tools
                String allClassUnavailable = "";
                for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
                    allClassUnavailable += i + 1 + ") Tool Name: " + allToolNamesUnavailable.get(i)
                            + "\n     - Tool ID: " + allToolIDSUnavailable.get(i)
                            + "\n     - Borrower: " + allBorrowerNamesUnavailable.get(i) + ""
                            + "\n     - Session: " + api.getStudentSession(api.getToolBorrowerID(allToolIDSUnavailable.get(i))) + "\n\n";
                }

                // Display list of unavailable tools
                JOptionPane.showMessageDialog(null, "Unavailable Tools - All Classes\n\n" + allClassUnavailable, "Tool Master Panel - Tool Report (All Classes, Unavailable Tools)", JOptionPane.INFORMATION_MESSAGE, null);

                // Return to unavailable tools panel
                unavailableTools(api);
                break;

            // Back to Tool Report Panel
            case 3:
                toolReport(api);
                break;
        }

        // Ensure return to Tool Report Panel
        toolReport(api);
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
        JPasswordField password = new JPasswordField();
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

        // Initialize options
        String[] options = {"Student Management", "Tool Management", "Logout to Main Menu"};
        int selection = JOptionPane.showOptionDialog(null, "What would you like to do?", "Admin Panel", 0, 3, null, options, options[0]);
        switch (selection) {
            // Manage Students
            case 0:
                studentManagement(api);
                break;

            // Manage Tools
            case 1:
                manageTools(api);
                break;

            // Return to main menu and logout
            case 2:
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
        String[] studentOptions = {"Add a Student", "Disable a Student", "Back to Admin Panel"};
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
                        String successMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
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
                                    String successAddMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
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
                // Disable student
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

                    // Select and disable student
                    String studentToRemove = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Disable Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToRemove != null) {
                        // Strip String to only ID
                        String studentID = studentToRemove.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.removeStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully removed: " + studentToRemove;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been disabled. Error.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagement(api);
                    }
                } // if no active students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to disable.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
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
        String[] toolOptions = {"Add a tool", "Deactivate a tool", "View Tools", "Back"};
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
                // Deactivate a tool
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

                    // Select and Deactivate tool
                    String toolToRemove = (String) JOptionPane.showInputDialog(null, "Deactivate a Tool", "Admin Panel - Deactivate Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToRemove != null) {
                        // Strip String to only ID
                        String toolID = toolToRemove.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.removeTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully removed: " + toolToRemove;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Deactivate Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been deactivated. Error.", "Admin Panel - Deactivate Tool", JOptionPane.ERROR_MESSAGE, null);
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
                    JOptionPane.showMessageDialog(null, "There are no tools to deactivate.", "Admin Panel - Deactivate Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                manageTools(api);
                break;

            // View tools    
            case 2:
                // Check if any tools available to borrow
                if (api.toolIDList(false).isEmpty() && api.toolIDList(true).isEmpty()) {
                    // Back to tool master if no tools found
                    JOptionPane.showMessageDialog(null, "No tools found", "Tool Master Panel - Borrow Tool (No Available Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                    toolMaster(api);
                } // else continue 
                else {
                    // Initialize Options
                    String[] viewToolOptions = {"View Active Tools", "View Inactive Tools", "View all Tools", "Back"};
                    int viewToolSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Tools", 0, 3, null, viewToolOptions, viewToolOptions[0]);
                    switch (viewToolSelection) {
                        // view active tools
                        case 0:
                            // check if active tools found
                            if (!api.toolNameList(true).isEmpty()) {
                                // Get active tool list
                                ArrayList<String> activeToolNameList = api.toolNameList(true);
                                ArrayList<Integer> activeToolIDList = api.toolIDList(true);

                                // Combine names with ids
                                ArrayList<String> activeTempArray = new ArrayList<>();
                                for (int i = 0; i < activeToolNameList.size(); i++) {
                                    activeTempArray.add(activeToolNameList.get(i) + ", ID: " + activeToolIDList.get(i));
                                }

                                // Arraylist -> Array
                                String[] activeToolList = new String[activeTempArray.size()];
                                activeToolList = activeTempArray.toArray(activeToolList);
                                JOptionPane.showMessageDialog(null, activeToolList, "Admin Panel - View Tools (Active Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                            } // else no active tools found
                            else {
                                JOptionPane.showMessageDialog(null, "No Active Tools found", "Tool Master Panel - View Tool (No Active Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                            }

                            break;

                        // view inactive tools
                        case 1:
                            // check if inactive tools found
                            if (!api.toolNameList(false).isEmpty()) {
                                // Get inactive tool list
                                ArrayList<String> inactiveToolNameList = api.toolNameList(false);
                                ArrayList<Integer> inactiveToolIDList = api.toolIDList(false);

                                // Combine names with ids
                                ArrayList<String> inactiveTempArray = new ArrayList<>();
                                for (int i = 0; i < inactiveToolNameList.size(); i++) {
                                    inactiveTempArray.add(inactiveToolNameList.get(i) + ", ID: " + inactiveToolIDList.get(i));
                                }

                                // Arraylist -> Array
                                String[] inactiveToolList = new String[inactiveTempArray.size()];
                                inactiveToolList = inactiveTempArray.toArray(inactiveToolList);
                                JOptionPane.showMessageDialog(null, inactiveToolList, "Admin Panel - View Tools (Inactive Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                            } // else no inactive tools found
                            else {
                                JOptionPane.showMessageDialog(null, "No Inactive Tools found", "Tool Master Panel - View Tools (No Inactive Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                            }
                            break;

                        // view all tools
                        case 2:
                            // check if inactive or active tools found
                            if (!api.toolNameList(false).isEmpty() || !api.toolNameList(true).isEmpty()) {
                                // Get inactive tool list
                                ArrayList<String> inactiveToolNameList = api.toolNameList(false);
                                ArrayList<Integer> inactiveToolIDList = api.toolIDList(false);

                                // Combine names with ids
                                ArrayList<String> tempArray = new ArrayList<>();
                                for (int i = 0; i < inactiveToolNameList.size(); i++) {
                                    tempArray.add(inactiveToolNameList.get(i) + ", ID: " + inactiveToolIDList.get(i) + ", Status: Inactive");
                                }

                                // Get active tool list
                                if (!api.toolNameList(true).isEmpty()) {
                                    // Get active tool list
                                    ArrayList<String> activeToolNameList = api.toolNameList(true);
                                    ArrayList<Integer> activeToolIDList = api.toolIDList(true);

                                    // Combine names with ids
                                    for (int i = 0; i < activeToolNameList.size(); i++) {
                                        tempArray.add(activeToolNameList.get(i) + ", ID: " + activeToolIDList.get(i) + ", Status: Active");
                                    }
                                    
                                    // Arraylist -> Array
                                    String[] fullToolList = new String[tempArray.size()];
                                    fullToolList = tempArray.toArray(fullToolList);
                                    
                                    // Display tools
                                    JOptionPane.showMessageDialog(null, fullToolList, "Admin Panel - View Tools (All Tools)", JOptionPane.INFORMATION_MESSAGE, null);
                                } // else no active tools found
                                else {
                                    JOptionPane.showMessageDialog(null, "No Active Tools found", "Tool Master Panel - View Tool (No Active Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                                }
                            } // else no inactive or active tools found
                            else {
                                JOptionPane.showMessageDialog(null, "No Tools found", "Tool Master Panel - View All Tools (No Tools Found)", JOptionPane.ERROR_MESSAGE, null);
                            }
                            break;
                        
                        // return to tool management    
                        case 3:
                            manageTools(api);
                    }
                }

                // Return to tool management panel once completed
                manageTools(api);

                break;

            case 3:
                // Back to admin panel
                admin(api);
                break;
        }

    }
}
