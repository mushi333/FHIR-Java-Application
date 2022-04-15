package View;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *  Creates the patients details panel so the practitioner can
 *  view a specific patient in further detail and also set the
 *  desired refresh rate of the monitor.
 *
 *  @author Kenneth Huynh
 */
public class PatientDetailsPane extends JPanel {

    // Refresh rate section
    private JButton setRefreshRateButton;
    private JTextField refreshRateTextField;
    private JLabel refreshRateFeedback;

    // Patient details section
    private JLabel birthDateLabel;
    private JLabel genderLabel;
    private JTextArea addressArea;

    /**
     *  Sets up the refresh rate and patient details section.
     */
    public PatientDetailsPane(Dimension dimension) {
        super();

        // Layout setup
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Spacing
        this.add(Box.createVerticalGlue());

        // Create panels
        this.createRefreshRatePanel();
        this.createPatientInformationPanel();

        // Spacing
        this.add(Box.createVerticalGlue());

        // View setup
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
    }

    /**
     * Creates the panel which controls the refresh rate of the monitor
     */
    private void createRefreshRatePanel() {
        // Refresh rate text field
        JPanel refreshRatePanel = new JPanel();
        refreshRateTextField = new JTextField("", 8);
        refreshRatePanel.add(refreshRateTextField);

        // Refresh rate set button
        setRefreshRateButton = new JButton("Set");
        refreshRatePanel.add(setRefreshRateButton);
        refreshRatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Refresh rate warning label
        refreshRateFeedback = new JLabel("          ");
        refreshRateFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Refresh rate section
        JPanel fullRefreshRatePanel = new JPanel();
        fullRefreshRatePanel.setLayout(new BoxLayout(fullRefreshRatePanel, BoxLayout.Y_AXIS));
        fullRefreshRatePanel.add(refreshRatePanel);
        fullRefreshRatePanel.add(refreshRateFeedback);

        // Black border with heading
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        TitledBorder borderTitle = BorderFactory.createTitledBorder(blackLine, "Refresh rate");
        fullRefreshRatePanel.setBorder(borderTitle);

        this.add(fullRefreshRatePanel);
    }

    /**
     * Creates the panel that showcases the patients information
     */
    private void createPatientInformationPanel() {
        // Birth date Panel
        JPanel birthDatePanel = new JPanel();
        birthDatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel birthdatePrefix = new JLabel("Birth Date: ");
        birthDatePanel.add(birthdatePrefix);
        birthDateLabel = new JLabel("");
        birthDateLabel.setFont(birthDateLabel.getFont().deriveFont(Font.PLAIN));    // change font
        birthDatePanel.add(birthDateLabel);

        // Gender Panel
        JPanel genderPanel = new JPanel();
        genderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel genderPrefix = new JLabel("Gender: ");
        genderPanel.add(genderPrefix);
        genderLabel = new JLabel("");
        genderLabel.setFont(genderLabel.getFont().deriveFont(Font.PLAIN));    // change font
        genderPanel.add(genderLabel);

        // Address Panel
        JPanel addressPanel = new JPanel();
        addressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel addressPrefix = new JLabel("Address: ");
        addressPanel.add(addressPrefix);
        addressArea = new JTextArea(5, 20);
        addressArea.setText("");
        addressArea.setWrapStyleWord(true);
        addressArea.setLineWrap(true);
        addressArea.setOpaque(false);
        addressArea.setEditable(false);
        addressArea.setFocusable(false);
        addressArea.setBackground(UIManager.getColor("Label.background"));
        addressArea.setFont(UIManager.getFont("Label.font.plain"));    // change font
        addressArea.setBorder(UIManager.getBorder("Label.border"));
        addressPanel.add(addressArea);
        addressPanel.setMinimumSize(new Dimension(250, 150));
        addressPanel.setMaximumSize(new Dimension(250, 150));
        addressPanel.setPreferredSize(new Dimension(250, 150));

        // Add to patient details panel
        JPanel patientDetailsPanel = new JPanel();
        patientDetailsPanel.setLayout(new BoxLayout(patientDetailsPanel, BoxLayout.Y_AXIS));
        patientDetailsPanel.add(birthDatePanel);
        patientDetailsPanel.add(genderPanel);
        patientDetailsPanel.add(addressPanel);
        patientDetailsPanel.setMinimumSize(new Dimension(250, 350));
        patientDetailsPanel.setMaximumSize(new Dimension(250, 350));
        patientDetailsPanel.setPreferredSize(new Dimension(250, 350));

        // Black border with heading
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        TitledBorder borderTitle = BorderFactory.createTitledBorder(blackLine, "Patient Details");
        patientDetailsPanel.setBorder(borderTitle);

        this.add(patientDetailsPanel);
    }

    /**
     * Setter for the text for the refresh rate feedback.
     * @param feedback Feedback message
     */
    public void setRefreshRateFeedback(String feedback) {
        refreshRateFeedback.setText(feedback);
    }

    /**
     * Getter for the text within the refresh rate Text Field.
     * @return The refresh rate in seconds as a string
     */
    public String getRefreshRateText() {
        return refreshRateTextField.getText();
    }

    /**
     * Setter for the birth date label
     * @param birthDate The birthdate of the selected monitored patient
     */
    public void setBirthDateLabel(String birthDate) {
        birthDateLabel.setText(birthDate);
    }

    /**
     * Setter for the gender label
     * @param gender The gender of the selected monitored patient
     */
    public void setGenderLabel(String gender) {
        genderLabel.setText(gender);
    }

    /**
     * Setter for the address label
     * @param address The address of the selected monitored patient
     */
    public void setAddressArea(String address) {
        addressArea.setText(address);
    }

    /**
     * Adds listener for the refresh button.
     * @param listener The listener class used to control the refresh button's action.
     */
    public void addRefreshListener(ActionListener listener){
        setRefreshRateButton.addActionListener(listener);
    }
}
