package com.billy.marinemotorsportsmanagement;

import com.billy.marinemotorsportsmanagement.JComponents.JButton.Button;
import com.billy.marinemotorsportsmanagement.JComponents.JButton.ButtonArray;
import com.billy.marinemotorsportsmanagement.JComponents.JLabel.Description;
import com.billy.marinemotorsportsmanagement.JComponents.JLabel.Field;
import com.billy.marinemotorsportsmanagement.JComponents.JLabel.TextPrompt;
import com.billy.marinemotorsportsmanagement.JComponents.JLabel.Title;
import com.billy.marinemotorsportsmanagement.JComponents.JTable.*;
import com.billy.marinemotorsportsmanagement.JComponents.JTextArea.Scroll;
import com.billy.marinemotorsportsmanagement.JComponents.JTextArea.TextArea;
import com.billy.marinemotorsportsmanagement.Model.Category;
import com.billy.marinemotorsportsmanagement.Model.Tool;
import com.billy.marinemotorsportsmanagement.Services.CategoryService;
import com.billy.marinemotorsportsmanagement.Services.ToolService;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * This Main Class operates the Marine Motorsports Management Program
 *
 * @author Billy Stanton
 * @version 2.0
 * @since 11/1/23
 */
public class Main {
    // Init api
    private static final CategoryService api = new CategoryService();

    // Init vars for Tool Scan
    public static int in = 0, out = 0, studentID = 0;
    public static boolean error = false;

    /**
     * The main method starts and configures the application
     */
    public static void main(String[] args) {

        // Ensure Database Connection before continuing
        if (!api.databaseConnection()) {
            JOptionPane.showMessageDialog(null, "Database Error\nNot Connected, please contact support");
            api.exit();
        }

        // Configure UI (student)
        uiConfig(false);

        // Load main menu
        mainMenu();

        // Exit
        api.exit();
    }

