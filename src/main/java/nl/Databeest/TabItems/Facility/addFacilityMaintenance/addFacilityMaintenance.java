package nl.Databeest.TabItems.Facility.addFacilityMaintenance;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.User;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by A on 13/01/2017.
 */
public class addFacilityMaintenance extends SubMenuItem{
    private JComboBox cmbAddFacilityMaintenance;
    private JPanel startDatePanel;
    private JSpinner startDaySpinner;
    private JComboBox startMonthComboBox;
    private JSpinner startYearSpinner;
    private JPanel endDatePanel;
    private JSpinner endDaySpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endYearSpinner;
    private JTextField txtAddFacilityMaintenanceReason;
    private JButton btnAddFacilityMaintenance;
    private JPanel mainPanel;
    private JButton btnRefreshFacilityMaintenance;

    private final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};




    public addFacilityMaintenance() {
        getFacilityName();
        startMonthComboBox.addItem("");
        endMonthComboBox.addItem("");
        for (int i = 0; i < MONTHS.length; i++) {
            startMonthComboBox.addItem(MONTHS[i]);
            endMonthComboBox.addItem(MONTHS[i]);}
        btnAddFacilityMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {addFacilityMaintenance();
            }

        });
        btnRefreshFacilityMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMaintenance();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Add Facility Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public void refreshMaintenance() {
        cmbAddFacilityMaintenance.removeAllItems();
        getFacilityName();

    }



    public void addFacilityMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        Date startDate = DateHelper.createSqlDate(
                startDaySpinner.getValue().toString(),
                startMonthComboBox.getSelectedIndex(),
                startYearSpinner.getValue().toString()
        );

        Date endDate = null;
        if(endMonthComboBox.getSelectedIndex() != 0){
            endDate = DateHelper.createSqlDate(
                    endDaySpinner.getValue().toString(),
                    endMonthComboBox.getSelectedIndex(),
                    endYearSpinner.getValue().toString()
            );
        }

        try{
            stmt = con.prepareStatement("SP_INSERT_FACILITY_MAINTENANCE ?,?,?,?,?,?");
            stmt.setEscapeProcessing(true);


            String string = cmbAddFacilityMaintenance.getSelectedItem().toString();;
            String[] parts = string.split("\\|");
            String part1 = parts[0];
            String part2 = parts[1];


            stmt.setString(1, part1);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            stmt.setString(4, txtAddFacilityMaintenanceReason.getText());
            stmt.setInt(5, User.getInstance().getUserId());
            stmt.setString(6, part2);

            stmt.execute();

            JOptionPane.showMessageDialog(null, "The facility maintenance has been added successfully.", "Success!", 1);
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }



    public void getFacilityName() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT NAME, START_TIME FROM FACILITY WHERE IS_DELETED <> 1");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();



            while(rs.next()) {
                String facilityName = rs.getString(1);
                String start_time = rs.getString(2);

                cmbAddFacilityMaintenance.addItem(facilityName + "|" + start_time);
            }
            closeConn(con, stmt);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }



    }
}
















