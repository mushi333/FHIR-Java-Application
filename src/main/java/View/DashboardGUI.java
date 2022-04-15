package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

import Model.AppPractitioner;
import Model.AppPatient;

/**
 *  Creates the dashboard page so the practitioner can monitor their patients
 *  at a certain rate and check their details.
 *
 *  @author Kenneth Huynh
 */
public class DashboardGUI extends GUI {
    // Dashboard components
    public AllPatientsPane allPatientsPanel;
    public PatientDetailsPane patientDetailsPane;

    // Blood pressure panel details
    private JTextArea bloodPressureValues;

    // Table data
    private DefaultTableModel monitorTableModel;
    private boolean cholesterolState;
    private boolean bloodPressureState;
    private int systolicReading;
    private int diastolicReading;

    // Table buttons and text fields
    private JButton cholesterolSwitch;
    private JButton bloodPressureSwitch;
    private JTextField systolicTextField;
    private JButton systolicSetButton;
    private JTextField diastolicTextField;
    private JButton diastolicSetButton;
    private JLabel bloodPressureFeedback;

    // Graphs
    private JPanel cholesterolGraphCard;
    private JPanel bloodPressureGraphCard;

    // Practitioner
    private final AppPractitioner PRACTITIONER;

    /**
     *  Updates the frame with the dashboard
     *
     *  @param frame Used to show the practitioners dashboard
     */
    public DashboardGUI(JFrame frame, AppPractitioner practitioner) {
        // Initial Frame
        super(frame);

        // Setup instance variables
        this.PRACTITIONER = practitioner;

        // Predetermined values
        this.cholesterolState = true;
        this.bloodPressureState = true;
        this.systolicReading = 9999;
        this.diastolicReading = 9999;

        // Create dashboard page
        createDashboardPanel();
    }

    /**
     * Refreshes the patient monitor.
     */
    public void refreshMonitorTableModel() {
        final int CHOLESTEROL = 1;
        final int CHOLESTEROL_TIME = 2;
        final int SYSTOLIC = 3;
        final int DIASTOLIC = 4;
        final int BP_TIME = 5;

        // Updates the visibility of cholesterol cells
        int columns = monitorTableModel.getColumnCount();
        int rows = monitorTableModel.getRowCount();
        for (int row = 0; row < rows; row++) {
            final String CHOLESTEROL_STR = PRACTITIONER.getPatientFromMonitorList(row).getCholesterolLevel();
            final String SYSTOLIC_STR = PRACTITIONER.getPatientFromMonitorList(row).getLatestSystolicBloodPressure();
            final String DIASTOLIC_STR = PRACTITIONER.getPatientFromMonitorList(row).getLatestDiastolicBloodPressure();
            for (int column = 0; column < columns; column++) {
                // Updates the visibility of cholesterol cells
                if (!cholesterolState && (column == CHOLESTEROL || column == CHOLESTEROL_TIME)) {
                    monitorTableModel.setValueAt("-", row, column);
                } else {
                    if (column == CHOLESTEROL) {
                        monitorTableModel.setValueAt(CHOLESTEROL_STR, row, column);
                    } else if (column == CHOLESTEROL_TIME) {
                        final String CHOLESTEROL_TIME_STR = PRACTITIONER.getPatientFromMonitorList(row).getEffectiveDateTimeCholesterol();
                        monitorTableModel.setValueAt(CHOLESTEROL_TIME_STR, row, column);
                    }
                }

                // Updates the visibility of blood pressure cells
                if (!bloodPressureState && (column == SYSTOLIC || column == DIASTOLIC || column == BP_TIME)) {
                    monitorTableModel.setValueAt("-", row, column);
                } else {
                    if (column == SYSTOLIC) {
                        monitorTableModel.setValueAt(SYSTOLIC_STR, row, column);
                    } else if (column == DIASTOLIC) {
                        monitorTableModel.setValueAt(DIASTOLIC_STR, row, column);
                    } else if (column == BP_TIME) {
                        final String BLOOD_PRESSURE_TIME_STR = PRACTITIONER.getPatientFromMonitorList(row).getLatestEffectiveDateTimeBloodPressure();
                        monitorTableModel.setValueAt(BLOOD_PRESSURE_TIME_STR, row, column);
                    }
                }
            }
        }

        monitorTableModel.fireTableDataChanged();

        // Refresh cholesterol graph
        cholesterolGraphCard.removeAll();
        cholesterolGraphCard.add(new BarGraph(PRACTITIONER.getMonitorPatientList(), new Dimension(1220, 330)));

        // Refresh blood pressure graph
        bloodPressureGraphCard.removeAll();
        bloodPressureGraphCard.add(new LineGraph(PRACTITIONER.getMonitorPatientList(), new Dimension(1220, 330)));
    }

