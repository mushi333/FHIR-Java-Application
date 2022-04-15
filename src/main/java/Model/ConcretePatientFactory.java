package Model;

/***
 * The concrete patient factory class, this creates the patients.
 *
 * @author Mujtaba Zahidi
 */

public class ConcretePatientFactory implements PatientFactory {

    public ConcretePatientFactory() {
    }

    /**
     * Creates a patient class with the given id, name, gender, birth date and address. All values are String based.
     * @param id Patient's id.
     * @param name Patient's name.
     * @param gender Patient's gender.
     * @param birthDate Patient's birth date.
     * @param address Patient's address.
     * @return a new patient with the given details.
     */
    public AppPatient createPatient(String id, String name, String gender, String birthDate,
                                    String address, String cholesterol, String effectiveTimeDateCholesterol,
                                    String[] diastolic,String[] systolic,String[] effectiveDateTimeBloodPressure) {
        AppPatient patient = new AppPatient();
        patient.setId(id);
        patient.setName(name);
        patient.setGender(gender);
        patient.setBirthDate(birthDate);
        patient.setAddress(address);
        patient.setCholesterolLevel(cholesterol);
        patient.setEffectiveDateTimeCholesterol(effectiveTimeDateCholesterol);
        patient.setDiastolicBloodPressure(diastolic);
        patient.setSystolicBloodPressure(systolic);
        patient.setEffectiveDateTimeBloodPressure(effectiveDateTimeBloodPressure);
        return patient;
    }
}
