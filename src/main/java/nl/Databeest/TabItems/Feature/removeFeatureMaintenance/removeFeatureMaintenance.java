package nl.Databeest.TabItems.Feature.removeFeatureMaintenance;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by A on 20/12/2016.
 */
public class removeFeatureMaintenance extends SubMenuItem{
    private JPanel mainPanel;
    private JButton btnRemoveFeatureMaintenance;

    public removeFeatureMaintenance() {
        btnRemoveFeatureMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Remove Feature Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public void removeFeatureMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;



    }


}