    /**
     * Adds listener for the cholesterol button.
     * @param listener The listener class used to control the cholesterol button's action.
     */
    public void addCholesterolSwitchListener(ActionListener listener){
        cholesterolSwitch.addActionListener(listener);
    }

    /**
     * Adds listener for the blood pressure button.
     * @param listener The listener class used to control the blood pressures button's action.
     */
    public void addBloodPressureSwitchListener(ActionListener listener) {
        bloodPressureSwitch.addActionListener(listener);
    }

    /**
     * Adds listener for the systolic setter button.
     * @param listener The listener class used to control the systolic setter button's action.
     */
    public void addSystolicSetterListener(ActionListener listener) {
        systolicSetButton.addActionListener(listener);
    }

    /**
     * Adds listener for the diastolic setter button.
     * @param listener The listener class used to control the diastolic setter button's action.
     */
    public void addDiastolicSetterListener(ActionListener listener) {
        diastolicSetButton.addActionListener(listener);
    }

    /**
     * Getter for the text within the systolic blood pressure Text Field.
     * @return The systolic blood pressure in mmHg as a string
     */
    public String getSystolicText() {
        return systolicTextField.getText();
    }

    /**
     * Getter for the text within the diastolic blood pressure Text Field.
     * @return The diastolic blood pressure in mmHg as a string
     */
    public String getDiastolicText() {
        return diastolicTextField.getText();
    }

    /**
     * Setter for the systolic reading
     * @param newSystolicReading The new minimum systolic reading
     */
    public void setSystolicReading(int newSystolicReading) {
        systolicReading = newSystolicReading;
    }

    /**
     * Setter for the diastolic reading
     * @param newDiastolicReading The new minimum diastolic reading
     */
    public void setDiastolicReading(int newDiastolicReading) {
        diastolicReading = newDiastolicReading;
    }

    /**
     * Setter for the text for the blood pressure feedback.
     * @param feedback Feedback message
     */
    public void setBloodPressureFeedback(String feedback) {
        bloodPressureFeedback.setText(feedback);
    }

    /**
     * Switches the status of the cholesterol state.
     */
    public void switchCholesterolState() {
        cholesterolState = !cholesterolState;
    }

    /**
     * Switches the status of the blood pressure state.
     */
    public void switchBloodPressureState() {
        bloodPressureState = !bloodPressureState;
    }

