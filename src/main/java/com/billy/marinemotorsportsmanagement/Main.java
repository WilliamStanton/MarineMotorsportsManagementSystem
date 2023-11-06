package com.billy.marinemotorsportsmanagement;

import java.util.ArrayList;
import java.util.Arrays;
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
        int selection = JOptionPane.showOptionDialog(null, "Welcome to the Marine Motorsports Management System\nPlease select who you are:", "Marine Motorsports Management System", 0, 3, null, options, options[0]);
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
        // Check if any tools available to return
        if (api.toolIDList(true, false).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No tools are currently being borrowed", "Tool Master Panel - Return Tool", JOptionPane.ERROR_MESSAGE, null);
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
            String toolSelected = (String) JOptionPane.showInputDialog(null, "Please select a tool to return", "Tool Master Panel - Return Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
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
            JOptionPane.showMessageDialog(null, "No tools are currently able to be borrowed", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
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
                    // Check if any am students exist
                    if (api.studentIDList(true, "AM").isEmpty()) {
                        // Back to tool master if no students found
                        JOptionPane.showMessageDialog(null, "No AM Students currently exist", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
                        toolMaster(api);
                    }
                } else {
                    sessionSelected = "PM";
                    // check if any pm students exist
                    if (api.studentIDList(true, "PM").isEmpty()) {
                        // Back to tool master if no students found
                        JOptionPane.showMessageDialog(null, "No Students currently exist", "Tool Master Panel - Borrow Tool", JOptionPane.ERROR_MESSAGE, null);
                        toolMaster(api);
                    }
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
                String studentSelected = (String) JOptionPane.showInputDialog(null, "Please select the student borrowing a tool", "Tool Master Panel - Borrow Tool", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
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
                    String toolSelected = (String) JOptionPane.showInputDialog(null, "Please select a tool to borrow", "Tool Master Panel - Borrow Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
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
                    JOptionPane.showMessageDialog(null, "Available Tools \n\n" + allClassAvailable, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE, null);
                } // Else no tools available
                else {
                    JOptionPane.showMessageDialog(null, "No available tools found", "Tool Master Panel - Tool Report", JOptionPane.ERROR_MESSAGE, null);
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
            JOptionPane.showMessageDialog(null, "No unavailable tools found", "Tool Master Panel - Tool Report", JOptionPane.ERROR_MESSAGE, null);
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

        int unavailableSession = JOptionPane.showOptionDialog(null, "Please select the Class Session to view", "Tool Master Panel - Tool Report", 0, 3, null, sessionOptions, sessionOptions[0]);
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
                JOptionPane.showMessageDialog(null, "Unavailable Tools - AM Class\n\n" + amClassUnavailable, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE, null);

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
                JOptionPane.showMessageDialog(null, "Unavailable Tools - PM Class\n\n" + pmClassUnavailable, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE, null);

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
                JOptionPane.showMessageDialog(null, "Unavailable Tools - All Classes\n\n" + allClassUnavailable, "Tool Master Panel - Tool Report", JOptionPane.INFORMATION_MESSAGE, null);

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
                studentManagementAdmin(api);
                break;

            // Manage Tools
            case 1:
                toolManagementAdmin(api);
                break;

            // Return to main menu and logout
            case 2:
                api.logout();
                mainMenu(api);
                break;
        }
    }

    /**
     * The studentManagementAdmin method provides options for managing students and
     * is used only in the admin class
     *
     * @param api contains methods for management
     */
    public static void studentManagementAdmin(Tool api) {
        // Select Add or Remove or Back
        String[] studentOptions = {"Add Student", "Disable Student", "Re-enable Student", "View Students", "Back to Admin Panel"};
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
                    studentManagementAdmin(api);
                } // If No, return to student management panel
                else {
                    studentManagementAdmin(api);
                }
                break;

            case 1:
                // Disable student
                // if any students are enabled
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
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Disable Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully disabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been disabled. Error.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no enabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to disable.", "Admin Panel - Disable Student", JOptionPane.ERROR_MESSAGE, null);
                }
                // Return to student management panel once completed
                studentManagementAdmin(api);
                break;
                
            // Re-enable student    
            case 2:
                // if any students are disabled
                if (!api.studentNameList(false).isEmpty()) {
                    // Get all students names
                    ArrayList<String> studentNames = api.studentNameList(false);
                    // Get all student ids
                    ArrayList<Integer> studentIds = api.studentIDList(false);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < studentNames.size(); i++) {
                        tempArray.add(studentNames.get(i) + ", ID: " + studentIds.get(i));
                    }

                    // Arraylist -> Array
                    String[] studentList = new String[tempArray.size()];
                    studentList = tempArray.toArray(studentList);

                    // Select and re-enable student
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Select a Student", "Admin Panel - Re-enable Student", JOptionPane.QUESTION_MESSAGE, null, studentList, studentList[0]);
                    
                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully re-enabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Student", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Student has not been re-enabled. Error.", "Admin Panel - Re-enable Student", JOptionPane.ERROR_MESSAGE, null);
                        }
                    } // If No, return to student management panel
                    else {
                        studentManagementAdmin(api);
                    }
                } // if no disabled students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to re-enable.", "Admin Panel - Re-enable Student", JOptionPane.ERROR_MESSAGE, null);
                }
                // Return to student management panel once completed
                studentManagementAdmin(api);
                break;
                
            // View students panel    
            case 3:
                viewStudentsAdmin(api);
                break;

            case 4:
                // Return back to admin panel
                admin(api);
                break;
        }
    }
    
    /**
     * The viewStudentsAdmin method shows disabled/enabled, and all students
     * used only in the admin method
     * @param api 
     */
    public static void viewStudentsAdmin(Tool api) {
        // Check if any student available to borrow
        if (api.studentIDList(false).isEmpty() && api.studentIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No Students exist", "Admin Panel - View Students", JOptionPane.ERROR_MESSAGE, null);
            toolManagementAdmin(api);
        } // else continue 
        else {
            // initialize flags & arrays
            boolean disabledStudents = false;
            boolean enabledStudents = false;
            String[] enabledStudentList;
            String[] disabledStudentList;
            
            // check disabled students
            if (!api.studentNameList(false).isEmpty()) {
                // update flag
                disabledStudents = true;
                
                // build disabled students
                // Get disabled student list
                ArrayList<String> disabledStudentNameList = api.studentNameList(false);
                ArrayList<Integer> disabledStudentIDList = api.studentIDList(false);

                // Combine names with ids
                ArrayList<String> disabledTempArray = new ArrayList<>();
                for (int i = 0; i < disabledStudentNameList.size(); i++) {
                    disabledTempArray.add(disabledStudentNameList.get(i) + ", ID: " + disabledStudentIDList.get(i) + ", Session: " + api.getStudentSession(disabledStudentIDList.get(i)) + ", Status: Disabled");
                }

                // Arraylist -> Array
                disabledStudentList = new String[disabledTempArray.size()];
                disabledStudentList = disabledTempArray.toArray(disabledStudentList);
            } else {
                disabledStudentList = new String[0];
            }
            
            // check enabled students
            if (!api.studentNameList(true).isEmpty()) {
                // update flag
                enabledStudents = true;
                
                // build enabled students
                // Get enabled student list
                ArrayList<String> enabledStudentNameList = api.studentNameList(true);
                ArrayList<Integer> enabledStudentIDList = api.studentIDList(true);

                // Combine names with ids
                ArrayList<String> enabledTempArray = new ArrayList<>();
                for (int i = 0; i < enabledStudentNameList.size(); i++) {
                    enabledTempArray.add(enabledStudentNameList.get(i) + ", ID: " + enabledStudentIDList.get(i) + ", Session: " + api.getStudentSession(enabledStudentIDList.get(i)) + ", Status: Enabled");
                }

                // Arraylist -> Array
                enabledStudentList = new String[enabledTempArray.size()];
                enabledStudentList = enabledTempArray.toArray(enabledStudentList);
            } else {
                enabledStudentList = new String[0];
            }
            
            // Initialize Options
            String[] viewStudentOptions = {"View Enabled Students", "View Disabled Students", "View all Students", "Back"};
            int viewStudentSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Students", 0, 3, null, viewStudentOptions, viewStudentOptions[0]);
            switch (viewStudentSelection) {
                // view enabled Students
                case 0:
                    // check if enabled Students found
                    if (enabledStudents) {
                        JOptionPane.showMessageDialog(null, enabledStudentList, "Admin Panel - View Enabled Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Students exist", "Admin Panel - View Enabled Students", JOptionPane.ERROR_MESSAGE, null);
                    }

                    break;

                // view disabled Students
                case 1:
                    // check if disabled Students found
                    if (disabledStudents) {
                        JOptionPane.showMessageDialog(null, disabledStudentList, "Admin Panel - View Disabled Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no disabled students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Students exist", "Admin Panel Panel - View Disabled Students", JOptionPane.ERROR_MESSAGE, null);
                    }
                    break;

                // view all tools
                case 2:
                    // check if disabled or enabled Students found
                    if (disabledStudents || disabledStudents) {                            
                            // combine both arrays
                            String[] fullStudentList = Arrays.copyOf(enabledStudentList, enabledStudentList.length + disabledStudentList.length);
                            System.arraycopy(disabledStudentList, 0, fullStudentList, enabledStudentList.length, disabledStudentList.length);
                            
                            // Display Studentss
                            JOptionPane.showMessageDialog(null, fullStudentList, "Admin Panel - View All Students", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled or disabled Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No Students exist", "Tool Master Panel - View All Students", JOptionPane.ERROR_MESSAGE, null);
                    }
                    break;

                // return to Student management    
                case 3:
                    studentManagementAdmin(api);
            }
        }
        // Return to view students panel once completed
        viewStudentsAdmin(api);
    }

    /**
     * The toolManagementAdmin method provides options for managing students and is
     * used only in the admin method
     *
     * @param api contains methods for management
     */
    public static void toolManagementAdmin(Tool api) {
        // Select Add or Remove or Back
        String[] toolOptions = {"Add tool", "Disable tool", "Re-enable Tool", "View Tools", "Back"};
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
                    toolManagementAdmin(api);
                }

                // Return to tool management once completed
                toolManagementAdmin(api);
                break;

            case 1:
                // Disable a tool
                // Get enabled tool list
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

                    // Select and Disable tool
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Disable Tool", "Admin Panel - Disable Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully disabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Disable Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been disabled. Error.", "Admin Panel - Disable Tool", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool management once completed
                        toolManagementAdmin(api);
                        break;
                    } // If No, return to tool management
                    else {
                        toolManagementAdmin(api);
                    }
                } // if no enabled tools
                else {
                    JOptionPane.showMessageDialog(null, "There are no tools to disable.", "Admin Panel - Disable Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                toolManagementAdmin(api);
                break;

            // Re-enable tool    
            case 2:
                // Get disabled tool list
                if (!api.toolNameList(false).isEmpty()) {
                    ArrayList<String> toolNameList = api.toolNameList(false);
                    ArrayList<Integer> toolIDList = api.toolIDList(false);

                    // Combine names with ids
                    ArrayList<String> tempArray = new ArrayList<>();
                    for (int i = 0; i < toolNameList.size(); i++) {
                        tempArray.add(toolNameList.get(i) + ", ID: " + toolIDList.get(i));
                    }

                    // Arraylist -> Array
                    String[] toolList = new String[tempArray.size()];
                    toolList = tempArray.toArray(toolList);

                    // Select and re-enable tool
                    String toolToDisable = (String) JOptionPane.showInputDialog(null, "Re-enable Tool", "Admin Panel - Re-enable Tool", JOptionPane.QUESTION_MESSAGE, null, toolList, toolList[0]);
                    // If yes
                    if (toolToDisable != null) {
                        // Strip String to only ID
                        String toolID = toolToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableTool(Integer.parseInt(toolID))) {
                            String successRemoveMessage = "Tool successfully re-enabled: " + toolToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Admin Panel - Re-enable Tool", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been re-enabled. Error.", "Admin Panel - Re-enable Tool", JOptionPane.ERROR_MESSAGE, null);
                        }

                        // Return to tool management once completed
                        toolManagementAdmin(api);
                        break;
                    } // If no, return to tool management
                    else {
                        toolManagementAdmin(api);
                    }
                } // If no enabled tools
                else {
                    JOptionPane.showMessageDialog(null, "There are no tools to re-enable.", "Admin Panel - Re-enable Tool", JOptionPane.ERROR_MESSAGE, null);
                }

                // Return to tool management panel once completed
                toolManagementAdmin(api);
                break;
                
            // View tools    
            case 3:
                viewToolsAdmin(api);
                break;

            case 4:
                // Back to admin panel
                admin(api);
                break;
        }
    }
    
    /**
     * The viewToolsAdmin method shows disabled/enabled tools, and all tools
     * used only in the admin method
     * 
     * @param api used only in the admin class
     */
    public static void viewToolsAdmin(Tool api) {
        // Check if any tools available to borrow
        if (api.toolIDList(false).isEmpty() && api.toolIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No Tools exist", "Admin Panel - View Tools", JOptionPane.ERROR_MESSAGE, null);
            toolManagementAdmin(api);
        } // else continue 
        else {
            // initialize flags & arrays
            boolean disabledTools = false;
            boolean enabledTools = false;
            String[] enabledToolList;
            String[] disabledToolList;
            
            // check disabled tools
            if (!api.toolNameList(false).isEmpty()) {
                // update flag
                disabledTools = true;
                
                // build disabled tools
                // Get disabled tool list
                ArrayList<String> disabledToolNameList = api.toolNameList(false);
                ArrayList<Integer> disabledToolIDList = api.toolIDList(false);

                // Combine names with ids
                ArrayList<String> disabledTempArray = new ArrayList<>();
                for (int i = 0; i < disabledToolNameList.size(); i++) {
                    disabledTempArray.add(disabledToolNameList.get(i) + ", ID: " + disabledToolIDList.get(i) + ", Status: Disabled");
                }

                // Arraylist -> Array
                disabledToolList = new String[disabledTempArray.size()];
                disabledToolList = disabledTempArray.toArray(disabledToolList);
            } else {
                disabledToolList = new String[0];
            }
            
            // check enabled tools
            if (!api.toolNameList(true).isEmpty()) {
                // update flag
                enabledTools = true;
                
                // build enabled tools
                // Get enabled tool list
                ArrayList<String> enabledToolNameList = api.toolNameList(true);
                ArrayList<Integer> enabledToolIDList = api.toolIDList(true);

                // Combine names with ids
                ArrayList<String> enabledTempArray = new ArrayList<>();
                for (int i = 0; i < enabledToolNameList.size(); i++) {
                    enabledTempArray.add(enabledToolNameList.get(i) + ", ID: " + enabledToolIDList.get(i) + ", Status: Enabled");
                }

                // Arraylist -> Array
                enabledToolList = new String[enabledTempArray.size()];
                enabledToolList = enabledTempArray.toArray(enabledToolList);
            } else {
                enabledToolList = new String[0];
            }
            
            // Initialize Options
            String[] viewToolOptions = {"View Enabled Tools", "View Disabled Tools", "View all Tools", "Back"};
            int viewToolSelection = (int) JOptionPane.showOptionDialog(null, "Please select an option", "Admin Panel - View Tools", 0, 3, null, viewToolOptions, viewToolOptions[0]);
            switch (viewToolSelection) {
                // view enabled tools
                case 0:
                    // check if enabled tools found
                    if (enabledTools) {
                        JOptionPane.showMessageDialog(null, enabledToolList, "Admin Panel - View Enabled Tools", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no enabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Enabled Tools exist", "Admin Panel - View Enabled Tools", JOptionPane.ERROR_MESSAGE, null);
                    }

                    break;

                // view disabled tools
                case 1:
                    // check if disabled tools found
                    if (disabledTools) {
                        JOptionPane.showMessageDialog(null, disabledToolList, "Admin Panel - View Disabled Tools", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no disabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Disabled Tools exist", "Admin Panel Panel - View Disabled Tools", JOptionPane.ERROR_MESSAGE, null);
                    }
                    break;

                // view all tools
                case 2:
                    // check if disabled or enabled tools found
                    if (disabledTools || enabledTools) {                            
                            // combine both arrays
                            String[] fullToolList = Arrays.copyOf(enabledToolList, enabledToolList.length + disabledToolList.length);
                            System.arraycopy(disabledToolList, 0, fullToolList, enabledToolList.length, disabledToolList.length);
                            
                            // Display tools
                            JOptionPane.showMessageDialog(null, fullToolList, "Admin Panel - View All Tools", JOptionPane.INFORMATION_MESSAGE, null);
                    } // else no disabled or enabled tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No Tools exist", "Tool Master Panel - View All Tools", JOptionPane.ERROR_MESSAGE, null);
                    }
                    break;

                // return to tool management    
                case 3:
                    toolManagementAdmin(api);
            }
        }
        // Return to view tools panel once completed
        viewToolsAdmin(api);
    }
}
