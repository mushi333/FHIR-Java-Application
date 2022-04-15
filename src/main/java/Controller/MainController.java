package Controller;

import Model.AppPatient;
import Model.AppPractitioner;
import View.DashboardGUI;
import View.LoginGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class controls the flow of the program by responding to the user's interactions.
 * @author Kenneth Huynh, Mujtaba Zahidi
 */
public class MainController {
    // The login and dashboard GUI attributes
    private final LoginGUI LOGIN;
    private DashboardGUI dashboard;
    private AppPractitioner practitioner;
    private final JFrame APPFRAME;

    // API class
    private final FHIRAPI API;

    // Timer
    public ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * The constructor. Here it initially instantiates the login class and the API.
     */
    public MainController() {
        // Creating the app frame
        APPFRAME = new JFrame();
        this.LOGIN = new LoginGUI(APPFRAME);

        this.LOGIN.addLoginListener(new LoginListener());
        this.API = new FHIRAPI();
    }

    /**
     * This listener class implements the login controller functionality.
     */
    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String id = LOGIN.getPractitionerIDText();

            LOGIN.changeLoginFeedback(true); // Give users feedback

            // Search for specific practitioner's identifier
            boolean loginSuccessful = API.verifyPractitioner(id);

            // Returns true if practitioner exists
            if (loginSuccessful) {
                // Remove components
                LOGIN.removeLoginPanelFromFrame();

                // Create practitioner to pass into View.DashboardGUI constructor
                practitioner = new AppPractitioner(id);

                // Move to home panel
                dashboard = new DashboardGUI(APPFRAME, practitioner);

                dashboard.addCholesterolSwitchListener(new CholesterolSwitchListener());
                dashboard.addBloodPressureSwitchListener(new BloodPressureSwitchListener());
                dashboard.addSystolicSetterListener(new SystolicListener());
                dashboard.addDiastolicSetterListener(new DiastolicSetterListener());

                // Listeners for all patients panel
                dashboard.allPatientsPanel.addPatientAddingListener(new PatientAddingListener());
                dashboard.allPatientsPanel.addPatientRemovingListener(new PatientRemovingListener());

                // Listeners for patient details panel
                dashboard.patientDetailsPane.addRefreshListener(new RefreshListener());

            } else {
                LOGIN.changeLoginFeedback(false);
            }
            System.out.println("Logging in.");
        }
    }

    /**
     * This listener class implements the refresh controller functionality.
     */
    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Error handling for integers
            try {
                int refreshRate = Integer.parseInt(dashboard.patientDetailsPane.getRefreshRateText());
                if (refreshRate > 0) {
                    dashboard.patientDetailsPane.setRefreshRateFeedback("New refresh rate: " + refreshRate + " seconds.");
                    System.out.println("New refresh: " + refreshRate + " seconds.");

                    Runnable runnable = new Runnable() {
                        public void run() {
                            practitioner.notifyObserver();
                            dashboard.refreshMonitorTableModel();
                            dashboard.updateBloodPressureText(practitioner.createBloodPressureDetails());
                        }
                    };

                    executor.scheduleAtFixedRate(runnable, 0, refreshRate, TimeUnit.SECONDS);

                } else {
                    dashboard.patientDetailsPane.setRefreshRateFeedback("Enter a positive integer.");
                    System.out.println("Entry was not a positive integer.");
                }
            } catch (NumberFormatException exception) {
                dashboard.patientDetailsPane.setRefreshRateFeedback("Enter a positive integer.");
                System.out.println("Entry was not a positive integer.");
            }

        }
    }

    /**
     * This listener class implements the patient adding to cholesterol list controller functionality.
     */
    class PatientAddingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Cholesterol table should contain unique patients
            try {
                int selectedPatientIndex = dashboard.allPatientsPanel.getSelectedRowInAllPatientsTable();
                AppPatient patient = practitioner.getPatientFromAllPatientsList(selectedPatientIndex);
                if (dashboard.addPatientToMonitorTable(patient)) {
                    System.out.println("Added new patient.");
                } else {
                    System.out.println("Patient already added.");
                }
            } catch (Exception exception) {
                System.out.println("Patient already added.");
            }
        }
    }

    /**
     * This listener class implements the patient removal to cholesterol list controller functionality.
     */
    class PatientRemovingListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Remove patient if it exists in the table
            try {
                int selectedPatientIndex = dashboard.allPatientsPanel.getSelectedRowInAllPatientsTable();
                AppPatient patient = practitioner.getPatientFromAllPatientsList(selectedPatientIndex);

                if (dashboard.removePatientFromMonitorTable(patient)) {
                    System.out.println("Patient removed.");
                } else {
                    System.out.println("Patient not found in table.");
                }
            } catch (Exception exception) {
                System.out.println("Patient not found in table.");
            }
        }
    }

    /**
     * This listener class implements the cholesterol switch controller functionality.
     */
    class CholesterolSwitchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Switch cholesterol to be seen/hidden
            dashboard.switchCholesterolState();
            System.out.println("Cholesterol state switched.");

            dashboard.refreshMonitorTableModel();
        }
    }

    /**
     * This listener class implements the blood pressure switch controller functionality.
     */
    class BloodPressureSwitchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Switch blood pressure to be seen/hidden
            dashboard.switchBloodPressureState();
            System.out.println("Blood pressure state switched.");

            dashboard.refreshMonitorTableModel();
        }
    }

    /**
     * This listener class implements the systolic amount controller functionality.
     */
    class SystolicListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Error handling for integers
            try {
                int systolicValue = Integer.parseInt(dashboard.getSystolicText());
                if (systolicValue > 0) {
                    dashboard.setBloodPressureFeedback("New systolic reading: " + systolicValue);
                    System.out.println("New systolic reading: " + systolicValue);

                    dashboard.setSystolicReading(systolicValue);
                    dashboard.refreshMonitorTableModel();

                } else {
                    dashboard.setBloodPressureFeedback("Enter a positive integer.");
                    System.out.println("Entry was not a positive integer.");
                }
            } catch (NumberFormatException exception) {
                dashboard.setBloodPressureFeedback("Enter a positive integer.");
                System.out.println("Entry was not a positive integer.");
            }

        }
    }

    /**
     * This listener class implements the systolic amount controller functionality.
     */
    class DiastolicSetterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Error handling for integers
            try {
                int diastolicValue = Integer.parseInt(dashboard.getDiastolicText());
                if (diastolicValue > 0) {
                    dashboard.setBloodPressureFeedback("New diastolic reading: " + diastolicValue);
                    System.out.println("New diastolic reading: " + diastolicValue);

                    dashboard.setDiastolicReading(diastolicValue);
                    dashboard.refreshMonitorTableModel();

                } else {
                    dashboard.setBloodPressureFeedback("Enter a positive integer.");
                    System.out.println("Entry was not a positive integer.");
                }
            } catch (NumberFormatException exception) {
                dashboard.setBloodPressureFeedback("Enter a positive integer.");
                System.out.println("Entry was not a positive integer.");
            }

        }
    }
}
