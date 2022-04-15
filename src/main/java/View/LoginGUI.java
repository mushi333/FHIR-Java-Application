package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *  Creates the login page to find patients of a particular practitioner
 *  with simple verification to check the existence of the ID.
 *
 *  @author Kenneth Huynh
 */
public class LoginGUI extends GUI {

    // View.GUI Login page
    private JPanel loginPanel;
    private JButton loginButton;
    private JTextField practitionerIDTextField;
    private Label loginFeedback;

    /**
     *  Updates the frame with the login page.
     *
     *  @param frame Used to place panels onto.
     */
    public LoginGUI(JFrame frame) {
        // Initial Frame
        super(frame);

        // Create login page
        createLoginPanel();
    }

    /**
     * Changes the login feedback if processing request or not.
     * @param verifyingID True if request is pending, false if ID not found
     */
    public void changeLoginFeedback(boolean verifyingID) {
        if (verifyingID) {
            loginFeedback.setText("Processing...");
        } else {
            loginFeedback.setText("ID not found");
        }
    }

    /**
     * Getter for the text within the Practitioner ID Text Field.
     * @return A Practitioner ID string
     */
    public String getPractitionerIDText() {
        return practitionerIDTextField.getText();
    }

    /**
     * Removes the login panel from the current frame.
     */
    public void removeLoginPanelFromFrame() {
        frame.remove(loginPanel);
    }

    /**
     * Adds listener for the login button.
     * @param listener The listener class used to control the the login button's action.
     */
    public void addLoginListener(ActionListener listener){
        loginButton.addActionListener(listener);
    }

    /**
     *  Sets up the login frame by creating all the needed panels and its elements.
     */
    private void createLoginPanel() {
        // Search Bar
        practitionerIDTextField = new JTextField(20);

        // Button
        loginButton = new JButton("Login");

        // Label
        loginFeedback = new Label("                    "); // White space added so that it can be replaced
        JLabel title = new JLabel("PRACTITIONER LOGIN");
        title.setFont(title.getFont().deriveFont(20f));         // change to bigger font

        // Setup title
        JPanel titlePanel = new JPanel();
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);

        // Setup practitioner ID field and loginButton
        JPanel practitionerLoginFieldPanel = new JPanel();
        practitionerLoginFieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        practitionerLoginFieldPanel.add(practitionerIDTextField);
        practitionerLoginFieldPanel.add(loginButton);

        // Setup authentication message
        JPanel msgPanel = new JPanel();
        msgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgPanel.add(loginFeedback);

        // Setup whole login panel
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(titlePanel);
        loginPanel.add(practitionerLoginFieldPanel);
        loginPanel.add(msgPanel);

        // Setup application frame
        frame.add(loginPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
