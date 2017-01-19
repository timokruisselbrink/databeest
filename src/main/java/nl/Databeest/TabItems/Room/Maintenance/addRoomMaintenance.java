package nl.Databeest.TabItems.Room.Maintenance;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.User;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by A on 20/12/2016.
 */
public class addRoomMaintenance extends SubMenuItem{
    private JPanel mainPanel;
    private JButton btnAddRoomMaintenance;
    private JPanel startDatePanel;
    private JPanel endDatePanel;
    private JSpinner startDaySpinner;
    private JComboBox startMonthComboBox;
    private JSpinner startYearSpinner;
    private JSpinner endDaySpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endYearSpinner;
    private JTextField txtAddRoomMaintenanceReason;
    private JComboBox cmbAddRoomMaintenanceID;
    private JButton btnRefreshRoomMaintenance;

    private final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected String getMenuItemName() {
        return "Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public addRoomMaintenance() {
        getRoomID();
        startMonthComboBox.addItem("");
        endMonthComboBox.addItem("");
        for (int i = 0; i < MONTHS.length; i++) {
            startMonthComboBox.addItem(MONTHS[i]);
            endMonthComboBox.addItem(MONTHS[i]);}

        btnAddRoomMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoomMaintenance();

            }
        });

        btnRefreshRoomMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshRoomMaintenance();
            }
        });
    }

    private void refreshRoomMaintenance() {
        cmbAddRoomMaintenanceID.removeAllItems();
        getRoomID();
    }


    public void addRoomMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;


        int roomID = (Integer) cmbAddRoomMaintenanceID.getSelectedItem();

        Date startDate = DateHelper.createSqlDate(
                startDaySpinner.getValue().toString(),
                startMonthComboBox.getSelectedIndex(),
                startYearSpinner.getValue().toString()
        );

        Date endDate = null;
        if (endMonthComboBox.getSelectedIndex() != 0) {
            endDate = DateHelper.createSqlDate(
                    endDaySpinner.getValue().toString(),
                    endMonthComboBox.getSelectedIndex(),
                    endYearSpinner.getValue().toString()
            );
        }



        try{
            stmt = con.prepareStatement("SP_ADD_ROOM_MAINTENANCE ?,?,?,?,?");
            stmt.setEscapeProcessing(true);


            stmt.setInt(1, roomID);
            stmt.setDate(2, startDate);
            if(endDate == null){
                stmt.setNull(3, Types.DATE);
            }
            else {
                stmt.setDate(3, endDate);
            }
            stmt.setString(4, txtAddRoomMaintenanceReason.getText());
            stmt.setInt(5, User.getInstance().getUserId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The room maintenance has been added successfully.", "Success!", 1);

            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }

    public void getRoomID() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT Room_ID FROM ROOM WHERE END_TIME < GETDATE()");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();


            while(rs.next()) {
                int roomID = rs.getInt(1);

                cmbAddRoomMaintenanceID.addItem(roomID);
            }
            closeConn(con, stmt);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }



    }


}
