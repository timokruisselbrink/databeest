package nl.Databeest.TabItems.Feature.createFeatureType;

import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by A on 19/12/2016.
 */
public class createFeatureType extends SubMenuItem{
    private JPanel mainPanel;
    private JButton btnAddFeatureType;
    private JTextField txtFeatureTypeName;
    private JTextField txtFeatureTypePrice;


    public createFeatureType() {
        btnAddFeatureType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            saveFeatureType();

            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Create Type";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public void saveFeatureType() {
        Connection con = getConnection();
        PreparedStatement stmt = null;
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        try{
            stmt = con.prepareStatement("SP_CREATE_FEATURE_TYPE ?,?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, txtFeatureTypeName.getText());
            stmt.setFloat(2, Float.valueOf(txtFeatureTypePrice.getText()));
            stmt.setDate(3, date);
            stmt.setDate(4, null);
            stmt.setBoolean(5,false);

            stmt.execute();



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

}