    /**
     * The uiConfig method configures the way that the UI is displayed
     *
     * @param admin true if admin config must be loaded, else student config
     * loaded
     */
    public static void uiConfig(boolean admin) {

        // JOptionPane
        // Fonts
        UIManager.put("OptionPane.messageForeground", Color.white);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 32));
        UIManager.put("OptionPane.textFieldFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("OptionPane.listFont", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.BOLD, 32));

        // JOptionPane Size
        UIManager.put("OptionPane.border", new EmptyBorder(75, 100, 100, 100));
        UIManager.put("OptionPane.background", new Color(66, 66, 100));
        UIManager.put("Panel.background", new Color(66, 66, 100));

        // Btns
        UIManager.put("OptionPane.cancelButtonText", "Back");
        UIManager.put("OptionPane.okButtonText", "Continue");
        UIManager.put("Button.background", new Color(97, 97, 114));
        UIManager.put("Button.foreground", Color.white);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));

        // Text Field
        // Fonts
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 28));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 28));

        // Admin Config
        if (admin) {
            UIManager.put("Button.border", new EmptyBorder(20, 20, 20, 20));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 20));
        } // Student Config
        else {
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 26));
            UIManager.put("Button.border", new EmptyBorder(20, 25, 20, 25));
        }
    }

    /**
     * The mainMenu method displays the options Student, Admin, or exit.
     */
    public static void mainMenu() {
        // Configure UI (default, student)
        uiConfig(false);

        // Initialize menu options
        String[] options = {"Tool Master", "Teacher"};

        // Login Selection Display
        JLabel title = new Title("Welcome to the Marine Motorsports Management System");
        JLabel dev = new Description("Developed by William Stanton", true);

        Object[] loginSelectionTitle = {
                title,
                dev,
        };

        int selection = JOptionPane.showOptionDialog(null, loginSelectionTitle, "Marine Motorsports Management System", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            // load tool master panel
            case 0:
                toolMaster(null);
                break;

            // load Teacher Panel
            case 1:
                // Configure UI (teacher)
                uiConfig(true);
                admin();
                break;

            // exit application
            default:
                // Login Field
                // JLabel exitMessage
                JLabel exitMessage = new Title("Please enter administrator credentials to exit the application.");

                // JTextField & JLabel Username (Username field for login)
                JTextField username = new JTextField();
                JLabel usernameTitle = new Field("Username");

                // JPasswordField & JLabel Password (Password field for login)
                JPasswordField password = new JPasswordField();
                JLabel passwordTitle = new Field("Password");

                // Display Array
                Object[] loginField = {
                        exitMessage,
                        usernameTitle, username,
                        passwordTitle, password
                };

                // Attempt login till session status is active
                while (!api.sessionStatus()) {
                    // Login Dialog
                    int option = JOptionPane.showConfirmDialog(null, loginField, "Exit Application", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // If Ok, check login
                    if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {
                        api.exit();
                    } // If no, exit to menu
                    else if (option == JOptionPane.CANCEL_OPTION) {
                        mainMenu();
                    } // Else, login was incorrect
                    else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials, please try again", "Teacher Login", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
        }
    }

    /**
     * The toolMaster method controls various different functions that are
     * utilized by the toolMaster
     *
     * @param session null if no session chosen, else pass session for method
     */
    public static void toolMaster(String session) {
        // Select Session
        if (session == null) {
            String[] sessions = {"AM", "PM", "Back"};
            // Session Choice Display
            JLabel sessionTitle = new Title("Tool Master");
            JLabel sessionDescription = new Description("Select the current session");
            Object[] display = {
                    sessionTitle,
                    sessionDescription
            };

            // Get selection
            int sessionChoice = JOptionPane.showOptionDialog(null, display, "Tool Master Panel - Select Session", 0, JOptionPane.PLAIN_MESSAGE, null, sessions, sessions[0]);

            switch (sessionChoice) {
                case 0 -> session = "AM";
                case 1 -> session = "PM";
                default -> mainMenu();
            }
        }

        // Initialize menu options
        String[] options = {"Scan Tools", "View Borrowed Tools", "Log out of " + session + " Session"};

        // Tool Master selection display
        JLabel toolMasterTitle = new Title("Tool Master | " + session + " Session");
        JLabel toolMasterDescription = new Description("What would you like to do?");
        Object[] toolMasterSelection = {
                toolMasterTitle,
                toolMasterDescription
        };

        int selection = JOptionPane.showOptionDialog(null, toolMasterSelection, "Tool Master Panel", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            case 0 -> quickScan(session);
            case 1 -> borrowedTools(session);
            default -> mainMenu();
        }
    }

    /**
     * The quickScan method allows the tool master to quickly return or borrow a
     * tool by automatically identifying if it should be borrowed or returned
     *
     * @param session current session (AM/PM)
     */
    public static void quickScan(String session) {
        // Build Student Name Array
        String[] studentList = new String[api.studentNameList(true, session).size()];
        studentList = api.studentNameList(true, session).toArray(studentList);

        // Edit Student Name Array to only contain first letter of last name
        for (int i = 0; i < studentList.length; i++) {
            // Init var
            boolean similarity = false;

            // Check current name (i) against all other names to see if any has
            // the same last name. If so, add last name letter
            for (int j = 0; j < studentList.length; j++) {
                if (studentList[i].replaceAll(" .*", "").equals(studentList[j].replaceAll(" .*", "")) && i != j) {
                    similarity = true;
                    break;
                }
            }

            // Complete changes if similar (same first name) or not similar (different first name)
            if (similarity) {
                studentList[i] = studentList[i].replaceAll(" .*", " ") + studentList[i].replaceAll("^.*?\\s", "").charAt(0) + ".";
            } else {
                studentList[i] = studentList[i].replaceAll(" .*", " ");
            }
        }

        // Build Student ID Array
        Integer[] studentIDList = new Integer[studentList.length];
        studentIDList = api.studentIDList(true, session).toArray(studentIDList);

        // Re-arrange both Arrays in alphabetical order
        String temp;
        int numTemp;
        // re-arrange array
        for (int i = 0; i < studentList.length; i++) {
            for (int j = i + 1; j < studentList.length; j++) {
                if (studentList[i].compareTo(studentList[j]) > 0) {
                    // handle student names
                    temp = studentList[i];
                    studentList[i] = studentList[j];
                    studentList[j] = temp;

                    // handle student ids
                    numTemp = studentIDList[i];
                    studentIDList[i] = studentIDList[j];
                    studentIDList[j] = numTemp;
                }
            }
        }

        // Run quick scan till exit
        while (true) {
            // Update vars
            studentID = 0;
            in = 0;
            out = 0;
            error = false;

            // Get Chosen Student
            // JPanel Student Panel (displays all students as buttons)
            JPanel studentPanel = new JPanel(new GridLayout(6, 5, 6, 6));

            // JLabel Student Panel Title (display title)
            JLabel studentPanelTitle = new Title("Scan Tools");
            JLabel studentPanelDesc = new Description("Please select a Student | " + session + " Session");

            // Chosen Student ActionListener
            ActionListener chooseStudent = (ActionEvent ae) -> {
                // Get index
                JButton btn = (JButton) ae.getSource();
                studentID = (int) btn.getClientProperty("index");
                JOptionPane.getRootFrame().dispose();
            };

            // Add all buttons
            JButton[] buttonArray = new JButton[studentList.length];
            for (int i = 0; i < studentList.length; i++) {
                buttonArray[i] = new ButtonArray(studentList[i]);
                buttonArray[i].putClientProperty("index", i);
                buttonArray[i].addActionListener(chooseStudent);
                studentPanel.add(buttonArray[i]);
            }

            // Choose Student Display Array
            Object[] chooseStudentDisplay = {
                    studentPanelTitle,
                    studentPanelDesc,
                    studentPanel
            };

            // Display Student Selection & Handle Exit
            int exit = JOptionPane.showOptionDialog(null, chooseStudentDisplay, "Scan Tools", 0, -1, null, new String[]{"Back"}, 0);

            // Handle Exit
            if (exit == 0) {
                toolMaster(session);
            }

            // Match StudentID with Student Name Selected
            studentID = studentIDList[studentID];

            // Initialize Tool Scan GUI
            // JLabel Scan Title (display user tools being scanned for)
            JLabel scanTitle = new Title("Scanning tools for: " + api.getStudentName(studentID));

            // JLabel Scan Stats (borrow/return count)
            JLabel scanStats = new Description("Tools Borrowed: " + out + " | Tools Returned: " + in);

            // JTextArea Tools Scanned (display tools scanned in/out)
            JTextArea toolsScanned = new TextArea("waiting for tools to be scanned...\n");
            toolsScanned.setFocusable(false);
            toolsScanned.getCaret().setVisible(false);

            // JScrollPane scroll (display JTextArea with scrollbar)
            JScrollPane scroll = new Scroll(toolsScanned);

            // JLabel Title for borrowing tools section
            JLabel borrowTitle = new Title("Borrow Tools");
            borrowTitle.setForeground(new Color(97, 97, 114));

            // JTextField search field (Text box to read input)
            JTextField searchField = new JTextField() {
                // Set focus on TextField
                public void addNotify() {
                    super.addNotify();
                    requestFocus();
                }
            };

            searchField.setPreferredSize(new Dimension(700, 40));

            // Text Prompt for searchField
            TextPrompt searchFieldPrompt = new TextPrompt("Enter tool name or scan a tool", searchField);
            searchFieldPrompt.changeAlpha(0.5f);

            // JButton search button (to search/scan tool)
            JButton searchButton = new Button("Search Tool");

            // Display searchField + searchButton next to each other
            JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
            searchPanel.add(borrowTitle, BorderLayout.NORTH);
            searchPanel.add(searchField, BorderLayout.WEST);
            searchPanel.add(searchButton, BorderLayout.EAST);
            searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            searchPanel.setBackground(Color.LIGHT_GRAY);

            // Return Tools Button
            JButton returnTools = new Button("Return Tools");

            // Finish Button
            JButton finish = new Button("Finish Scanning");

            // Other options panel
            JPanel otherPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            otherPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            JLabel otherPanelTitle = new Title("Other Options");
            otherPanelTitle.setForeground(new Color(97, 97, 114));
            otherPanel.setBackground(Color.LIGHT_GRAY);
            otherPanel.add(otherPanelTitle);
            otherPanel.add(returnTools);
            otherPanel.add(finish);

            // Search if enter is pressed
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                        searchButton.doClick();
                }
            });

            // Enter Borrow Tools Panel by Button
            searchButton.addActionListener(ae -> {
                // TOOL SCANNED (MMS-)
                if (searchField.getText().toUpperCase().contains("MMS-")) {
                    // Parse Tool ID (remove MMS-)
                    String scannedTool = searchField.getText();
                    try {
                        // clear textarea default text
                        if (toolsScanned.getText().contains("waiting for tools to be scanned..."))
                            toolsScanned.setText("");
                        scannedTool = scannedTool.replaceAll("[^0-9]+", "");
                        int toolID = Integer.parseInt(scannedTool);
                        // Ensure scanned tool is valid & active
                        if (api.toolStatus(toolID)) {
                            // If tool isn't available, return tool
                            if (!api.toolAvailability(toolID)) {
                                toolsScanned.append("Borrow Error, Tool Unavailable: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                error = true;
                            } // Else if tool is available, borrow tool
                            else {
                                // Borrow success
                                if (api.borrowTool(studentID, toolID)) {
                                    toolsScanned.append("Borrowed: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                    out++;
                                } // Borrow error
                                else {
                                    toolsScanned.append("Borrow Error: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                                    error = true;
                                }
                            }
                        } // Else, check if tool doesn't exist or is just inactive
                        else {
                            error = true;
                            // Tool doesn't exist
                            if (api.getToolName(toolID) == null) {
                                toolsScanned.append("Unknown Tool Error, ID: " + toolID + "\n");
                            } // Tool exists, but is inactive
                            else if (!api.toolStatus(toolID)){
                                toolsScanned.append("Inactive Tool Error: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                            } // Else unknown error
                            else {
                                toolsScanned.append("Unknown Scan Error\n");
                                error = true;
                            }
                        }
                    } catch (Exception e) {
                        searchField.setText("");
                    }

                    // Update Stats
                    scanStats.setText("Tools Borrowed: " + out + " | Tools Returned: " + in);
                }

                // Search Input
                else {
                    // Title
                    JLabel borrowToolTitle = new Title("Please select the tool to borrow");
                    JLabel borrowToolDesc = new Description("Please select your tool");
                    JPanel borrowToolPanel = new JPanel(new GridLayout(0, 2, 6, 6));
                    JScrollPane borrowTool = new Scroll(borrowToolPanel);
                    borrowTool.getVerticalScrollBar().setUnitIncrement(30); // fix scroll speed
                    ArrayList<Integer> foundTools = new ArrayList<>();
                    JButton returnBtn = new Button("Back to Scanning");

                    // Get all tools
                    ArrayList<Integer> toolIDList = api.toolIDList(true, true);
                    ArrayList<String> toolNameList = api.toolNameList(true, true);

                    // Search for tools if searching
                    if (!searchField.getText().isEmpty() && searchField.getText().length() < 45 && !searchField.getText().toUpperCase().contains("MMSC-") && !searchField.getText().toUpperCase().contains("https")) {
                        // Split Words
                        var searchFieldKeywords = searchField.getText().toLowerCase().split(" ");
                        for (int i = 0; i < toolNameList.size(); i++) {
                            boolean flag = true;
                            // Check if tool name contains all the keywords
                            for (int j = 0; j < searchFieldKeywords.length; j++) {
                                // Check if matches keywords
                                if (!toolNameList.get(i).toLowerCase().contains(searchFieldKeywords[j])) {
                                    flag = false; // if no match, set flag false
                                    break; // break out of loop
                                }
                            }

                            // Add if match
                            if (flag)
                                foundTools.add(toolIDList.get(i));
                        }
                        if (!foundTools.isEmpty())
                            borrowToolDesc.setText(foundTools.size() + " AVAILABLE tools found for: " + "\"" + searchField.getText() + "\"");
                        else {
                            borrowToolTitle.setText("No tools found");
                            borrowToolDesc.setText("\"" + searchField.getText() + "\" could not be found");
                        }
                    } else if (!searchField.getText().toUpperCase().contains("MMSC-")){
                        borrowToolTitle.setText("No tools found");
                        if (searchField.getText().length() >= 45)
                            borrowToolDesc.setText("Too many characters entered, please shorten your search");
                        else
                            borrowToolDesc.setText("Search bar was left incomplete");
                    }

                    // Search for category if begins with MMSC-
                    if (searchField.getText().toUpperCase().contains("MMSC-")) {
                        // Parse Category ID (remove MMSC-)
                        String scannedCategory = searchField.getText();
                        try {
                            scannedCategory = scannedCategory.replaceAll("[^0-9]+", "");
                            int categoryID = Integer.parseInt(scannedCategory);
                            var category = api.getCategory(categoryID);

                            // if category exists
                            if (category != null) {
                                // Add all tools to found tools list
                                for (int i = 0; i < category.getTools().size(); i++) {
                                    var toolID = category.getTools().get(i).getId();
                                    // Ensure tool is active before adding
                                    if (api.toolStatus(toolID) && api.toolAvailability(toolID))
                                        foundTools.add(toolID);
                                }

                                // If tools found
                                if (!foundTools.isEmpty())
                                    borrowToolDesc.setText(foundTools.size() + " AVAILABLE tools found in " + "\"" + category.getCategoryName() + "\""); // set text
                                    // If category is inactive
                                else if (!category.isStatus()) {
                                    borrowToolTitle.setText("Category is inactive");
                                    borrowToolDesc.setText("\"" + category.getCategoryName() + "\" is currently inactive and cannot be used");
                                }
                                // If no available tools within category
                                else {
                                    borrowToolTitle.setText("No tools found");
                                    borrowToolDesc.setText("All tools in \"" + category.getCategoryName() + "\" are currently unavailable");
                                }
                            }
                            // if category does not exist
                            else {
                                borrowToolTitle.setText("Category not found");
                                borrowToolDesc.setText("Please try again or scan another category");
                            }
                        } catch (Exception e) {
                            searchField.setText("");
                        }
                        // Update Stats
                        scanStats.setText("Tools Borrowed: " + out + " | Tools Returned: " + in);
                    }

                    // Chosen Tool ActionListener
                    ActionListener chooseTool = (ActionEvent ae2) -> {
                        // Get index
                        JButton btn = (JButton) ae2.getSource();
                        int toolID = (int) btn.getClientProperty("index");
                        boolean success = api.borrowTool(studentID, toolID);
                        if (success) {
                            // clear textarea default text
                            if (toolsScanned.getText().contains("waiting for tools to be scanned..."))
                                toolsScanned.setText("");
                            toolsScanned.append("Borrowed: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                            out++;
                            returnBtn.doClick();
                        }
                        else
                            toolsScanned.append("Error, tool not available: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                    };

                    // Add all tools as buttons
                    JButton[] buttonArray1 = new ButtonArray[foundTools.size()];
                    for (int i = 0; i < buttonArray1.length; i++) {
                        String toolName = api.getToolName(foundTools.get(i));
                        buttonArray1[i] = new ButtonArray(toolName);
                        buttonArray1[i].putClientProperty("index", foundTools.get(i));
                        buttonArray1[i].addActionListener(chooseTool);
                        borrowToolPanel.add(buttonArray1[i]);

                        // Make 1 col instead of 2 if tool name is too long to prevent unnecessary scrolling
                        if (toolName.length() > 40)
                            borrowToolPanel.setLayout(new GridLayout(0, 1, 6, 6));
                    }

                    // Action Listener for exiting borrow tools
                    returnBtn.addActionListener(e -> {
                        scanStats.setText("Tools Borrowed: " + out + " | Tools Returned: " + in);
                        Window w = SwingUtilities.getWindowAncestor(returnBtn);
                        if (w != null) {
                            w.dispose(); // dispose window
                            searchField.addNotify(); // focus on search field
                        }
                    });

                    // Display Object
                    Object[] borrowToolsDisplay = {
                            borrowToolTitle,
                            borrowToolDesc,
                            borrowTool,
                            returnBtn
                    };

                    if (foundTools.isEmpty())
                        borrowToolsDisplay = new Object[]{borrowToolTitle, borrowToolDesc, returnBtn};

                    // Display Borrow Tools
                    JOptionPane.showOptionDialog(null, borrowToolsDisplay, "Borrow Tools", 0, -1, null, new Object[]{}, null);
                }

                // Clear field
                searchField.setText("");
            });

            // Enter Return Tools Panel by Button
            returnTools.addActionListener(ae -> {
                // JPanel Return Tool Panel (displays all tools as buttons)
                JPanel returnToolPanel = new JPanel(new GridLayout(6, 5, 6, 6));
                JButton returnBtn = new Button("Back to Scanning");
                JButton returnAllBtn = new Button("Return All");

                // Title
                JLabel title = new Title("Return Tools for " + api.getStudentName(studentID));
                JLabel description = new Description("");
                var toolCount = api.getStudentToolIDList(studentID).size();
                if (toolCount > 1) {
                    description.setText("Click tool to return | Borrowing " + toolCount + " tools");
                } else if (toolCount == 0){
                    description.setText("This student currently has no tools out!");
                } else {
                    description.setText("Borrowing one tool");
                }

                // Chosen Tool ActionListener
                ActionListener chooseTool = (ActionEvent ae2) -> {
                    // Get index
                    JButton btn = (JButton) ae2.getSource();
                    int toolID = (int) btn.getClientProperty("index");
                    boolean success = api.returnTool(toolID, studentID);
                    if (success) {
                        toolsScanned.append("Returned: " + api.getToolName(toolID) + ", ID: " + toolID + "\n");
                        in++;
                        returnBtn.doClick();
                        returnTools.doClick();
                    }
                };

                // Add all tools as buttons
                ArrayList<Integer> toolIDList = api.getStudentToolIDList(studentID);
                JButton[] buttonArray1 = new ButtonArray[toolIDList.size()];
                for (int i = 0; i < buttonArray1.length; i++) {
                    buttonArray1[i] = new ButtonArray(api.getToolName(toolIDList.get(i)));
                    buttonArray1[i].putClientProperty("index", toolIDList.get(i));
                    buttonArray1[i].addActionListener(chooseTool);
                    returnToolPanel.add(buttonArray1[i]);
                }

                // Action Listener for returning all tools
                returnAllBtn.addActionListener(e -> {
                    var toolsOut = api.getStudentToolIDList(studentID); // get all tools student has
                    // return all tools student has out
                    for (int i = 0; i < toolsOut.size(); i++) {
                        if (api.returnTool(toolsOut.get(i), studentID)) {
                            // clear textarea default text
                            if (toolsScanned.getText().contains("waiting for tools to be scanned..."))
                                toolsScanned.setText("");
                            toolsScanned.append("Returned: " + api.getToolName(toolsOut.get(i)) + ", ID: " + toolsOut.get(i) + "\n");
                            in++;
                        } else {
                            toolsScanned.append("Return Error: " + api.getToolName(toolsOut.get(i)) + ", ID: " + toolsOut.get(i) + "\n");
                            error = true;
                        }
                    }

                    returnBtn.doClick(); // return back to scanner
                });

                // Action Listener for exiting return tools
                returnBtn.addActionListener(e -> {
                    scanStats.setText("Tools Borrowed: " + out + " | Tools Returned: " + in);
                    Window w = SwingUtilities.getWindowAncestor(returnBtn);
                    if (w != null) {
                        w.dispose(); // dispose window
                        searchField.addNotify(); // focus on search field
                    }
                });

                Object[] returnToolsDisplay = {
                        title, description, returnToolPanel, returnAllBtn, returnBtn
                };

                // if no tools exist
                if (api.getStudentToolIDList(studentID).isEmpty())
                    returnToolsDisplay = new Object[]{title, description, returnToolPanel, returnBtn};

                // Display Return Tools
                JOptionPane.showOptionDialog(null, returnToolsDisplay, "Return Tools", 0, -1, null, new Object[]{}, null);
            });

            // Add Finish Button Listener
            finish.addActionListener(ae -> {
                // Check if any errors occurred
                if (error) {
                    // Ask user if they want to continue scanning or exit
                    // JLabel Error Titles (Title containing info/student with errors)
                    JLabel errorTitle = new Title("One or more errors occurred scanning tools for " + api.getStudentName(studentID));
                    JLabel errorTitle2 = new Description("\nPlease continue if all the tools were scanned, otherwise go back to scanning");

                    // Error Display Array
                    Object[] errorDisplay = {
                            errorTitle, // Title Containing info/student with errors
                            errorTitle2,// Title Containing info/student with errors
                            scroll // Tools Scanned (display tools scanned in/out)
                    };

                    // Display Error Message
                    int cont = JOptionPane.showConfirmDialog(null, errorDisplay, "Scan Tools Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // If OK, exit tool scan successfully
                    if (cont == JOptionPane.OK_OPTION) {
                        JOptionPane.getRootFrame().dispose();
                    }
                } // Else if no errors, successfully exit tool scan
                else {
                    JOptionPane.getRootFrame().dispose();
                }
            });

            // Tool Scan Display Array
            Object[] toolScanDisplay = {
                    scanTitle, // Scan Title (display user tools being scanned for)
                    scanStats, // Scan Stats (borrow/return count)
                    scroll, // Tools Scanned (display tools scanned in/out)
                    searchPanel, // Search panel (field+btn)
                    otherPanel, // Return Tools (easily return tools for selected student) & Finish Scanning
            };

            // Start Student Scan Session
            JOptionPane.showOptionDialog(null, toolScanDisplay, "Scan Tools", 0, -1, null, new Object[]{}, null);
        }
    }

    /**
     * The Tool Lookup method looks up the status of a tool by the given id (if
     * borrowed, if so by who, otherwise it is available)
     */
    public static void toolLookup(int toolID, JTable allBorrowedTools) {
        // JLabel Tool Lookup Title
        JLabel toolLookupTitle = new Title();

        // JLabel Tool Inventory Title
        JLabel toolInventoryTitle = new Description();

        // JTextArea Text Area (display specified tool data)
        JTable toolBorrowers = new JTable();

        // JScrollPane scroll (display JTextArea with scrollbar)
        JScrollPane scroll = new Scroll(toolBorrowers, 500, 500);

        // (Optional) error message
        JLabel toolLookupError = new Title("");

        // JOptionPane display array
        Object[] display = {
                toolLookupError
        };

        // Check if tool is enabled
        if (api.toolStatus(toolID)) {
            // Check if there is *any* of the tool out, if so, build list of names
            var borrowerIds = api.getToolBorrowerIDS(toolID);
            String[][] borrowerData = new String[borrowerIds.size()][4];
            if (!borrowerIds.isEmpty()) {
                for (int i = 0; i < borrowerIds.size(); i++) {
                    borrowerData[i][0] = api.getToolName(toolID);
                    borrowerData[i][1] = String.valueOf(toolID);
                    borrowerData[i][2] = String.valueOf(api.getStudentName(borrowerIds.get(i)));
                    borrowerData[i][3] = String.valueOf(borrowerIds.get(i));
                }
                var tools = new ToolLookup(borrowerData, "ToolID");
                scroll = new Scroll(tools);

                // Mouse listener for table row (tool return)
                tools.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent mouseEvent) {
                        JTable table =(JTable) mouseEvent.getSource();
                        // Return tool if double click
                        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                            // return tool and go back to main table
                            if (api.returnTool(Integer.parseInt((String) tools.getModel().getValueAt(table.getSelectedRow(), 1)), Integer.parseInt((String) tools.getModel().getValueAt(table.getSelectedRow(), 3))) && SwingUtilities.getWindowAncestor(table) != null) {
                                SwingUtilities.getWindowAncestor(table).dispose(); // dispose window
                                // Update row quantities
                                allBorrowedTools.setValueAt(String.valueOf(Integer.parseInt((String) allBorrowedTools.getValueAt(allBorrowedTools.getSelectedRow(), 2))-1), allBorrowedTools.getSelectedRow(), 2);
                                allBorrowedTools.setValueAt(String.valueOf(Integer.parseInt((String) allBorrowedTools.getValueAt(allBorrowedTools.getSelectedRow(), 1))+1), allBorrowedTools.getSelectedRow(), 1);
                                if (Integer.parseInt((String) allBorrowedTools.getValueAt(allBorrowedTools.getSelectedRow(), 2)) == 0) {
                                    ((DefaultTableModel) allBorrowedTools.getModel()).removeRow(allBorrowedTools.getSelectedRow());
                                }
                                allBorrowedTools.repaint(); // reload all borrowed tools
                            }
                        }
                    }
                });
            }
            if (api.toolAvailability(toolID) && !borrowerIds.isEmpty()) {
                // Set display
                toolLookupTitle.setText(api.getToolName(toolID) + " is currently available to borrow");
                toolInventoryTitle.setText("Borrowed: " + api.getToolAvailablityQuantity(toolID, false) + " | Inventory: " + api.getToolAvailablityQuantity(toolID, true));

                display = new Object[]{
                        toolLookupTitle,
                        toolInventoryTitle,
                        scroll
                };
            } else if (api.toolAvailability(toolID) && borrowerIds.isEmpty()) {
                toolLookupTitle.setText(api.getToolName(toolID) + " (ID: " + toolID + ") is currently available to borrow");
                toolInventoryTitle.setText("Borrowers: 0 | Inventory: " + api.getToolQuantity(toolID));

                display = new Object[]{
                        toolLookupTitle,
                        toolInventoryTitle,
                };
            }
            else {
                toolInventoryTitle.setText("Borrowed: " + api.getToolAvailablityQuantity(toolID, false) + " | Inventory: " + api.getToolAvailablityQuantity(toolID, true));
                display = new Object[] {
                        toolLookupTitle,
                        toolInventoryTitle,
                        scroll
                };
                toolLookupTitle.setText(api.getToolName(toolID) + " is currently unavailable to borrow");
            }
        }
        else if (!api.toolStatus(toolID) && api.getToolName(toolID) != null) {
            // Set error
            toolLookupError.setText(api.getToolName(toolID) + " (ID: " + toolID + ") is currently disabled");
        } // Else tool does not exist
        else {
            // Set error
            toolLookupError.setText("The tool by (ID: " + toolID + ") does not exist");
        }

        JOptionPane.showMessageDialog(null, display, "Tool Lookup", -1);
    }

    /**
     * The borrowedTools method provides the methods for viewing the various
     * unavailable tool reports
     *
     * @param session current session (AM/PM)
     */
    public static void borrowedTools(String session) {
        // Check if any unavailable tools
        if (api.toolIDList(true, false).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "All tools are currently available in the " + session + " Session. ", "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE, null);
            toolMaster(session);
        }

        // Get all unavailable tool ids
        ArrayList<Integer> allToolIDSUnavailable = api.toolIDList(true, false);
        String[][] unavailableToolData = new String[allToolIDSUnavailable.size()][4];

        // Build list of AM Class borrowed tools
        for (int i = 0; i < allToolIDSUnavailable.size(); i++) {
            unavailableToolData[i][0] = api.getToolName(allToolIDSUnavailable.get(i));
            unavailableToolData[i][1] = String.valueOf(api.getToolAvailablityQuantity(allToolIDSUnavailable.get(i), true));
            unavailableToolData[i][2] = String.valueOf(api.getToolAvailablityQuantity(allToolIDSUnavailable.get(i), false));
            unavailableToolData[i][3] = String.valueOf(allToolIDSUnavailable.get(i));
        }

        // JLabel Title
        JLabel borrowedTitle = new Title("(" + allToolIDSUnavailable.size() + ") Borrowed Tools");
        JLabel borrowedDescription = new Description( "Double click a tool to view the current borrowers (or more info)");

        // JTable (display all borrowed tools)
        JTable borrowedTools = new BorrowedTools(unavailableToolData);

        // Mouse listener for table row (tool lookup)
        borrowedTools.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                // Perform tool lookup
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    toolLookup(Integer.parseInt(borrowedTools.getValueAt(table.getSelectedRow(), 3).toString()), table);
                }
            }
        });

        // Remove borrowed tools that are no longer being borrowed + update stats
        borrowedTools.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                // If the updated tool is completely available
                if (borrowedTools.getRowCount() > 0 && Integer.parseInt((String)borrowedTools.getValueAt(e.getFirstRow(), 2)) == 0) {
                    borrowedTitle.setText("(" + api.toolIDList(true, false).size() + ") Borrowed Tools"); // update title
                }
            }
        });

        // JScrollPane scroll (display JTable with scrollbar)
        JScrollPane scroll = new Scroll(borrowedTools, 1000, 500);

        // Unavailable Tools Display Array
        Object[] display = {
                borrowedTitle, // Borrowers Title (display amount of tools unavailable + session)
                borrowedDescription,
                scroll // Text Area (display all unavailable tools)
        };

        // Display Unavailable Tools
        JOptionPane.showMessageDialog(null, display, "Tool Master Panel - Tool Report", JOptionPane.PLAIN_MESSAGE);

        // Ensure return back to Tool Master Panel
        toolMaster(session);
    }

    /**
     * The admin method controls authentication and various different admin
     * functions for the Teacher Panel
     */
    public static void admin() {
        // Login Field
        // JLabel Login Title (self-explanatory)
        JLabel loginTitle = new Title("Teacher Login");
        JLabel loginDesc = new Description("Please enter your login credentials");

        // JTextField & JLabel Username (Username field for login)
        JTextField username = new JTextField();
        JLabel usernameTitle = new Field("Username");

        // JPasswordField & JLabel Password (Password field for login)
        JPasswordField password = new JPasswordField();
        JLabel passwordTitle = new Field("Password");

        // Login Field Display Array
        Object[] loginField = {
                loginTitle, // Login Title (self-explanatory)
                loginDesc, // loginDesc,
                usernameTitle, username, // Username (Username field for login)
                passwordTitle, password // Password (Password field for login)
        };

        // Attempt login till session status is active
        // Authentication Display
        JLabel authTitle = new Title();
        JLabel authDesc = new Description();

        Object[] display = {
                authTitle,
                authDesc
        };

        while (!api.sessionStatus()) {
            // Display Login Dialog
            int option = JOptionPane.showConfirmDialog(null, loginField, "Teacher Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If Ok, check login
            if (option == JOptionPane.OK_OPTION && api.login(username.getText(), password.getText())) {

            } // If no, exit to menu
            else if (option == JOptionPane.CANCEL_OPTION) {
                mainMenu();
            } // Else, login was incorrect
            else {
                // Display Invalid Credentials Message
                authTitle.setText("Login Error");
                authDesc.setText("Invalid credentials, please try again");
                JOptionPane.showMessageDialog(null, display, "Teacher Login", JOptionPane.PLAIN_MESSAGE, null);
            }
        }

        // Initialize Options
        String[] options = {"Student Management", "Tool Management", "Logout"};

        // Display Title
        JLabel adminTitle = new Title("Welcome, Mr. Pickerell");
        JLabel adminDesc = new Description("What would you like to do today?");
        Object[] adminDisplay = {
                adminTitle,
                adminDesc
        };

        int selection = JOptionPane.showOptionDialog(null, adminDisplay, "Teacher Panel", 0, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            // Manage Students
            case 0 ->
                    studentManagementAdmin();
            // Manage Tools
            case 1 ->
                    toolManagementAdmin();
            // Return to main menu and logout
            case 2 -> {
                api.logout();
                mainMenu();
            }
        }
    }

    /**
     * The studentManagementAdmin method provides options for managing students
     * and is used only in the admin class
     */
    public static void studentManagementAdmin() {
        // Select Add or Remove or Back
        String[] studentOptions = {"Add Student", "Toggle Students", "Student Roster", "Back"};

        // Student Management Display
        JLabel studentManagementTitle = new Title("Student Management");
        JLabel studentManagementDescription = new Description("Please select an action");

        Object[] studentManagementDisplay = {
                studentManagementTitle,
                studentManagementDescription
        };

        int studentSelection = JOptionPane.showOptionDialog(null, studentManagementDisplay, "Teacher Panel - Student Management", 0, JOptionPane.PLAIN_MESSAGE, null, studentOptions, studentOptions[0]);
        switch (studentSelection) {
            case 0 -> {
                // Add Student Field
                // JLabel Add Student Title (Title)
                JLabel addStudentTitle = new Title("Add Student");

                // JTextField & JLabel Full Name (Text box to enter Full Student Name)
                JTextField fullName = new JTextField();
                JLabel fullNameTitle = new Field("Full Name", 26);

                // JTextField & JLabel Student Session (Text box to enter Student Session)
                JTextField studentSession = new JTextField();
                JLabel studentSessionTitle = new Field("Session (AM or PM)", 26);

                // Add Student Field Display Array
                Object[] addStudentField = {
                        addStudentTitle,
                        fullNameTitle, fullName, // Full Name (Text box to enter Full Student Name)
                        studentSessionTitle, studentSession // Student Session (Text box to enter Student Session)
                };

                // Display Add Student Field
                int addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Teacher Panel - Add Student", JOptionPane.OK_CANCEL_OPTION, -1, null);

                // If Ok, attempt student creation
                if (addStudentInfo == JOptionPane.OK_OPTION) {
                    boolean added = false;
                    // If student added
                    if (api.addStudent(fullName.getText(), studentSession.getText())) {
                        String successMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                        JOptionPane.showMessageDialog(null, successMessage, "Add Student", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else, if student not added, try again till added or cancelled
                    else {
                        while (!added) {
                            JOptionPane.showMessageDialog(null, "Student has not been added, please ensure you enter a valid session.", "Add Student", JOptionPane.PLAIN_MESSAGE, null);
                            addStudentInfo = JOptionPane.showConfirmDialog(null, addStudentField, "Teacher Panel - Add Student", JOptionPane.OK_CANCEL_OPTION, -1, null);
                            // If yes, continue trying to add student
                            if (addStudentInfo == JOptionPane.YES_OPTION) {
                                if (api.addStudent(fullName.getText(), studentSession.getText())) {
                                    added = true;
                                    String successAddMessage = "Student successfully added:\n\n" + "Name: " + fullName.getText() + "\nSession: " + studentSession.getText();
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Teacher Panel - Add Student", JOptionPane.PLAIN_MESSAGE, null);
                                }
                            } // If no, exit loop and return to menu
                            else {
                                break;
                            }
                        }
                    }

                    // Return to student management panel
                    studentManagementAdmin();
                } // If No, return to student management panel
                else {
                    studentManagementAdmin();
                }
            }

            case 1 -> {
                toggleStudentsAdmin();
            }

            case 2 ->
                    studentRosterAdmin();

            // Return back to Teacher Panel
            default -> admin();
        }
    }

    /**
     * The toggleStudentsAdmin method allows for the admin to activate/deactivate students
     */
    public static void toggleStudentsAdmin() {
        // Toggle Students
        // Select Reactivate or Deactivate or Back
        String[] toggleOptions = {"Reactivate Student", "Deactivate Student", "Deactivate all Students", "Back"};

        // Student Management Display
        JLabel toggleStudentsTitle = new Title("Toggle Students");
        JLabel toggleStudentsDescription = new Description("Please select an action");

        Object[] toggleStudentsDisplay = {
                toggleStudentsTitle,
                toggleStudentsDescription
        };

        int toggleStudentsSelection = JOptionPane.showOptionDialog(null, toggleStudentsDisplay, "Teacher Panel - Toggle Students", 0, JOptionPane.PLAIN_MESSAGE, null, toggleOptions, toggleOptions[0]);

        switch(toggleStudentsSelection) {
            case 0 -> {
                // if any students are deactivated
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

                    // Select and reactivate student
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Reactivate Student", "Teacher Panel - Reactivate Student", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);

                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.enableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully reactivated: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Teacher Panel - Reactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error, student has not been reactivated.", "Teacher Panel - Reactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                        }
                    } // If No, return to toggle students
                    else {
                        toggleStudentsAdmin();
                    }
                } // if no deactivated students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to reactivate.", "Teacher Panel - Reactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                }
                // Return to toggle students once completed
                toggleStudentsAdmin();
            }

            case 1 -> {
                // Deactivate student
                // if any students are deactivated
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

                    // Select and deactivate student
                    String studentToDisable = (String) JOptionPane.showInputDialog(null, "Deactivate Student", "Teacher Panel - Deactivate Student", JOptionPane.PLAIN_MESSAGE, null, studentList, studentList[0]);
                    // If yes
                    if (studentToDisable != null) {
                        // Strip String to only ID
                        String studentID = studentToDisable.replaceAll("^[^:\\r\\n]+:[ \\t]*", "");
                        if (api.disableStudent(Integer.parseInt(studentID))) {
                            String successRemoveMessage = "Student successfully disabled: " + studentToDisable;
                            JOptionPane.showMessageDialog(null, successRemoveMessage, "Teacher Panel - Deactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error, student has not been deactivated.", "Teacher Panel - Deactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                        }
                    } // Return to toggle students once completed
                    else {
                        toggleStudentsAdmin();
                    }
                } // if no inactive students
                else {
                    JOptionPane.showMessageDialog(null, "There are no students to deactivate.", "Teacher Panel - Deactivate Student", JOptionPane.PLAIN_MESSAGE, null);
                }
                // Return to toggle students once completed
                toggleStudentsAdmin();
            }

            // Deactivate all students
            case 2 -> {
                // Display
                JLabel deactivateAllTitle = new Title("Deactivate all Students?");
                JLabel deactivateAllDescription = new Description("Warning: this should only be used for a new school year.");
                Object[] display = {
                        deactivateAllTitle,
                        deactivateAllDescription
                };

                // Get choice
                int choice = JOptionPane.showConfirmDialog(null, display, "Teacher Panel - Deactivate all Students", JOptionPane.YES_NO_OPTION, -1, null);

                // Deactivate all students
                if (choice == JOptionPane.YES_OPTION) {
                    // Display warning
                    JLabel confirmationTitle = new Title("Are you sure you would like to deactivate all students?");
                    JLabel confirmDescription = new Description("If used in mistake, you will have to manually reactivate each student.");

                    Object[] confirmationDisplay = {
                            confirmationTitle,
                            confirmDescription
                    };

                    // Get choice
                    int confirmationChoice = JOptionPane.showConfirmDialog(null, confirmationDisplay, "Teacher Panel - Deactivate all Students", JOptionPane.YES_NO_OPTION, -1, null);

                    // Deactivate all students
                    if (confirmationChoice == JOptionPane.OK_OPTION) {
                        if (api.deactivateAllStudents()) {
                            JOptionPane.showMessageDialog(null, "All students deactivated successfully.", "Teacher Panel - Deactivate all Students", -1, null);
                        } else {
                            JOptionPane.showMessageDialog(null, "Unknown error, students were not deactivated.", "Teacher Panel - Deactivate all Students", -1, null);
                        }
                        toggleStudentsAdmin();
                    }
                    // Return to toggle students panel
                    else if (confirmationChoice == JOptionPane.NO_OPTION) {
                        toggleStudentsAdmin();
                    }

                } // Return to toggle students panel
                else if (choice == JOptionPane.NO_OPTION){
                    toggleStudentsAdmin();
                }
            }

            // Return back to Student Management
            default -> studentManagementAdmin();
        }
    }

    /**
     * The studentRosterAdmin method shows active/inactive students
     * used only in the admin method
     */
    public static void studentRosterAdmin() {
        // Check if any student available to borrow
        if (api.studentIDList(false).isEmpty() && api.studentIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No Students exist", "Teacher Panel - Student Roster", JOptionPane.PLAIN_MESSAGE, null);
            studentManagementAdmin();
        } // Else continue 
        else {
            // Initialize flags & arrays
            // Build Active Students
            boolean activeStudents = false;
            ArrayList<Integer> activeStudentIDList = api.studentIDList(true);
            String[][] activeStudentData = new String[activeStudentIDList.size()][3];

            // Build Inactive Students
            boolean inactiveStudents = false;
            ArrayList<Integer> inactiveStudentIDList = api.studentIDList(false);
            String[][] inactiveStudentData = new String[inactiveStudentIDList.size()][3];

            // Check inactive students
            if (!inactiveStudentIDList.isEmpty()) {
                // Update flag
                inactiveStudents = true;

                // Get inactive student list
                for (int i = 0; i < inactiveStudentIDList.size(); i++) {
                    inactiveStudentData[i][0] = api.getStudentName(inactiveStudentIDList.get(i));
                    inactiveStudentData[i][1] = String.valueOf(inactiveStudentIDList.get(i));
                    inactiveStudentData[i][2] = api.getStudentSession(inactiveStudentIDList.get(i));
                }
            }

            // Check active students
            if (!activeStudentIDList.isEmpty()) {
                // Update flag
                activeStudents = true;

                // Get active student list
                for (int i = 0; i < activeStudentIDList.size(); i++) {
                    activeStudentData[i][0] = api.getStudentName(activeStudentIDList.get(i));
                    activeStudentData[i][1] = String.valueOf(activeStudentIDList.get(i));
                    activeStudentData[i][2] = api.getStudentSession(activeStudentIDList.get(i));
                }
            }

            // Initialize Options
            String[] viewStudentOptions = {"Active Students", "Inactive Students", "Back"};

            // Student Roster Display
            JLabel studentRosterTitle = new Title("Student Roster");
            JLabel studentRosterDescription = new Description("Please select an option");
            Object[] studentRosterDisplay = {
                    studentRosterTitle,
                    studentRosterDescription
            };

            int viewStudentSelection = (int) JOptionPane.showOptionDialog(null, studentRosterDisplay, "Teacher Panel - Student Roster", 0, JOptionPane.PLAIN_MESSAGE, null, viewStudentOptions, viewStudentOptions[0]);
            switch (viewStudentSelection) {
                case 0 -> {
                    // View Active Students
                    // Check if Active Students found
                    if (activeStudents) {
                        // JLabel View Active Students Title (Title)
                        JLabel viewActiveStudentsTitle = new Title("Student Roster");
                        JLabel viewActiveStudentsDescription = new Description(api.studentIDList(true).size() + " Active Students");

                        // JTable (display active students)
                        JTable activeStudentsList = new StudentRoster(activeStudentData);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(activeStudentsList, 500, 500);

                        // View active Students Array
                        Object[] display = {
                                viewActiveStudentsTitle,
                                viewActiveStudentsDescription,
                                scroll // Students Enabled (display enabled students)
                        };

                        // Display Active Students
                        JOptionPane.showMessageDialog(null, display, "Teacher Panel - View Active Students", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no active Students found
                    else {
                        JOptionPane.showMessageDialog(null, "No active students exist", "Teacher Panel - View Active Students", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View Inactive Students
                case 1 -> {
                    // Check if Inactive Students found
                    if (inactiveStudents) {
                        // JLabel View inactive Students Title (Title)
                        JLabel viewInactiveStudentsTitle = new Title("Student Roster");
                        JLabel viewInactiveStudentsDescription = new Description(api.studentIDList(false).size() + " Inactive Students");

                        // JTable (display active students)
                        JTable inactiveStudentsList = new StudentRoster(inactiveStudentData);

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(inactiveStudentsList, 500, 500);

                        // View Disabled Students Array
                        Object[] display = {
                                viewInactiveStudentsTitle,
                                viewInactiveStudentsDescription,
                                scroll // scroll (display JTextArea with scrollbar)
                        };

                        // Display Inactive Students
                        JOptionPane.showMessageDialog(null, display, "Teacher Panel - View Inactive Students", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no inactive students found
                    else {
                        JOptionPane.showMessageDialog(null, "No inactive students exist", "Teacher Panel Panel - View Inactive Students", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }

                // Return back to Student management
                default -> studentManagementAdmin();
            }
        }
        // Return to student roster panel once completed
        studentRosterAdmin();
    }

    /**
     * The toolManagementAdmin method provides options for managing students and
     * is used only in the admin method
     */
    public static void toolManagementAdmin() {
        // Select Add or Remove or Back
        String[] toolOptions = {"Add Tool", "Add Tool Category", "Tool Inventory", "Tool Categories", "Back"};

        // Tool Management Display
        JLabel toolManagementTitle = new Title("Tool Management");
        JLabel toolManagementDescription = new Description("Please select an action");
        Object[] toolManagementDisplay = {
                toolManagementTitle,
                toolManagementDescription
        };

        int toolSelection = JOptionPane.showOptionDialog(null, toolManagementDisplay, "Teacher Panel - Tool Management", 0, JOptionPane.PLAIN_MESSAGE, null, toolOptions, toolOptions[0]);
        switch (toolSelection) {
            case 0 -> {
                // Add a tool
                JLabel title = new Title("Add Tool");
                JLabel toolNameTitle = new Field("Tool Name");
                JTextField toolName = new JTextField();
                JLabel toolQuantityTitle = new Field("Tool Quantity");
                JTextField toolQuantity = new JTextField();

                // Get Categories
                ArrayList<Category> categories = api.getCategories();

                // Remove all inactive categories
                for (int i = 0; i < categories.size(); i++) {
                    if (!categories.get(i).isStatus()) {
                        categories.remove(i);
                        i--;
                    }
                }

                JLabel categoryTitle = new Field("Category");
                String[] categoryList = new String[categories.size()];
                for (int i = 0; i < categoryList.length; i++) {
                    categoryList[i] = categories.get(i).getCategoryName() + " (ID: " + categories.get(i).getId() + ")";
                }
                JComboBox categoryDropdown = new JComboBox(categoryList);

                Object[] display = {
                        title,
                        toolNameTitle,
                        toolName,
                        toolQuantityTitle,
                        toolQuantity,
                        categoryTitle,
                        categoryDropdown
                };

                while (toolName.getText().isBlank() && toolQuantity.getText().isBlank()) {
                    int btn = JOptionPane.showOptionDialog(null, display, "Teacher Panel - Add Tool", 0, -1, null, new String[]{"Add Tool", "Back"}, 0);

                    if (btn == 0) {
                        // If yes, add
                        if ((!toolName.getText().isBlank()) && (!toolQuantity.getText().isBlank())) {
                            // If tool added
                            if (Integer.parseInt(toolQuantity.getText()) > 0) {
                                // Get Category / Parse Category ID
                                String effectedCategory = (String) categoryDropdown.getSelectedItem();
                                Pattern pattern = Pattern.compile("\\(ID: (\\d+)\\)");
                                Matcher matcher = pattern.matcher(effectedCategory);
                                matcher.find();
                                int categoryID = Integer.parseInt(matcher.group(1));

                                // Create tool
                                int toolID = api.createTool(toolName.getText(), Integer.parseInt(toolQuantity.getText()), categoryID);
                                if (toolID != 0) {
                                    String successAddMessage = "Tool added to inventory: \n" + toolName.getText() + " (ID: " + toolID + ")\nCategory: " + api.getCategory(api.getTool(toolID).getCategoryId()).getCategoryName();
                                    JOptionPane.showMessageDialog(null, successAddMessage, "Teacher Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
                                } // Else if tool not added,
                                else {
                                    JOptionPane.showMessageDialog(null, "Error adding tool to inventory.", "Teacher Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
                                }
                            } // Quantity less than 1
                            else {
                                JOptionPane.showMessageDialog(null, "Tool has not been added, quantity must be greater than 0.", "Teacher Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
                            }

                            // If no, return to Teacher Panel
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been added, one or more fields were not completed.", "Teacher Panel - Add Tool", JOptionPane.PLAIN_MESSAGE);
                            toolManagementAdmin();
                        }
                    }
                    else {
                        break;
                    }
                }

                // Return to tool management once completed
                toolManagementAdmin();
            }

            // Add Tool Category
            case 1 -> {
                // Add a tool
                JLabel title = new Title("Add Tool Category");
                JLabel categoryNameTitle = new Field("Category Name");
                JTextField categoryName = new JTextField();
                Object[] display = {
                        title,
                        categoryNameTitle,
                        categoryName
                };
                while (categoryName.getText().isBlank()) {
                    int btn = JOptionPane.showOptionDialog(null, display, "Teacher Panel - Add Tool", 0, -1, null, new String[]{"Add Category", "Back"}, 0);
                    if (btn == 0) {
                        // If yes, add
                        if ((!categoryName.getText().isBlank())) {
                            // Create Category
                            if (api.addCategory(categoryName.getText())) {
                                String successAddMessage = "Category added: " + categoryName.getText();
                                JOptionPane.showMessageDialog(null, successAddMessage, "Teacher Panel - Add Category", JOptionPane.PLAIN_MESSAGE);
                            } // Else if tool not added,
                            else
                                JOptionPane.showMessageDialog(null, "Error adding tool to inventory.", "Teacher Panel - Add Category", JOptionPane.PLAIN_MESSAGE);
                            // If no, return to Teacher Panel
                        } else {
                            JOptionPane.showMessageDialog(null, "Tool has not been added, one or more fields were not completed.", "Teacher Panel - Add Category", JOptionPane.PLAIN_MESSAGE);
                            toolManagementAdmin();
                        }
                    }
                    else {
                        break;
                    }
                }
                // Return to tool management once completed
                toolManagementAdmin();
            }

            // Tool Inventory
            case 2 -> toolInventoryAdmin();

            // Tool Categories
            case 3 -> toolCategoryAdmin();

            // Back to Teacher Panel
            default -> admin();
        }
    }

    /**
     * The toolInventoryAdmin method shows disabled/enabled tools, and all tools
     * used only in the admin method
     */
    public static void toolInventoryAdmin() {
        // Check if any tools available to borrow
        if (api.toolIDList(false).isEmpty() && api.toolIDList(true).isEmpty()) {
            // Back to tool master if no tools found
            JOptionPane.showMessageDialog(null, "No tools exist", "Teacher Panel - View Tools", JOptionPane.PLAIN_MESSAGE, null);
            toolManagementAdmin();
        } // Else continue 
        else {
            // Initialize flags & arrays
            boolean inactiveTools = false;
            boolean activeTools = false;

            // Inactive Tools
            ArrayList<Integer> inactiveToolIDList = api.toolIDList(false);
            String[][] inactiveToolData = new String[inactiveToolIDList.size()][4];

            // Active Tools
            ArrayList<Integer> activeToolIDList = api.toolIDList(true);
            String[][] activeToolData = new String[activeToolIDList.size()][4];

            // Check Inactive Tools
            if (!inactiveToolIDList.isEmpty()) {
                // Update flag
                inactiveTools = true;

                // Build data
                for (int i = 0; i < inactiveToolData.length; i++) {
                    var tool = api.getTool(inactiveToolIDList.get(i));
                    inactiveToolData[i][0] = tool.getToolName(); // tool name

                    // Get category if assigned
                    if (tool.getCategoryId() != 0)
                        inactiveToolData[i][1] = api.getCategory(tool.getCategoryId()).getCategoryName(); // category name
                    else // else assign no category
                        inactiveToolData[i][1] = "None";

                    inactiveToolData[i][2] = String.valueOf(tool.getId()); // tool id
                    inactiveToolData[i][3] = String.valueOf(tool.getQuantity()); // tool quantity
                }
            }

            // Check Active tools
            if (!activeToolIDList.isEmpty()) {
                // Update flag
                activeTools = true;

                // Build data
                for (int i = 0; i < activeToolData.length; i++) {
                    var tool = api.getTool(activeToolIDList.get(i));
                    activeToolData[i][0] = tool.getToolName(); // tool name

                    // Get category if assigned
                    if (tool.getCategoryId() != 0)
                        activeToolData[i][1] = api.getCategory(tool.getCategoryId()).getCategoryName(); // category name
                    else // else assign no category
                        activeToolData[i][1] = "None";

                    activeToolData[i][2] = String.valueOf(tool.getId()); // tool id
                    activeToolData[i][3] = String.valueOf(tool.getQuantity()); // tool quantity
                }
            }

            // Initialize Options
            String[] toolInventoryOptions = {"Active Tools", "Inactive Tools", "Back"};

            // Tool Inventory Display
            JLabel toolInventoryTitle = new Title("Tool Inventory");
            JLabel toolInventoryDescription = new Description("Please select a filter");
            Object[] toolInventoryDisplay = {
                    toolInventoryTitle,
                    toolInventoryDescription
            };

            int toolInventorySelection = (int) JOptionPane.showOptionDialog(null, toolInventoryDisplay, "Teacher Panel - Tool Inventory", 0, JOptionPane.PLAIN_MESSAGE, null, toolInventoryOptions, toolInventoryOptions[0]);
            switch (toolInventorySelection) {
                // View Active Tools
                case 0 -> {
                    // Check if active tools found
                    if (activeTools) {
                        // JLabel Active Tools
                        JLabel toolsActiveTitle = new Title("Tool Inventory (" + api.toolNameList(true).size() + " Active Tools)");
                        JLabel toolsActiveDescription = new Description("Double click a tool to edit its details");

                        // JTextArea Active Tools (display all enabled tools)
                        JTable toolsActiveTable = new ToolInventory(activeToolData);
                        // Mouse listener for table row (edit tool)
                        toolsActiveTable.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent mouseEvent) {
                                JTable table = (JTable) mouseEvent.getSource();
                                // Perform tool lookup
                                if (mouseEvent.getClickCount() == 2 && toolsActiveTable.getSelectedRow() != -1)
                                    editTool(api.getTool(Integer.parseInt((String) toolsActiveTable.getValueAt(table.getSelectedRow(), 2))));
                            }
                        });

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(toolsActiveTable, 1000, 500);

                        // Active Tools Display
                        Object[] enabledToolsDisplay = {
                                toolsActiveTitle,
                                toolsActiveDescription,
                                scroll
                        };

                        // Display Active Tools
                        JOptionPane.showMessageDialog(null, enabledToolsDisplay, "Teacher Panel - View Active Tools", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no Active tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No active tools exist", "Teacher Panel - View Active Tools", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View Inactive Tools
                case 1 -> {
                    // Check if Inactive Tools found
                    if (inactiveTools) {
                        // JLabel Inactive Tools
                        JLabel toolsInactiveTitle = new Title("Tool Inventory (" + api.toolNameList(false).size() + " Inactive Tools)");
                        JLabel toolsInactiveDescription = new Description("Double click a tool to edit its details");

                        // JTextArea Active Tools (display all enabled tools)
                        JTable toolsInactiveTable = new ToolInventory(inactiveToolData);
                        // Mouse listener for table row (edit tool)
                        toolsInactiveTable.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent mouseEvent) {
                                JTable table =(JTable) mouseEvent.getSource();
                                // Perform tool lookup
                                if (mouseEvent.getClickCount() == 2 && toolsInactiveTable.getSelectedRow() != -1)
                                    editTool(api.getTool(Integer.parseInt((String) toolsInactiveTable.getValueAt(table.getSelectedRow(), 2))));
                            }
                        });

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(toolsInactiveTable, 1000, 500);

                        // Inactive Tools Display
                        Object[] inactiveToolsDisplay = {
                                toolsInactiveTitle,
                                toolsInactiveDescription,
                                scroll
                        };

                        /// Display Inactive Tools
                        JOptionPane.showMessageDialog(null, inactiveToolsDisplay, "Teacher Panel - View Inactive Tools", JOptionPane.PLAIN_MESSAGE, null);
                    } // else no Inactive tools found
                    else {
                        JOptionPane.showMessageDialog(null, "No inactive tools exist", "Teacher Panel Panel - View Inactive Tools", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }

                // Return back to tool management
                default -> toolManagementAdmin();
            }
        }
        // Return to view tools panel once completed
        toolInventoryAdmin();
    }

    /**
     * Used for editing a specific tool (name, category, quantity, status etc)
     * @param tool the tool
     */
    public static void editTool(Tool tool) {
        // Build UI
        JLabel title = new Title("Editing Tool");
        JLabel description = new Description(tool.getToolName());

        // Tool Properties
        JLabel toolName = new Field("Name");
        JTextField toolNameInput = new JTextField(tool.getToolName());

        JLabel toolCategory = new Field("Category");
        // Build Category List (first = current category)
        var categories = api.getCategories();

        // Remove all inactive categories
        for (int i = 0; i < categories.size(); i++) {
            if (!categories.get(i).isStatus()) {
                categories.remove(i);
                i--;
            }
        }

        var firstCategory = categories.get(0);
        for (int i = 0; i < categories.size(); i++) {
            // When current category found, swap it with the first place category
            if (categories.get(i).getId() == tool.getCategoryId()) {
                categories.set(0, categories.get(i));
                categories.set(i, firstCategory);
            }
        }
        // Convert to Array & Format: Category Name (ID: xxx)
        String[] categoriesArray = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoriesArray[i] = categories.get(i).getCategoryName() + " (ID: " + categories.get(i).getId() + ")";
        }
        JComboBox toolCategoryInput = new JComboBox(categoriesArray);

        JLabel toolQuantity = new Field("Quantity");
        JTextField toolQuantityInput = new JTextField(String.valueOf(tool.getQuantity()));

        // Build status
        JLabel toolStatus = new Field("Status");
        JComboBox toolStatusInput;
        if (tool.isStatus())
            toolStatusInput = new JComboBox(new String[]{"Active", "Inactive"});
        else
            toolStatusInput = new JComboBox(new String[]{"Inactive", "Active"});

        // Buttons
        JButton updateBtn = new Button("Update Tool");
        JButton cancelBtn = new Button("Cancel");

        Object[] editToolDisplay = {
                title,
                description,
                toolName,
                toolNameInput,
                toolCategory,
                toolCategoryInput,
                toolQuantity,
                toolQuantityInput,
                toolStatus,
                toolStatusInput,
                updateBtn,
                cancelBtn
        };

        // Action Listener for updating tool
        updateBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(updateBtn);
            if (w != null) {
                // Get Category
                String effectedCategory = (String) toolCategoryInput.getSelectedItem();
                Pattern pattern = Pattern.compile("\\(ID: (\\d+)\\)");
                Matcher matcher = pattern.matcher(effectedCategory);
                matcher.find();
                int categoryID = Integer.parseInt(matcher.group(1));

                // Set tool based off of status
                if (((String) toolStatusInput.getSelectedItem()).equalsIgnoreCase("Active"))
                    api.setTool(new Tool(tool.getId(), categoryID, toolNameInput.getText(), Integer.parseInt(toolQuantityInput.getText()), true));
                else
                    api.setTool(new Tool(tool.getId(), categoryID, toolNameInput.getText(), Integer.parseInt(toolQuantityInput.getText()), false));
                w.dispose(); // dispose window
            }
        });

        // Action Listener for cancelling edit
        cancelBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(updateBtn);
            if (w != null) {
                w.dispose(); // dispose window
            }
        });

        // Display menu
        JOptionPane.showOptionDialog(null, editToolDisplay, "Edit Tool", 0, -1, null, new Object[]{}, null);
    }

    /**
     * Displays all inactive & active categories and allows for them to be edited
     */
    public static void toolCategoryAdmin() {
        // Check if any categories exist
        if (api.getCategories().isEmpty()) {
            // Back to tool master if no categories found
            JOptionPane.showMessageDialog(null, "No categories exist", "Teacher Panel - View Categories", JOptionPane.PLAIN_MESSAGE, null);
            toolManagementAdmin();
        } // Else continue
        else {
            // Initialize flags & arrays
            boolean inactiveCategory = false;
            boolean activeCategory = false;

            // Build category data
            var categories = api.getCategories();

            ArrayList<Category> inactiveCategoriesData = new ArrayList<>();
            ArrayList<Category> activeCategoriesData = new ArrayList<>();
            for (int i = 0; i < categories.size(); i++) {
                // Add active categories
                if (categories.get(i).isStatus())
                    activeCategoriesData.add(categories.get(i));
                    // Add inactive categories
                else
                    inactiveCategoriesData.add(categories.get(i));
            }

            // Init categories
            String[][] inactiveCategories = new String[inactiveCategoriesData.size()][3];
            String[][] activeCategories = new String[activeCategoriesData.size()][3];

            // Check/Build Inactive Categories
            if (!inactiveCategoriesData.isEmpty()) {
                // Update flag
                inactiveCategory = true;

                // Build data
                for (int i = 0; i < inactiveCategoriesData.size(); i++) {
                    var category = inactiveCategoriesData.get(i);
                    inactiveCategories[i][0] = category.getCategoryName(); // category name
                    inactiveCategories[i][1] = String.valueOf(category.getTools().size()); // tools amount
                    inactiveCategories[i][2] = String.valueOf(category.getId()); // category id
                }
            }

            // Check/Build Active Categories
            if (!activeCategoriesData.isEmpty()) {
                // Update flag
                activeCategory = true;

                // Build data
                for (int i = 0; i < activeCategoriesData.size(); i++) {
                    var category = activeCategoriesData.get(i);
                    activeCategories[i][0] = category.getCategoryName(); // category name
                    activeCategories[i][1] = String.valueOf(category.getTools().size()); // tools amount
                    activeCategories[i][2] = String.valueOf(category.getId()); // category id
                }
            }

            // Initialize Options
            String[] categoryInventoryOptions = {"Active Categories", "Inactive Categories", "Back"};

            // Tool Inventory Display
            JLabel categoryTitle = new Title("Tool Categories");
            JLabel categoryDescription = new Description("Please select a filter");
            Object[] toolCategoryDisplay = {
                    categoryTitle,
                    categoryDescription
            };

            int toolCategorySelection = (int) JOptionPane.showOptionDialog(null, toolCategoryDisplay, "Teacher Panel - Tool Categories", 0, JOptionPane.PLAIN_MESSAGE, null, categoryInventoryOptions, categoryInventoryOptions[0]);
            switch (toolCategorySelection) {
                // View Active Categories
                case 0 -> {
                    // Check if active categories found
                    if (activeCategory) {
                        // JLabel Active Categories
                        JLabel categoriesActiveTitle = new Title("Categories (" + activeCategories.length + " Active Categories)");
                        JLabel categoriesActiveDescription = new Description("Double click a category to edit its details");

                        // JTextArea Active Categories (display all active categories)
                        JTable categoriesActiveTable = new Categories(activeCategories);
                        // Mouse listener for table row (edit category)
                        categoriesActiveTable.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent mouseEvent) {
                                JTable table = (JTable) mouseEvent.getSource();
                                // Perform category lookup
                                if (mouseEvent.getClickCount() == 2 && categoriesActiveTable.getSelectedRow() != -1)
                                    editCategory(api.getCategory(Integer.parseInt((String) categoriesActiveTable.getValueAt(table.getSelectedRow(), 2))));
                            }
                        });

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(categoriesActiveTable, 1000, 500);

                        // Active Categories Display
                        Object[] enabledToolsDisplay = {
                                categoriesActiveTitle,
                                categoriesActiveDescription,
                                scroll
                        };

                        // Display active categories
                        JOptionPane.showMessageDialog(null, enabledToolsDisplay, "Teacher Panel - View Active Tool Categories", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no active categories found
                    else {
                        JOptionPane.showMessageDialog(null, "No Active Tool Categories exist", "Teacher Panel - View Active Tool Categories", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }
                // View Inactive Categories
                case 1 -> {
                    // Check if inactive categories found
                    if (inactiveCategory) {
                        // JLabel Inctive Categories
                        JLabel categoriesInactiveTitle = new Title("Categories (" + inactiveCategories.length + " Inactive Categories)");
                        JLabel categoriesInactiveDescription = new Description("Double click a category to edit its details");

                        // JTextArea Inactive Categories (display all inactive categories)
                        JTable categoriesInactiveTable = new Categories(inactiveCategories);
                        // Mouse listener for table row (edit category)
                        categoriesInactiveTable.addMouseListener(new MouseAdapter() {
                            public void mousePressed(MouseEvent mouseEvent) {
                                JTable table = (JTable) mouseEvent.getSource();
                                // Perform category lookup
                                if (mouseEvent.getClickCount() == 2 && categoriesInactiveTable.getSelectedRow() != -1)
                                    editCategory(api.getCategory(Integer.parseInt((String) categoriesInactiveTable.getValueAt(table.getSelectedRow(), 2))));
                            }
                        });

                        // JScrollPane scroll (display JTextArea with scrollbar)
                        JScrollPane scroll = new Scroll(categoriesInactiveTable, 1000, 500);

                        // Inactive Categories Display
                        Object[] enabledToolsDisplay = {
                                categoriesInactiveTitle,
                                categoriesInactiveDescription,
                                scroll
                        };

                        // Display active categories
                        JOptionPane.showMessageDialog(null, enabledToolsDisplay, "Teacher Panel - View Inactive Tool Categories", JOptionPane.PLAIN_MESSAGE, null);
                    } // Else no active categories found
                    else {
                        JOptionPane.showMessageDialog(null, "No Inactive Tool Categories exist", "Teacher Panel - View Inactive Tool Categories", JOptionPane.PLAIN_MESSAGE, null);
                    }
                }

                // Return back to tool management
                default -> toolManagementAdmin();
            }
        }
        // Return to tool categories panel once completed
        toolCategoryAdmin();
    }

    /**
     * Used for editing a specific category (name, status, etc)
     * @param category the category
     */
    public static void editCategory(Category category) {
        // Build UI
        JLabel title = new Title("Editing Category");
        JLabel description = new Description(category.getCategoryName());

        // Category Properties
        JLabel categoryName = new Field("Name");
        JTextField categoryNameInput = new JTextField(category.getCategoryName());

        // Build status
        JLabel categoryStatus = new Field("Status");
        JComboBox categoryStatusInput;
        if (category.isStatus())
            categoryStatusInput = new JComboBox(new String[]{"Active", "Inactive"});
        else
            categoryStatusInput = new JComboBox(new String[]{"Inactive", "Active"});

        // Buttons
        JButton updateBtn = new Button("Update Category");
        JButton cancelBtn = new Button("Cancel");

        Object[] editCategoryDisplay = {
                title,
                description,
                categoryName,
                categoryNameInput,
                categoryStatus,
                categoryStatusInput,
                updateBtn,
                cancelBtn
        };

        // Action Listener for updating tool
        updateBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(updateBtn);
            if (w != null) {
                // Set tool based off of status
                if (((String) categoryStatusInput.getSelectedItem()).equalsIgnoreCase("Active"))
                    api.setCategory(new Category(category.getId(), categoryNameInput.getText(), true, category.getTools()));
                else
                    api.setCategory(new Category(category.getId(), categoryNameInput.getText(), false, category.getTools()));
                w.dispose(); // dispose window
            }
        });

        // Action Listener for cancelling edit
        cancelBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(updateBtn);
            if (w != null) {
                w.dispose(); // dispose window
            }
        });

        // Display menu
        JOptionPane.showOptionDialog(null, editCategoryDisplay, "Edit Tool", 0, -1, null, new Object[]{}, null);
    }
}
