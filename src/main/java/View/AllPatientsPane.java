package View;

import Model.AppPatient;
import Model.PatientList;
import Model.PatientListIterator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *  Creates the all patients panel so the practitioner can
 *  pick a specific patient to monitor or remove from the monitor.
 *
 *  @author Kenneth Huynh
 */
public class AllPatientsPane extends JPanel {
    // View.GUI Home page
    private final JButton ADD_PATIENT_BUTTON;
    private final JButton REMOVE_PATIENT_BUTTON;

    private DefaultTableModel allPatientsTableModel;
    private JTable allPatientsTable;

    // List of patients
    private final PatientList ALL_PATIENTS;

    /**
     *  Sets up the panel with the table of all the practitioner's patients
     *  and its add and removal buttons.
     *
     *  @param allPatients: The list of monitored patients
     *  @param dimension:   The panel dimensions
     */
    public AllPatientsPane(PatientList allPatients, Dimension dimension) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.ALL_PATIENTS = allPatients;

        // Table title
        JLabel title = new JLabel("Patients");
        title.setFont(title.getFont().deriveFont(16f));    // change to bigger font
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(title);

        // Add patient table
        createAllPatientsTable();

        // Add button
        JPanel buttonPanel = new JPanel();
        ADD_PATIENT_BUTTON = new JButton("Add");
        buttonPanel.add(ADD_PATIENT_BUTTON);

        // Remove button
        REMOVE_PATIENT_BUTTON = new JButton("Remove");
        buttonPanel.add(REMOVE_PATIENT_BUTTON);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(buttonPanel);

        // View setup
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
    }

    /**
     *  Creates the table of all practitioner's patients.
     */
    public void createAllPatientsTable() {
        // Patient table column headings
        String[] columnNames = {"Name"};

        // Set up table data
        allPatientsTableModel = new DefaultTableModel(columnNames, 0);
        PatientListIterator patientListIterator = ALL_PATIENTS.createIterator();
        while (!patientListIterator.isDone()) {
            // Add patient name to the table
            AppPatient patient = patientListIterator.currentItem();
            Object[] row = {patient.getName()};
            allPatientsTableModel.addRow(row);

            // Check if the index can be incremented
            if (!patientListIterator.isDone()) {
                patientListIterator.next(); // increment
            }
        }

        allPatientsTable = new JTable(allPatientsTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disables cell editing
                return false;
            }
        };

        // Setup table
        allPatientsTable.getTableHeader().setReorderingAllowed(false); // Disables column dragging
        allPatientsTable.getTableHeader().setResizingAllowed(false); // Disable column resizing
        allPatientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Disable multiple row selection
        allPatientsTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(allPatientsTable);

        // Add the scroll pane to this practitionerLoginFieldPanel.
        this.add(scrollPane);
    }

    /**
     * Adds listener for the adding patient button.
     * @param listener The listener class used to control the adding patients button's action.
     */
    public void addPatientAddingListener(ActionListener listener){
        ADD_PATIENT_BUTTON.addActionListener(listener);
    }

    /**
     * Adds listener for the removing patient button.
     * @param listener The listener class used to control the removing patients button's action.
     */
    public void addPatientRemovingListener(ActionListener listener){
        REMOVE_PATIENT_BUTTON.addActionListener(listener);
    }

    /**
     * Gets the selected row within the All Patients Table.
     * @return The index of the selected row
     */
    public int getSelectedRowInAllPatientsTable() {
        return allPatientsTable.getSelectedRow();
    }
}
