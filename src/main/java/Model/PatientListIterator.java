package Model;

/**
 *  Patient list iterator to iterate through a list of patients.
 *
 * @author Kenneth Huynh
 */
public class PatientListIterator implements Iterator {
    private final PatientList PATIENTS;
    private int currentIndex;

    /**
     *  Sets up the patient list iterator.
     */
    public PatientListIterator(AbstractList patientList) {
        PATIENTS = (PatientList)patientList;
        currentIndex = 0;
    }

    /**
     *  Returns the next patient in the patient list.
     *
     *  @return The next patient
     */
    public AppPatient next() throws ArrayIndexOutOfBoundsException  {
        if (isDone()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        currentIndex++;
        return currentItem();
    }

    /**
     *  Returns whether the iterator has reached the end of the patient list or not.
     *
     *  @return A boolean value
     */
    public boolean isDone() {
        int nextIndex = currentIndex + 1;
        return nextIndex == PATIENTS.patients.size();
    }

    /**
     *  Returns the current patient in the patient list.
     *
     *  @return The current patient
     */
    public AppPatient currentItem() {
        return PATIENTS.patients.get(currentIndex);
    }
}
