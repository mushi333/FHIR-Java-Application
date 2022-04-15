package Model;

import Controller.FHIRAPI;

import java.util.ArrayList;

/**
 * The seperate practitioner class to the FHIR API. This class contains the practitioner's id, a list of the patients and another list containing the monitored patients.
 * @author Mujtaba Zahidi, Kenneth Huynh
 */
public class AppPractitioner implements PractitionerSubject {
    private final PatientList patientList;
    private final MonitorPatientList monitorPatientList;
    private final FHIRAPI api;

    /**
     * The constructor will only need the practitioner's id. Everything is set by default.
     * @param id The id of the practitioner. This is used for the api calls.
     */
    public AppPractitioner(String id){
        patientList = new PatientList();
        monitorPatientList = new MonitorPatientList();

        // Setup API with practitioner
        api = new FHIRAPI();
        api.setPractitionerID(id);

        getAllPatientsFromAPI();
    }

    /**
     * This method retrieves all the necessary patient information through the api calls and sets it to the patient list.
     */
    private void getAllPatientsFromAPI() {
        // Retrieve list of patient urls to use
        ArrayList<String> patientURLS = api.getPatientListsURL();

        // Loop through the patient urls and create each patient
        PatientFactory factory = new ConcretePatientFactory();
        for (int i = 0; i < patientURLS.size(); i++) {
            String[] info = api.getPatientInfo(patientURLS.get(i));
            AppPatient patient;

            // Retrieve the cholesterol and base information
            String[] cholesterolCall = api.getPatientCholesterol(patientURLS.get(i));
            String cholesterol;
            String effectiveTimeDateCholesterol;
            if (cholesterolCall == null) {
                cholesterol = "N/A";
                effectiveTimeDateCholesterol = "N/A";
            }
            else {
                // Check for empty strings
                if (cholesterolCall[0].equals("")) {
                    cholesterol = "N/A";
                    effectiveTimeDateCholesterol = "N/A";
                } else {
                    cholesterol = cholesterolCall[0];
                    effectiveTimeDateCholesterol = cholesterolCall[1];
                }
            }

            // Retrieve the blood pressure values
            String[][] bloodPressureCall = api.getPatientBloodPressure(patientURLS.get(i));
            String[] diastolic = {};
            String[] systolic = {};
            String[] effectiveDateTimeBloodPressure = {};
            // Checks to see if it is not empty
            if (bloodPressureCall == null) {
                diastolic = null;
                systolic = null;
                effectiveDateTimeBloodPressure = null;
            }
            else {
                diastolic = bloodPressureCall[0];
                systolic = bloodPressureCall[1];
                effectiveDateTimeBloodPressure = bloodPressureCall[2];
            }

            patient = factory.createPatient(info[0], info[1], info[2], info[3], info[4], cholesterol,
                    effectiveTimeDateCholesterol, diastolic, systolic, effectiveDateTimeBloodPressure);
            patientList.append(patient);
        }
    }

    /**
     * Observer design pattern method. This notifies all the patients to update their cholesterol informations.
     */
    public void notifyObserver() {
        // Uses an iterator to find the patient
        PatientListIterator patientListIterator = monitorPatientList.createIterator();

        if (monitorPatientList.count() > 0) {
            AppPatient currPatient = patientListIterator.currentItem();
            currPatient.updateCholesterol(api);
            currPatient.updateBloodPressure(api);
            while (!patientListIterator.isDone()) {
                currPatient = patientListIterator.next();
                currPatient.updateCholesterol(api);
                currPatient.updateBloodPressure(api);
            }
        }
    }

    /**
     * Getter for all patients list.
     */
    public PatientList getAllPatients() {
        return new PatientList(patientList);
    }

    /**
     * Getter for cholesterol patient list.
     */
    public MonitorPatientList getMonitorPatientList() {
        return new MonitorPatientList(monitorPatientList);
    }

    /**
     * Getter for a specific patient in the cholesterol patients list.
     *
     * @param patientIndex: the patient to find
     */
    public AppPatient getPatientFromMonitorList(int patientIndex) {
        // Uses an iterator to find the cholesterol patient
        PatientListIterator cholesterolIterator = monitorPatientList.createIterator();

        if (monitorPatientList.count() > 0) {
            AppPatient currPatient = cholesterolIterator.currentItem();
            for (int i = 0; i < patientIndex; i++) {
                currPatient = cholesterolIterator.next();
            }
            return currPatient;
        }
        return null;
    }

    /**
     * Appends a new patient to the cholesterol list for monitoring.
     *
     * @param patient: the patient to be appended
     */
    public boolean addPatientToMonitorList(AppPatient patient) {
        return monitorPatientList.append(patient);
    }

    /**
     * Removes a patient from the cholesterol list to stop monitoring.
     *
     * @param patient: the patient to be removed
     */
    public boolean removePatientFromMonitorList(AppPatient patient) {
        return monitorPatientList.remove(patient);
    }

    /**
     * Getter for a specific patient in the all patients list.
     *
     * @param patientIndex: the patient to find
     */
    public AppPatient getPatientFromAllPatientsList(int patientIndex) {
        // Uses an iterator to find the patient
        PatientListIterator patientListIterator = patientList.createIterator();

        if (patientList.count() > 0) {
            AppPatient currPatient = patientListIterator.currentItem();
            for (int i = 0; i < patientIndex; i++) {
                currPatient = patientListIterator.next();
            }
            return currPatient;
        }
        return null;
    }

    /**
     * Creates the string for the blood pressure details needed for the textual requirement 4, showing systolic values and its time.
     */
    public String createBloodPressureDetails() {
        // Uses an iterator to find the patient
        PatientList cholesterolPatientList = getMonitorPatientList();
        PatientListIterator patientListIterator = cholesterolPatientList.createIterator();
        String values = "";
        // Checks to see if the list is not empty
        if (cholesterolPatientList.count() > 0) {
            AppPatient currPatient = patientListIterator.currentItem();
            String[] systolic = currPatient.getSystolicBloodPressure();
            String[] time = currPatient.getEffectiveDateTimeBloodPressure();
            // Checks to see if the patient has blood pressure values
            if (systolic != null) {
                values += currPatient.getName() + ": ";
                for (int i = time.length  - 1; i > 0; i--) {
                    // checks to see if it has current blood pressure value
                    if (systolic[i] != null)
                        values += systolic[i] + " (" + time[i] + "), ";
                }
                values += "\n";
                while (!patientListIterator.isDone()) {
                    currPatient = patientListIterator.next();
                    systolic = currPatient.getSystolicBloodPressure();
                    time = currPatient.getEffectiveDateTimeBloodPressure();
                    // Checks to see if the patient has blood pressure values

                    if (systolic != null) {
                        values += currPatient.getName() + ": ";
                        for (int i = time.length - 1; i > 0; i--) {
                            // checks to see if it has current blood pressure value

                            if (systolic[i] != null)
                                values += systolic[i] + " (" + time[i] + "), ";
                        }
                    }
                    values += "\n";
                }
            }
        }
        return values;
    }
}