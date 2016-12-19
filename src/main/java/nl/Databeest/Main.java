package nl.Databeest;

import nl.Databeest.Navigation.NavigationPanel;

import javax.swing.*;
import java.awt.*;

public class Main {

    /**
     * SubMenuItems the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //SubMenuItems and set up the window.
        JFrame frame = new JFrame("Hotel reservation system - test application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new NavigationPanel(), BorderLayout.CENTER);


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}