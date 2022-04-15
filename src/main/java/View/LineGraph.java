package View;

import Model.AppPatient;
import Model.PatientList;
import Model.PatientListIterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 *  Creates the systolic blood pressure line graph panel so the practitioner can
 *  monitor their patients in a graphical form for an overall picture.
 *
 *  @author Mujtaba Zahidi
 */
public class LineGraph extends JPanel {

    // List of patients
    private PatientList monitoredPatients;

    /**
     * Creates the line graph panel of systolic blood pressure values
     * @param monitoredPatients The patients currently monitored
     * @param dimension The desired dimensions of the graph
     */
    public LineGraph(PatientList monitoredPatients, Dimension dimension) {
        super();

        // Set instance variable
        this.monitoredPatients = monitoredPatients;

        // Create Dataset
        CategoryDataset dataset = createDataset();

        //Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Systolic Blood Pressure (mm/Hg)", //Chart Title
                "Intervals", // Category axis
                "Systolic Blood Pressures", // Value axis
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false
        );

        // Create the chart and setup dimensions
        ChartPanel panel = new ChartPanel(chart);
        panel.setDomainZoomable(false);
        panel.setRangeZoomable(false);
        panel.setMinimumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setMaximumSize(dimension);
        this.add(panel);
    }

    /**
     *  Creates the dataset used for the line graph
     */
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Iterate through all patients
        PatientListIterator patientListIterator = monitoredPatients.createIterator();

        if (monitoredPatients.count() > 0) {
            // Current patient
            AppPatient patient = patientListIterator.currentItem();

            // Check to see if it has blood pressure values
            if (patient.getSystolicBloodPressure() != null) {
                // Retrieve each value
                String name = patient.getName();
                String[] values = patient.getSystolicBloodPressure();

                // Parse blood pressure level
                float bloodPressureLevel = 0;

                // Loop through each values and dates
                for (int i = 0; i < values.length; i++) {
                    try {
                        if (!values[i].equals("N/A")) {
                            bloodPressureLevel = Float.parseFloat(values[i]);
                            // Add patient to graph
                            dataset.addValue(bloodPressureLevel, name, "" + (i + 1));
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                while (!patientListIterator.isDone()) {
                    // Current patient
                    patient = patientListIterator.next(); // increment

                    // Retrieve each value
                    name = patient.getName();
                    values = patient.getSystolicBloodPressure();

                    // Parse blood pressure level
                    bloodPressureLevel = 0;

                    // Loop through each values and dates
                    for (int i = 0; i < values.length; i++) {
                        try {
                            if (!values[i].equals("N/A")) {
                                bloodPressureLevel = Float.parseFloat(values[i]);
                                // Add patient to graph
                                dataset.addValue(bloodPressureLevel, name, "" + (i + 1));
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }

            return dataset;
        }
        return null;
    }
}
