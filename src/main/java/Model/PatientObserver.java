package Model;

import Controller.FHIRAPI;

/**
 * The patient observer interface, used in the Observer Design Pattern.
 */

public interface PatientObserver {
    public void updateCholesterol(FHIRAPI api);
    public void updateBloodPressure(FHIRAPI api);
}