    /**
     *  Sets up the dashboard frame by creating all the needed panels and its elements
     */
    public void createDashboardPanel() {
        // Dashboard title
        JLabel title = new JLabel("DASHBOARD");
        title.setFont(title.getFont().deriveFont(20f));    // change to bigger font
        JPanel titlePanel = new JPanel();
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        // All patients belonging to the practitioner
        allPatientsPanel = new AllPatientsPane(PRACTITIONER.getAllPatients(), new Dimension(250, 460));

        // Patients measurements table
        JPanel tabPanel = new JPanel();
        addTabbedComponentToPane(tabPanel);

        // Patient details
        patientDetailsPane = new PatientDetailsPane(new Dimension(250, 460));

        // Centre panel
        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.X_AXIS));
        centrePanel.add(Box.createHorizontalGlue());
        centrePanel.add(allPatientsPanel);
        centrePanel.add(Box.createHorizontalGlue());
        centrePanel.add(tabPanel);
        
        centrePanel.add(Box.createHorizontalGlue());
        centrePanel.add(patientDetailsPane);
        centrePanel.add(Box.createHorizontalGlue());

        // Add to frame
        frame.add(titlePanel, BorderLayout.PAGE_START);
        frame.add(centrePanel, BorderLayout.CENTER);

        // Reset frame visibility to see next page view
        frame.setSize(1750, 520);
        frame.setVisible(true);
    }

    /**
     *  Sets up the panel for patients to be monitored.
     *
     *  @param pane:    Used to attach the monitoring table
     */
    public void addMonitorTableToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // Patient measurement table
        createMonitorTable(pane);

        // Cholesterol button
        JPanel buttonPanel = new JPanel();
        cholesterolSwitch = new JButton("Cholesterol");
        buttonPanel.add(cholesterolSwitch);

        // Blood pressure button
        bloodPressureSwitch = new JButton("Blood Pressure");
        buttonPanel.add(bloodPressureSwitch);

        buttonPanel.add(Box.createVerticalGlue());

        // Systolic blood pressure label
        JLabel systolicLabel = new JLabel("Systolic Blood Pressure:");
        buttonPanel.add(systolicLabel);

        // Systolic blood pressure text field
        systolicTextField = new JTextField("", 8);
        buttonPanel.add(systolicTextField);

        // Systolic blood pressure set button
        systolicSetButton = new JButton("Set");
        buttonPanel.add(systolicSetButton);

        buttonPanel.add(Box.createVerticalGlue());

        // Diastolic blood pressure label
        JLabel diastolicLabel = new JLabel("Diastolic Blood Pressure:");
        buttonPanel.add(diastolicLabel);

        // Diastolic blood pressure text field
        diastolicTextField = new JTextField("", 8);
        buttonPanel.add(diastolicTextField);

        // Diastolic blood pressure set button
        diastolicSetButton = new JButton("Set");
        buttonPanel.add(diastolicSetButton);

        buttonPanel.add(Box.createVerticalGlue());

        // Blood pressure warning label
        bloodPressureFeedback = new JLabel("          ");
        buttonPanel.add(bloodPressureFeedback);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(buttonPanel);
    }

    /**
     *  Creates the table for monitoring patients cholesterol values.
     *
     * @param panel:    Used to encapsulate the table of patients cholesterol data
     */
    public void createMonitorTable(Container panel) {
        // Patient table column headings
        String[] columnNames = {"Name", "Total Cholesterol (mg/dL)", "Time", "Systolic Blood Pressure (mmHg)", "Diastolic Blood Pressure (mmHg)", "Time"};

        // Set up table data
        monitorTableModel = new DefaultTableModel(columnNames, 0);

        final JTable TABLE = new JTable(monitorTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disables cell editing
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                final int CHOLESTEROL = 1;
                final int SYSTOLIC = 3;
                final int DIASTOLIC = 4;

                final String CHOLESTEROL_STR = PRACTITIONER.getPatientFromMonitorList(row).getCholesterolLevel();
                final String SYSTOLIC_STR = PRACTITIONER.getPatientFromMonitorList(row).getLatestSystolicBloodPressure();
                final String DIASTOLIC_STR = PRACTITIONER.getPatientFromMonitorList(row).getLatestDiastolicBloodPressure();
                final Object CELL_VALUE = this.getValueAt(row, column);

                // Compare current patients cholesterol
                if ((column == CHOLESTEROL) && (!CHOLESTEROL_STR.equals("N/A") && (!CELL_VALUE.equals("-")))) {

                    float currCholesterol = Float.parseFloat(CHOLESTEROL_STR);
                    float averageCholesterol = PRACTITIONER.getMonitorPatientList().getAverageCholesterol();

                    if ((currCholesterol > averageCholesterol) && (PRACTITIONER.getMonitorPatientList().count() > 1)) {
                        c.setForeground(Color.RED);     // Abnormal cholesterol
                    } else {
                        c.setForeground(Color.BLACK);   // Normal cholesterol
                    }

                } else if ((column == SYSTOLIC) && (!SYSTOLIC_STR.equals("N/A")) && (!CELL_VALUE.equals("-"))) {
                    // Compare the systolic values of the patient

                    float currSystolic = Float.parseFloat(SYSTOLIC_STR);

                    if ((currSystolic > systolicReading) && (PRACTITIONER.getMonitorPatientList().count() > 0)) {
                        c.setForeground(Color.RED);     // Abnormal systolic values
                    } else {
                        c.setForeground(Color.BLACK);   // Normal systolic values
                    }
                } else if ((column == DIASTOLIC) && (!DIASTOLIC_STR.equals("N/A")) && (!CELL_VALUE.equals("-"))) {
                    // Compare the diastolic values of the patient
                    float currDiastolic = Float.parseFloat(DIASTOLIC_STR);

                    if ((currDiastolic > diastolicReading) && (PRACTITIONER.getMonitorPatientList().count() > 0)) {
                        c.setForeground(Color.RED);     // Abnormal diastolic values
                    } else {
                        c.setForeground(Color.BLACK);   // Normal diastolic values
                    }
                } else {
                    c.setForeground(Color.BLACK);   // Normal cell
                }

                return c;
            }
        };

        // Mouse click listener for row selection
        MouseListener tableMouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = TABLE.getSelectedRow();
                // Only get patient details if the table has at least one patient
                if (PRACTITIONER.getMonitorPatientList().count() > 0) {
                    AppPatient patient = PRACTITIONER.getPatientFromMonitorList(selectedRow);

                    // Update patient detail box
                    patientDetailsPane.setBirthDateLabel(patient.getBirthDate());
                    patientDetailsPane.setGenderLabel(patient.getGender());
                    patientDetailsPane.setAddressArea(patient.getAddress());
                }
            }
        };
        TABLE.addMouseListener(tableMouseListener);

        // Setup table
        TABLE.getTableHeader().setReorderingAllowed(false); // Disables column dragging
        TABLE.getTableHeader().setResizingAllowed(false); // Disable column resizing
        TABLE.setRowSelectionAllowed(false); // Disable cell row selection
        TABLE.setCellSelectionEnabled(false); // Disable cell selection
        TABLE.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(TABLE);

        // Add the scroll pane to this practitionerLoginFieldPanel.
        panel.add(scrollPane);
        panel.add(Box.createVerticalGlue());
    }

    /**
     *  Adds the patient to the cholesterol table for monitoring.
     *
     *  @param patient:  The patient to start monitoring
     */
    public boolean addPatientToMonitorTable(AppPatient patient) {
        // Add patient to table
        boolean patientAdded = PRACTITIONER.addPatientToMonitorList(patient);
        if (patientAdded) {
            Object[] cholesterolDetails = {patient.getName(), patient.getCholesterolLevel(), patient.getEffectiveDateTimeCholesterol(), patient.getLatestSystolicBloodPressure(), patient.getLatestDiastolicBloodPressure(), patient.getLatestEffectiveDateTimeBloodPressure()};
            monitorTableModel.addRow(cholesterolDetails);

            // Show changes
            updateBloodPressureText(PRACTITIONER.createBloodPressureDetails());
            refreshMonitorTableModel();
        }
        return patientAdded;
    }

    /**
     *  Removes the patient from the cholesterol table so it will no longer be monitored.
     *
     *  @param patient:  The patient to stop monitoring
     */
    public boolean removePatientFromMonitorTable(AppPatient patient) {
        // Remove patient from table
        int patientIndex = PRACTITIONER.getMonitorPatientList().indexOf(patient);
        boolean patientRemoved = PRACTITIONER.removePatientFromMonitorList(patient);
        if (patientRemoved) {
            monitorTableModel.removeRow(patientIndex);

            // Show changes
            updateBloodPressureText(PRACTITIONER.createBloodPressureDetails());
            refreshMonitorTableModel();
        }
        return patientRemoved;
    }

    /**
     * Adds a tabbed pane containing the monitor and graphs of the monitored patients
     * @param pane The pane to add the tabbed view to
     */
    public void addTabbedComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Patient monitor tab
        JPanel monitorCard = new JPanel();
        addMonitorTableToPane(monitorCard);
        tabbedPane.addTab("Patient Monitor", monitorCard);

        // Cholesterol graph tab
        cholesterolGraphCard = new JPanel();
        cholesterolGraphCard.add(new BarGraph(PRACTITIONER.getMonitorPatientList(), new Dimension(1220, 330)));
        tabbedPane.addTab("Cholesterol Graph", cholesterolGraphCard);

        // Blood Pressure graph tab
        bloodPressureGraphCard = new JPanel();
        bloodPressureGraphCard.add(new LineGraph(PRACTITIONER.getMonitorPatientList(), new Dimension(1220, 330)));
        tabbedPane.addTab("Blood Pressure Graphs", bloodPressureGraphCard);

        // Systolic Blood pressure tab
        JPanel bloodPressureCard = new JPanel();
        bloodPressureValues = new JTextArea();
        bloodPressureValues.setEditable(false);
        bloodPressureCard.add(bloodPressureValues);
        tabbedPane.addTab("Latest Systolic Blood Pressure", bloodPressureCard);

        tabbedPane.setPreferredSize(new Dimension(1220, 380));
        tabbedPane.setMaximumSize(new Dimension(1220, 380));

        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Updates the textual field for the systolic blood pressures.
     * @param text The new text paragraphs with the information.
     */
    public void updateBloodPressureText(String text) {
        this.bloodPressureValues.setText(text);
    }
}
