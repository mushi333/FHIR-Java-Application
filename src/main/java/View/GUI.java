package View;

import javax.swing.*;

/**
 *  Sets up the basic View.GUI and FHIR API properties for use.
 *
 *  @author Kenneth Huynh
 */
public abstract class GUI {
    // View.GUI Parts
    JFrame frame;

    /**
     *  Sets up the default frame properties.
     *
     *  @param frame The frame to be used and set up
     */
    public GUI(JFrame frame) {
        // Initial Frame
        this.frame = frame;
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Health Monitor");
        this.frame.setResizable(false);
    }
}
