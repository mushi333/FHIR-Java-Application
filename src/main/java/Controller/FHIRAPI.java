package Controller;// HAPI FHIR API
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

// UTL
import java.util.*;

/**
 * The class that used to interact with the HAPI FHIR API. Other classes will depend on this to get their necessary information.
 * @author Mujtaba Zahidi.
 */

public class FHIRAPI {
    // String attributes
    private String practitionerIdentifier;
    private String source;
    private String practitionerID;

    // Create a context and a client
    private FhirContext ctx;
    private String URL;
    private IGenericClient client;

    /**
     * The constructor will only create the external API based information, everything else is done by setters and getters.
     */
    public FHIRAPI() {
        // HAPI FHIR API
        this.ctx = FhirContext.forR4();
        this.URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
        this.client = ctx.newRestfulGenericClient(URL);
    }

    /**
     * Verifies the practitioner. This is used to make sure that the practitioner's id given is correct. Returns a boolean of its success.
     * @param id The id of the practitioner.
     * @return Boolean value indicating the success.
     */
    public boolean verifyPractitioner(String id) {
        try {
            // Peform a call to find a practitioner
            Practitioner practitioner = client.read().resource(Practitioner.class).withId(id).execute();
            return true;
        }
        catch (Exception e) {
            // 404 error call means that it does not exist or it was not correct id
            return false;
        }
    }

    /**
     * This method returns all the patient ids for the practitioner to use. Bases it on encounters with the practitioner.
     * @return An arraylist of the patient ids.
     */
    public ArrayList<String> getPatientListsURL() {
        ArrayList<String> patientList = new ArrayList();
        ArrayList<IBaseResource> encounterList = new ArrayList();

        // Once an exception happens, we assume the patient list is done
        try {
            // Perform a search to find the patients
            Bundle bundle = this.client
                    .search()
                    .byUrl(this.URL + "Encounter?participant.identifier=" + this.source + "|" + this.practitionerIdentifier)
                    .returnBundle(Bundle.class)
                    .execute();

            // Load the subsequent pages
            while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {

                // Only allows new patients to be added
                for(Bundle.BundleEntryComponent entry: bundle.getEntry()) {
                    Encounter encounter = (Encounter) entry.getResource();
                    String patientURL = encounter.getSubject().getReference();
                    patientList.add(patientURL.substring(8));
                }

                // Loads next bundle
                bundle = this.client
                        .loadPage()
                        .next(bundle)
                        .execute();
                encounterList.addAll(BundleUtil.toListOfResources(ctx, bundle));
            }
        }
        catch (Exception e) {
            System.out.println("Done extracting patient list");
        }

        // Remove repeated IDs
        Set<String> newSet = new LinkedHashSet<String>(patientList);
        patientList.clear();
        patientList.addAll(newSet);
        return patientList;
    }

    /**
     * Sets the practitioners id, this is used in setting the practitioner's identifier.
     * @param practitionerID Practitioners id.
     */
    public void setPractitionerID(String practitionerID) {
        // Calls to get practitioner's info
        Practitioner practitioner = this.client.read().resource(Practitioner.class).withId(practitionerID).execute();

        // Sets the respective values
        this.practitionerID = practitionerID;
        this.source = practitioner.getIdentifier().get(0).getSystem();
        this.practitionerIdentifier = practitioner.getIdentifier().get(0).getValue();

        System.out.println("Set identifier and source values");
    }

    /**
     * This retrieves the name, gender, address, birth date and the id of the patient.
     * @param patientID The id of the patient.
     * @return Returns a String array containing the aforementioned information.
     */
    public String[] getPatientInfo(String patientID) {
        // Gets the patients information
        Patient patient = this.client.read().resource(Patient.class).withId(patientID).execute();

        // Stores the patients information into a 4 variable array, where 1st element is the id, then the name, then
        // the gender and finally the address.
        String id = patientID;
        String name = patient.getName().get(0).getGivenAsSingleString() + " " + patient.getName().get(0).getFamily();
        String gender = patient.getGender().getDisplay();
        Date date = patient.getBirthDate();
        String birthDate = date.toString();
        birthDate = birthDate.substring(0, 11) + birthDate.substring(25, 29);
        String address = patient.getAddress().get(0).getLine().get(0) + ", " +
                patient.getAddress().get(0).getCity() + ", " +
                patient.getAddress().get(0).getState() + ", " +
                patient.getAddress().get(0).getCountry();
        String[] returnString = {id, name, gender, birthDate, address};
        return returnString;
    }

    /**
     * Gets the cholesterol level and the effective date time of a specific patient.
     * @param patientID The id of the patient used.
     * @return A String array containing the cholesterol and the date time.
     */
    public String[] getPatientCholesterol(String patientID) {
        String effectiveDateTime = "";
        String cholesterol = "";

        // Searches for patients cholesterol details, returns the effective time and date as the first element of the
        // array and the cholesterol level as the second element.
        // Returns null if the cholesterol does not exist.
        try {
            Bundle bundle = this.client
                    .search()
                    .byUrl("Observation?patient=" + patientID + "&code=2093-3&_sort=-date&_count=13")
                    .returnBundle(Bundle.class)
                    .execute();

            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                Observation observation = (Observation) entry.getResource();
                DateTimeType dateTime = (DateTimeType) observation.getEffective();
                effectiveDateTime = dateTime.getValueAsString();
                Quantity quantity = (Quantity) observation.getValue();
                cholesterol =quantity.getValue() + "";
                break;
            }

            String[] returnString = {cholesterol, effectiveDateTime};
            return returnString;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the blood pressure levels and the effective date time of a specific patient.
     * @param patientID The id of the patient used.
     * @return Structure of return string array is 3 rows and 5 columns, where each row is the specific values
     * (diastolic, systolic and date) and each column is each blood pressure reading.
     */
    public String[][] getPatientBloodPressure(String patientID) {
        ArrayList<String> date = new ArrayList();
        ArrayList<String> bloodPressureDiastolic = new ArrayList();
        ArrayList<String> bloodPressureSystolic = new ArrayList();
        /* Structure of return string array is 3 rows and 5 columns, where each row is the specific values (diastolic,
            systolic and date) and each column is each blood pressure reading. */
        String[][] returnString = new String[3][5];


        // It looks through each bundle, finds each values and inserts them into the respective arraylists. then merges
        // them into the final return string array.
        try {
            Bundle bundle = this.client
                    .search()
                    .byUrl("Observation?patient=" + patientID + "&code=55284-4&_sort=-date&_count=5")
                    .returnBundle(Bundle.class)
                    .execute();

            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                Observation observation = (Observation) entry.getResource();
                DateTimeType dateTime = (DateTimeType) observation.getEffective();
                date.add(dateTime.getValueAsString());
                String diastolic = observation.getComponent().get(0).getValueQuantity().getValue() + "";
                bloodPressureDiastolic.add(diastolic);
                String systolic = observation.getComponent().get(1).getValueQuantity().getValue() + "";
                bloodPressureSystolic.add(systolic);
            }

            for (int i = 0; i < date.size(); i++) {
                returnString[0][i] = bloodPressureDiastolic.get(i);
                returnString[1][i] = bloodPressureSystolic.get(i);
                returnString[2][i] = date.get(i);
            }
            return returnString;
        }
        catch (Exception e) {
            return null;
        }
    }
}