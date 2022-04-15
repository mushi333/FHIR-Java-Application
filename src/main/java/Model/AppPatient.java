package Model;

import Controller.FHIRAPI;

/**
 * The patient class that holds the patient's details. This class is different to the HAPI FHIR API Patient class.
 * @author Mujtaba Zahidi
 * @author Kenneth Huynh
 */

public class AppPatient implements PatientObserver {
    private String id;
    private String name;
    private String cholesterolLevel;
    private String effectiveDateTimeCholesterol;
    private String birthDate;
    private String gender;
    private String address;
    // These array values go from 0 to 4 and go from latest to oldest.
    private String[] systolicBloodPressure;
    private String[] diastolicBloodPressure;
    private String[] effectiveDateTimeBloodPressure;

    public AppPatient() {}

    /**
     * The observer design pattern update method. Updates the values of the cholesterol and effective date and time of the patient
     * @param api Requires the current api object to be passed. This means it has a dependency relationship with the Controller.FHIRAPI class.
     */
    public void updateCholesterol(FHIRAPI api) {
        String[] cholesterolCall = api.getPatientCholesterol(this.getId());

        // Checks to see if it is not empty
        if (cholesterolCall == null) {
            this.setCholesterolLevel("N/A");
            this.setEffectiveDateTimeCholesterol("N/A");
        }
        else {
            // Checks to see if the details have been correctly retrieved
            if (cholesterolCall[0].equals("")) {
                this.setCholesterolLevel("N/A");
                this.setEffectiveDateTimeCholesterol("N/A");
            } else {
                this.setCholesterolLevel(cholesterolCall[0]);
                this.setEffectiveDateTimeCholesterol(cholesterolCall[1]);
            }
        }

        System.out.println("Updated cholesterol information on patient: " + this.getName());
    }

    /**
     * The observer design pattern update method. Updates the values of the diastolic and systolic blood pressures and effective date and time of the patient.
     * @param api Requires the current api object to be passed. This means it has a dependency relationship with the Controller.FHIRAPI class.
     */
    public void updateBloodPressure(FHIRAPI api) {
        String[][] bloodPressureCall = api.getPatientBloodPressure(this.getId());

        // Checks to see if it is not empty
        if (bloodPressureCall == null) {
            this.setDiastolicBloodPressure(null);
            this.setSystolicBloodPressure(null);
            this.setEffectiveDateTimeBloodPressure(null);
        }
        else {
            this.setDiastolicBloodPressure(bloodPressureCall[0]);
            this.setSystolicBloodPressure(bloodPressureCall[1]);
            this.setEffectiveDateTimeBloodPressure(bloodPressureCall[2]);
        }

        System.out.println("Updated blood pressure information on patient: " + this.getName());
    }

    /**
     * Setter for patient ID.
     *
     * @param id: the ID of the patient
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for patient's name.
     *
     * @param name: the name of the patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for patient's cholesterol level.
     *
     * @param cholesterolLevel: the cholesterol level of the patient
     */
    public void setCholesterolLevel(String cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * Setter for cholesterol measurement effective time date.
     *
     * @param effectiveDateTimeCholesterol: the effective time date for the cholesterol level of the patient
     */
    public void setEffectiveDateTimeCholesterol(String effectiveDateTimeCholesterol) {
        this.effectiveDateTimeCholesterol = effectiveDateTimeCholesterol;
    }

    /**
     * Setter for patient's birth date.
     *
     * @param birthDate: the birth date of the patient
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Setter for patient's gender.
     *
     * @param gender: the gender of the patient
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Setter for patient's address.
     *
     * @param address: the address of the patient
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for patient ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for patient name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for patient's cholesterol level.
     */
    public String getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * Getter for patient's cholesterol level effective date time.
     */
    public String getEffectiveDateTimeCholesterol() {
        return effectiveDateTimeCholesterol;
    }

    /**
     * Getter for patient's birth date.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Getter for patient's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Getter for patient's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for systolic blood pressure.
     * @return String array with systolic blood pressures.
     */
    public String[] getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    /**
     * Setter for systolic blood pressure.
     * @param systolicBloodPressure String array of the systolic blood pressure.
     */
    public void setSystolicBloodPressure(String[] systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    /**
     * Getter for diastolic blood pressure.
     * @return String array with diastolic blood pressures.
     */
    public String[] getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    /**
     * Getter for the latest diastolic blood pressure.
     * @return String value of the latest diastolic blood pressure
     */
    public String getLatestDiastolicBloodPressure() {
        if (diastolicBloodPressure[0] == null) {
            return "N/A";
        }
        return diastolicBloodPressure[0];
    }

    /**
     * Getter for the latest systolic blood pressure.
     * @return String value of the latest systolic blood pressure
     */
    public String getLatestSystolicBloodPressure() {
        if (systolicBloodPressure[0] == null) {
            return "N/A";
        }
        return systolicBloodPressure[0];
    }

    /**
     * Getter for the latest date time for blood pressure.
     * @return String value of the latest date time for blood pressure
     */
    public String getLatestEffectiveDateTimeBloodPressure() {
        if (effectiveDateTimeBloodPressure[0] == null) {
            return "N/A";
        }
        return effectiveDateTimeBloodPressure[0];
    }

    /**
     * Setter for diastolic blood pressure.
     * @param diastolicBloodPressure String array of the diastolic blood pressure.
     */
    public void setDiastolicBloodPressure(String[] diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    /**
     * Getter for date and time of the blood pressures.
     * @return String array with date and time of the blood pressures.
     */
    public String[] getEffectiveDateTimeBloodPressure() {
        return effectiveDateTimeBloodPressure;
    }

    /**
     * Setter for date and time of the blood pressure.
     * @param effectiveDateTimeBloodPressure String array of date and time of the blood pressure.
     */
    public void setEffectiveDateTimeBloodPressure(String[] effectiveDateTimeBloodPressure) {
        this.effectiveDateTimeBloodPressure = effectiveDateTimeBloodPressure;
    }
}
