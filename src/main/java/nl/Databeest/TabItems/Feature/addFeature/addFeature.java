package nl.Databeest.TabItems.Feature.addFeature;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by A on 19/12/2016.
 */
public class addFeature extends SubMenuItem{
    private JPanel mainPanel;
    private JButton txtAddFeatureName;
    private JTextField txtFeatureAddName;
    private JButton btnAddFeature;
    private JComboBox cmbAddFeature;
    private JTextField txtFeatureTypePrice;

    public addFeature() {

        getFeatureTypeName();

        btnAddFeature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFeature();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Add Feature";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }


    public void addFeature() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SP_ADD_FEATURE ?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, cmbAddFeature.getSelectedItem().toString());
            stmt.setInt(2, highestFeatureSeqNoPlusOne());
            stmt.setBoolean(4, false);
            stmt.setString(3, "Midden-Nederland");

            stmt.execute();



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public int highestFeatureSeqNoPlusOne() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT MAX(FEATURE_SEQ_NO)+1 FROM FEATURE WHERE FEATURE_TYPE_NAME = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, cmbAddFeature.getSelectedItem().toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                return rs.getInt(1);
            }



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return 1;
    }

    public void getFeatureTypeName() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT NAME FROM FEATURE_TYPE WHERE IS_DELETED <> 1");
            stmt.setEscapeProcessing(true);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                cmbAddFeature.addItem(rs.getString(1));
            }
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

}

