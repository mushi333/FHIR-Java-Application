package Model;

import java.util.ArrayList;

/**
 *  List implementation for practitioner's monitored patients.
 *
 * @author Kenneth Huynh
 */
public class MonitorPatientList extends PatientList {

    // Instance variables
    private float totalCholesterol;
    private float averageCholesterol;
    private int patientsNotYetMeasured;

    /**
     *  Setup needed the total and average cholesterol for future calculation.
     */
    public MonitorPatientList() {
        super();

        patients = new ArrayList<AppPatient>();
        totalCholesterol = 0;
        averageCholesterol = 0;
        patientsNotYetMeasured = 0;
    }

    /**
     *  Copy constructor.
     */
    public MonitorPatientList(MonitorPatientList monitorPatientList) {
        super();

        patients = monitorPatientList.patients;
        totalCholesterol = monitorPatientList.totalCholesterol;
        averageCholesterol = monitorPatientList.averageCholesterol;
    }

    /**
     *  Appends a patient to the patient list and updates the cholesterol statistics.
     *
     *  @param patient: The patient to be appended onto the patient list
     *  @return If the addition of the patient was possible
     */
    @Override
    public boolean append(Object patient) {
        if (patients.contains((AppPatient) patient)) {
            return false;
        } else {
            patients.add((AppPatient) patient);

            // Update total and average cholesterol
            if (!((AppPatient) patient).getCholesterolLevel().equals("N/A")) {
                totalCholesterol += Float.parseFloat(((AppPatient) patient).getCholesterolLevel());
                averageCholesterol = totalCholesterol / (this.count() - patientsNotYetMeasured);
            } else {
                patientsNotYetMeasured++;
            }

            return true;
        }
    }

    /**
     *  Removes a patient from the patient list and updates the cholesterol statistics.
     *
     *  @param patient:  The patient to be removed from the patient list
     *  @return If the removal of the patient was possible
     */
    @Override
    public boolean remove(Object patient) {
        if (patients.contains((AppPatient) patient)) {
            patients.remove((AppPatient) patient);

            // Update total and average cholesterol
            if (!((AppPatient) patient).getCholesterolLevel().equals("N/A")) {
                totalCholesterol -= Float.parseFloat(((AppPatient) patient).getCholesterolLevel());
                if (totalCholesterol > 0) {
                    averageCholesterol = totalCholesterol / (this.count() - patientsNotYetMeasured);
                } else {
                    // If precision of parse is not good enough
                    totalCholesterol = 0;
                    averageCholesterol = 0;
                }
            } else {
                patientsNotYetMeasured--;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     *  Getter for the average cholesterol statistic.
     *
     *  @return The average cholesterol value
     */
    public float getAverageCholesterol() {
        return averageCholesterol;
    }
}
