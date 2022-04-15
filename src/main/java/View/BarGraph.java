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
 *  Creates the cholesterol bar graph panel so the practitioner can
 *  monitor their patients in a graphical form for an overall picture.
 *
 *  @author Kenneth Huynh
 */
public class BarGraph extends JPanel {

    // List of patients
    private final PatientList MONITORED_PATIENTS;

    /**
     * Creates the bar graph panel of cholesterol values
     * @param monitoredPatients The patients currently monitored
     * @param dimension The desired dimensions of the graph
     */
    public BarGraph(PatientList monitoredPatients, Dimension dimension) {
        super();

        // Set instance variable
        this.MONITORED_PATIENTS = monitoredPatients;

        // Create Dataset
        CategoryDataset dataset = createDataset();

        //Create chart
        JFreeChart chart= ChartFactory.createBarChart(
                "Total Cholesterol (mg/dL)", //Chart Title
                "Patients", // Category axis
                "Cholesterol Levels", // Value axis
                dataset,
                PlotOrientation.VERTICAL,
                true,false,false
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
     *  Creates the dataset used for the bar graph
     */
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Iterate through all patients
        PatientListIterator patientListIterator = MONITORED_PATIENTS.createIterator();

        if (MONITORED_PATIENTS.count() > 0) {
            // Current patient
            AppPatient patient = patientListIterator.currentItem();

            // Parse cholesterol level
            float cholesterolLevel = 0;
            if (!patient.getCholesterolLevel().equals("N/A")) {
                cholesterolLevel = Float.parseFloat(patient.getCholesterolLevel());
            }

            // Add patient to graph
            dataset.addValue(cholesterolLevel, patient.getName(), patient.getName());

            while (!patientListIterator.isDone()) {
                // Current patient
                patient = patientListIterator.next(); // increment

                // Parse cholesterol level
                cholesterolLevel = 0;
                if (!patient.getCholesterolLevel().equals("N/A")) {
                    cholesterolLevel = Float.parseFloat(patient.getCholesterolLevel());
                }

                // Add patient to graph
                dataset.addValue(cholesterolLevel, patient.getName(), patient.getName());
            }
        }

        return dataset;
    }
}
