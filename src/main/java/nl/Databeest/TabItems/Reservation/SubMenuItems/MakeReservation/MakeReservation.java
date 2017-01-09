package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by timok on 30-11-16.
 */
public class MakeReservation  extends SubMenuItem {
    private JPanel mainPanel;
    private JPanel datePeriodPanel;
    private JPanel startDatePanel;
    private JSpinner startDaySpinner;
    private JComboBox startMonthComboBox;
    private JSpinner startYearSpinner;
    private JSpinner endDaySpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endYearSpinner;
    private JPanel endDatePanel;
    private JButton searchFreeRoomsButton;
    private JPanel availableRoomsPanel;
    private JTable availableRoomsTable;
    private JPanel chosenRoomHolder;
    private JPanel guestInfoPanel;
    private JTextField firstNameTextField;
    private JTextField countryTextField;
    private JButton makeReservationButton;
    private JTextField prefiexTextField;
    private JTextField lastNameTextField;
    private JTextField phoneNumberTextField;
    private JTextField emalAddressTextField;
    private JComboBox genderComboBox;
    private JTextField birthdayTextField;
    private JTextField addressTextField;
    private JTextField address2TextField;
    private JTextField creditCardNumberTextField;
    private JTextField debitCardNumberTextField;
    private JButton clearScreenButton;

    private Date startDate, endDate;

    private ArrayList<SelectedRoomPanel> selectedRooms = new ArrayList<SelectedRoomPanel>();

