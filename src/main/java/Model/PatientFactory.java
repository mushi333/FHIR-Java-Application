package Model;
import Model.AppPatient;

/***
 * The abstract patient factory class.
 *
 * @author Mujtaba Zahidi
 */

public interface PatientFactory {
    public AppPatient createPatient(String id, String name, String gender, String birthDate,
                                    String address, String cholesterol, String effectiveTimeDateCholesterol,
                                    String[] diastolic,String[] systolic,String[] effectiveDateTimeBloodPressure);
}
