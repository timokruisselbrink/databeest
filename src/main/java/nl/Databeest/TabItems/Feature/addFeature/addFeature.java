package nl.Databeest.TabItems.Feature.addFeature;

import com.sun.org.apache.bcel.internal.generic.NEW;
import nl.Databeest.Helpers.UserRoles;
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

    public String getFeatureTypeStartTime() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT START_TIME FROM FEATURE_TYPE WHERE NAME = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, cmbAddFeature.getSelectedItem().toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                return rs.getString(1);
            }
            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return "1";
    }

    public void addFeature() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SP_ADD_FEATURE ?,?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, cmbAddFeature.getSelectedItem().toString());
            stmt.setInt(2, highestFeatureSeqNoPlusOne());
            stmt.setString(3, getFeatureTypeStartTime());
            stmt.setString(4, "Midden-Nederland");
            stmt.setInt(5, UserRoles.getInstance().getUserId());

            stmt.execute();
            closeConn(con, stmt);
            JOptionPane.showMessageDialog(null, "The feature has been added successfully.", "Success!", 1);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public int highestFeatureSeqNoPlusOne() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT MAX(SEQ_NO)+1 FROM FEATURE WHERE FEATURE_TYPE_NAME = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, cmbAddFeature.getSelectedItem().toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                return rs.getInt(1);
            }
            closeConn(con, stmt);


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
            stmt = con.prepareStatement("SELECT NAME FROM FEATURE_TYPE WHERE END_TIME IS NULL");
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

