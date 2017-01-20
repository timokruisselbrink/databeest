package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.Helpers.User;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

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
    private JButton makeReservationButton;
    private JButton clearScreenButton;
    private JPanel accountPanel;
    private JTabbedPane accountTabbedPane;

    private Login loginPanel;
    private  Register registerPanel;
    private WithoutAccount withoutAccountPanel;

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
        createCreateAccount();

        JSpinner.NumberEditor editor1 = new JSpinner.NumberEditor(endYearSpinner, "#");
        endYearSpinner.setEditor(editor1);

        JSpinner.NumberEditor editor2 = new JSpinner.NumberEditor(startYearSpinner, "#");
        startYearSpinner.setEditor(editor2);


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

    public void createCreateAccount(){
     //  accountTabbedPane = new JTabbedPane();
        accountTabbedPane.removeAll();
        loginPanel = new Login();
        registerPanel = new Register();
        withoutAccountPanel = new WithoutAccount();

        if(RoleHelper.isGuest()) {
            accountTabbedPane.addTab(loginPanel.getMenuItemName(), loginPanel.getMainPanel());
            accountTabbedPane.addTab(registerPanel.getMenuItemName(), registerPanel.getMainPanel());
        }
        accountTabbedPane.addTab(withoutAccountPanel.getMenuItemName(),withoutAccountPanel.getMainPanel());

        accountTabbedPane.revalidate();
        accountTabbedPane.repaint();
    }

    public void clearScreen(){
        startDaySpinner.setValue(10);
        startMonthComboBox.setSelectedIndex(3);
        startYearSpinner.setValue(2017);
        endDaySpinner.setValue(12);
        endMonthComboBox.setSelectedIndex(3);
        endYearSpinner.setValue(2017);

        availableRoomsTable.setModel(new DefaultTableModel());

        createCreateAccount();

        chosenRoomHolder.removeAll();
        chosenRoomHolder.revalidate();
        chosenRoomHolder.repaint();
    }


    public void setTestData(){
        startDaySpinner.setValue(10);
        startMonthComboBox.setSelectedIndex(3);
        startYearSpinner.setValue(2017);


        endDaySpinner.setValue(12);
        endMonthComboBox.setSelectedIndex(3);
        endYearSpinner.setValue(2017);
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

            int guestId = -1;

            if(RoleHelper.isGuest()){
                switch (accountTabbedPane.getSelectedIndex())
                {
                    case 0:
                        guestId = loginPanel.getGuestId(con);
                        break;
                    case 1:
                        guestId = registerPanel.getGuestId(con);
                        break;
                    case 2:
                        guestId = withoutAccountPanel.getGuestId(con);
                        break;
                }
            }
            else {
                guestId = withoutAccountPanel.getGuestId(con);
            }

            int reservationNumber = createReservation(con, guestId);

            createReservationsForRooms(con, reservationNumber);

            con.commit();
        con.close();
            JOptionPane.showMessageDialog(null, "The reservation has been made successfully.", "Success!", 1);
            clearScreen();
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
        PreparedStatement stmt = con.prepareStatement("EXEC SP_INSERT_RESERVATION_ROW ?,?,?,?,?,?,?");
        stmt.setEscapeProcessing(true);

        if(selectedRooms.size() == 0){
            throw new SQLException("Select at least one room.");
        }

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

            if(RoleHelper.isGuest()){
                stmt.setNull(7, Types.INTEGER);
            }else {
                stmt.setInt(7, User.getInstance().getUserId());
            }

            stmt.execute();

            createFeatureReservationsForRoom(con, selectedRoom, reservationNumber, seqNo);

            seqNo++;
        }

        stmt.close();
    }

    private void createFeatureReservationsForRoom(Connection con, SelectedRoomPanel room, int reservationNumber, int reservationRowSequenceNumber) throws SQLException {
        room.getSelectedFeatures();


        PreparedStatement stmt = con.prepareStatement("EXEC\t[dbo].[SP_INSERT_FEATURE_FOR_RESERVATION_ROW]\n" +
                "\t\t@Feature_Type_Name = ?,\n" +
                "\t\t@Feature_Amount = ?,\n" +
                "\t\t@Reservation_No = ?,\n" +
                "\t\t@Reservation_Row_Seq_No = ?,\n" +
                "\t\t@Start_Time = ?,\n" +
                "\t\t@End_Time = ?,\n" +
                "\t\t@Last_Edit_By = ?");
        stmt.setEscapeProcessing(true);

        stmt.setInt(3, reservationNumber);
        stmt.setInt(4, reservationRowSequenceNumber);
        stmt.setDate(5, startDate);
        stmt.setDate(6, endDate);
        if(RoleHelper.isGuest()){
            stmt.setNull(7, Types.INTEGER);
        }else {
            stmt.setInt(7, User.getInstance().getUserId());
        }

        for (SelectedFeatureModel model: room.getSelectedFeatures()) {
            stmt.setString(1, model.getFeatureTypeName());
            stmt.setInt(2, model.getAmount());


            System.out.println(stmt);
            stmt.execute();
        }

        stmt.close();
    }

    private int createReservation(Connection con, int guestId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("EXEC SP_INSERT_RESERVATION ?,?,?,?");
        stmt.setEscapeProcessing(true);

        stmt.setString(1, "Direct");
        stmt.setInt(2, guestId);

        if(RoleHelper.isGuest()){
            stmt.setNull(3, Types.INTEGER);
        }else {
            stmt.setInt(3, User.getInstance().getUserId());
        }

        if(RoleHelper.isGuest() && accountTabbedPane.getSelectedIndex() == 0){
            stmt.setInt(4, loginPanel.getUseLoyaltyPoints());
        }
        else {
            stmt.setInt(4, 0);
        }


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
