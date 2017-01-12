package nl.Databeest;

import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.Login.LoginDialog;
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

        //Login
        LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);
        // if logon successfully
        if(loginDlg.isSucceeded()){
            //Add content to the window.

            UserRoles.getInstance().setRoles(loginDlg.getRoles());
            UserRoles.getInstance().setUserId(loginDlg.getUserId());

            frame.add(new NavigationPanel(), BorderLayout.CENTER);

            //Display the window.
            frame.pack();
            frame.setVisible(true);
        }
        else {
            frame.setVisible(false); //you can't see me!
            frame.dispose(); //Destroy the JFrame object
        }
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