    private final String[] MONTHS = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected String getMenuItemName() {
        return "Make Reservation";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public MakeReservation(){
        createComboBoxItems();
        setActionListeners();
        setTestData();

        BoxLayout boxlayout = new BoxLayout(chosenRoomHolder, BoxLayout.Y_AXIS);
        chosenRoomHolder.setLayout(boxlayout);
        makeReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createReservation();
            }
        });
        clearScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearScreen();
            }
        });
    }

    public void clearScreen(){
        startDaySpinner.setValue(1);
        startMonthComboBox.setSelectedIndex(1);
        startYearSpinner.setValue(2000);
        endDaySpinner.setValue(1);
        endMonthComboBox.setSelectedIndex(1);
        endYearSpinner.setValue(2000);

        availableRoomsTable.setModel(new DefaultTableModel());

        firstNameTextField.setText("");
        countryTextField.setText("");
        prefiexTextField.setText("");
        lastNameTextField.setText("");
        phoneNumberTextField.setText("");
        emalAddressTextField.setText("");
        birthdayTextField.setText("");
        addressTextField.setText("");
        address2TextField.setText("");
        creditCardNumberTextField.setText("");
        debitCardNumberTextField.setText("");

        chosenRoomHolder.removeAll();
        chosenRoomHolder.revalidate();
        chosenRoomHolder.repaint();
    }


    public void setTestData(){
        startDaySpinner.setValue(10);
        startMonthComboBox.setSelectedIndex(3);
        startYearSpinner.setValue(2016);


        endDaySpinner.setValue(12);
        endMonthComboBox.setSelectedIndex(3);
        endYearSpinner.setValue(2016);
    };

    public void setActionListeners(){
        searchFreeRoomsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getFreeRooms();
            }
        });
    }

    public void getFreeRooms(){
        Connection con = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("EXEC SP_GET_AVAILABLE_ROOMS ?,?");
            stmt.setEscapeProcessing(true);

            startDate = DateHelper.createSqlDate(
                    startDaySpinner.getValue().toString(),
                    startMonthComboBox.getSelectedIndex(),
                    startYearSpinner.getValue().toString()
            );

            endDate = DateHelper.createSqlDate(
                    endDaySpinner.getValue().toString(),
                    endMonthComboBox.getSelectedIndex(),
                    endYearSpinner.getValue().toString()
            );

            stmt.setDate(1, startDate);

            stmt.setDate(2, endDate);

            ResultSet rs = stmt.executeQuery();

            availableRoomsTable.setModel(new AvailableRoomTableModel(rs) {
                @Override
                public void addSelectedRoom(int roomNr, String roomType) {
                    selectRoom(roomNr, roomType);
                }
            });

            closeConn(con, stmt);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void createComboBoxItems(){
        try {
            startMonthComboBox.addItem("");
            endMonthComboBox.addItem("");
            for (int i = 0; i < MONTHS.length; i++) {
                startMonthComboBox.addItem(MONTHS[i]);
                endMonthComboBox.addItem(MONTHS[i]);
            }



            genderComboBox.addItem("M");
            genderComboBox.addItem("F");
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void selectRoom(int roomNr, String roomType){
        SelectedRoomPanel panel = new SelectedRoomPanel(roomNr, roomType, startDate, endDate);
        chosenRoomHolder.add(panel.getSelectedRoomPanel());
        chosenRoomHolder.revalidate();

        selectedRooms.add(panel);
    }

    private void createReservation() {
        Connection con = getConnection();

        try {
            con.setAutoCommit(false);

            int guestId = createGuest(con);

            int reservationNumber = createReservation(con, guestId);

            createReservationsForRooms(con, reservationNumber);

            con.commit();
        con.close();
            JOptionPane.showMessageDialog(null, "The reservation has been made successfully.", "Success!", 1);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage());

        if (con != null) {
            try {
                con.rollback();
            } catch(SQLException excep) {
                JOptionPane.showMessageDialog(null, excep.getMessage());
            }
        }
    }
    }

    private void createReservationsForRooms(Connection con, int reservationNumber) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("EXEC SP_INSERT_RESERVATION_ROW ?,?,?,?,?,?");
        stmt.setEscapeProcessing(true);

        int seqNo = 1;
        for (SelectedRoomPanel selectedRoom: selectedRooms) {
            stmt.setInt(1, seqNo);
            stmt.setInt(2, reservationNumber);
            stmt.setInt(3, selectedRoom.getRoomId());
            stmt.setDate(4, startDate);
            stmt.setDate(5, endDate);
            /*stmt.setDouble(6, 200.00); //TODO: calculate
            stmt.setDouble(7, 200.00); //TODO: calculate
            stmt.setDouble(8, 200.00); //TODO: calculate*/
            stmt.setInt(6, selectedRoom.getAmountOfPersons());

            stmt.execute();

            createFeatureReservationsForRoom(con, selectedRoom);

            seqNo++;
        }
    }

    private void createFeatureReservationsForRoom(Connection con, SelectedRoomPanel room) throws SQLException {
        //TODO: alles hiero...
    }

    private int createGuest(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("EXEC SP_SIGN_UP_GUEST ?,?,?,?,?,?,?,?,?,?,?,?");
        stmt.setEscapeProcessing(true);

        if(firstNameTextField.getText() == null || firstNameTextField.getText().isEmpty()){
            stmt.setNull(1, Types.VARCHAR);
        }
        else {
            stmt.setString(1, firstNameTextField.getText());
        }

        if(prefiexTextField.getText() == null || prefiexTextField.getText().isEmpty()){
            stmt.setNull(2, Types.VARCHAR);
        }
        else {
            stmt.setString(2, prefiexTextField.getText());
        }

        if(lastNameTextField.getText() == null || lastNameTextField.getText().isEmpty()){
            stmt.setNull(3, Types.VARCHAR);
        }
        else {
            stmt.setString(3, lastNameTextField.getText());
        }

        if(creditCardNumberTextField.getText() == null || creditCardNumberTextField.getText().isEmpty()){
            stmt.setNull(4, Types.VARCHAR);
        }
        else {
            stmt.setString(4, creditCardNumberTextField.getText());
        }

        if(debitCardNumberTextField.getText() == null || debitCardNumberTextField.getText().isEmpty()){
            stmt.setNull(5, Types.VARCHAR);
        }
        else {
            stmt.setString(5, debitCardNumberTextField.getText());
        }

        if(phoneNumberTextField.getText() == null || phoneNumberTextField.getText().isEmpty()){
            stmt.setNull(6, Types.VARCHAR);
        }
        else {
            stmt.setString(6, phoneNumberTextField.getText());
        }

        if(emalAddressTextField.getText() == null || emalAddressTextField.getText().isEmpty()){
            stmt.setNull(7, Types.VARCHAR);
        }
        else {
            stmt.setString(7, emalAddressTextField.getText());
        }

        stmt.setString(8, (String) genderComboBox.getSelectedItem());

        if(countryTextField.getText() == null || countryTextField.getText().isEmpty()){
            stmt.setNull(9, Types.VARCHAR);
        }
        else {
            stmt.setString(9, countryTextField.getText());
        }

        if(addressTextField.getText() == null || addressTextField.getText().isEmpty()){
            stmt.setNull(10, Types.VARCHAR);
        }
        else {
            stmt.setString(10, addressTextField.getText());
        }

        if(address2TextField.getText() == null || address2TextField.getText().isEmpty()){
            stmt.setNull(11, Types.VARCHAR);
        }
        else {
            stmt.setString(11, address2TextField.getText());
        }

        if(birthdayTextField.getText() == null || birthdayTextField.getText().isEmpty()){
            stmt.setNull(12, Types.DATE);
        }
        else {
            stmt.setString(12, birthdayTextField.getText());
        }

        ResultSet rs = stmt.executeQuery();

        int guestId = -1;

        while(rs.next())
        {
            guestId = rs.getInt("GUEST_ID");
        }

        stmt.close();

        return guestId;
    }

    private int createReservation(Connection con, int guestId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("EXEC SP_INSERT_RESERVATION ?,?");
        stmt.setEscapeProcessing(true);

        stmt.setString(1, "Direct");
        stmt.setInt(2, guestId);


        ResultSet rs = stmt.executeQuery();

        int reservationId = -1;

        while(rs.next())
        {
            reservationId = rs.getInt("RESERVATION_NO");
        }

        stmt.close();

        return reservationId;

    }
}
