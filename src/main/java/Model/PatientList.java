package Model;

import java.util.ArrayList;

/**
 *  List implementation for practitioner's patients.
 *
 * @author Kenneth Huynh
 */
public class PatientList implements AbstractList {
    ArrayList<AppPatient> patients;

    /**
     *  Setup patient list.
     */
    public PatientList() {
        patients = new ArrayList<AppPatient>();
    }

    /**
     *  Copy constructor.
     */
    public PatientList(PatientList patientList) {
        patients = patientList.patients;
    }

    /**
     *  Creates a new iterator of for the patient list.
     *
     *  @return A new iterator for the patient list
     */
    public PatientListIterator createIterator() {
        return new PatientListIterator(this);
    }

    /**
     *  Returns the amount of patients in the patients list.
     *
     *  @return The patients list size
     */
    public int count() {
        return patients.size();
    }

    /**
     *  Appends a patient to the patient list.
     *
     *  @param patient: The patient to be appended onto the patient list
     *  @return If the addition of the patient was possible
     */
    public boolean append(Object patient) {
        patients.add((AppPatient) patient);
        return true;
    }

    /**
     *  Removes a patient from the patient list.
     *
     *  @param patient:  The patient to be removed from the patient list
     *  @return If the removal of the patient was possible
     */
    public boolean remove(Object patient) {
        if (patients.contains((AppPatient) patient)) {
            return false;
        } else {
            patients.remove((AppPatient) patient);
            return true;
        }
    }

    /**
     *  Finds the index of the patient in the list.
     *
     *  @param patient:  The patient used to find the index
     *  @return The index of the patient within the list
     */
    public int indexOf(Object patient) {
        if (patients.contains((AppPatient) patient)) {
            return patients.indexOf((AppPatient) patient);
        }

        return -1;  // If not found in list
    }
}
