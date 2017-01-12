package nl.Databeest.TabItems.PartnerRoom.addRoomOfPartner;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by A on 12/01/2017.
 */
public class addRoomOfPartner extends SubMenuItem {
    private JPanel mainPanel;
    private JComboBox cmbRoomIdForPartnerRoom;
    private JComboBox cmbPartnerNameForRoom;
    private JPanel startDatePanel;
    private JSpinner startDaySpinner;
    private JComboBox startMonthComboBox;
    private JSpinner startYearSpinner;
    private JPanel endDatePanel;
    private JSpinner endDaySpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endYearSpinner;
    private JButton btnAddRoomOfPartnerButton;
    private JTextField txtRoomOfPartnerPrice;
    private final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected String getMenuItemName() {
        return "Create";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public addRoomOfPartner() {
        getRoomId();
        getPartnerName();

        startMonthComboBox.addItem("");
        endMonthComboBox.addItem("");
        for (int i = 0; i < MONTHS.length; i++) {
            startMonthComboBox.addItem(MONTHS[i]);
            endMonthComboBox.addItem(MONTHS[i]);}

        btnAddRoomOfPartnerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRoomOfPartner();
            }
        });
    }

    private void getRoomId() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT ROOM_ID FROM ROOM" );
            stmt.setEscapeProcessing(true);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cmbRoomIdForPartnerRoom.addItem(rs.getString(1));
            }
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void getPartnerName() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("SELECT PARTNER_NAME FROM PARTNER");
            stmt.setEscapeProcessing(true);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cmbPartnerNameForRoom.addItem(rs.getString(1));
            }
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void addRoomOfPartner() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        int roomID = Integer.parseInt((String) cmbRoomIdForPartnerRoom.getSelectedItem());
        String partnerName = (String) cmbPartnerNameForRoom.getSelectedItem();

        Date startDate = DateHelper.createSqlDate(
                startDaySpinner.getValue().toString(),
                startMonthComboBox.getSelectedIndex(),
                startYearSpinner.getValue().toString()
        );

        Date endDate = DateHelper.createSqlDate(
                endDaySpinner.getValue().toString(),
                endMonthComboBox.getSelectedIndex(),
                endYearSpinner.getValue().toString()
        );


        try{
            stmt = con.prepareStatement("EXEC SP_ADD_ROOM_OF_PARTNER ?,?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, roomID);
            stmt.setString(2, partnerName);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            stmt.setFloat(5, Float.valueOf(txtRoomOfPartnerPrice.getText()));

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The room for the partner has been added successfully.", "Success!", 1);
            closeConn(con, stmt);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }
}
