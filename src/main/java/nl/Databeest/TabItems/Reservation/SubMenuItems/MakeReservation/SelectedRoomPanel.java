package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import nl.Databeest.Database.Database;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by timok on 08-12-16.
 */
public class SelectedRoomPanel extends Database{
    private JPanel selectedRoomPanel;
    private JPanel roomHolder;
    private JLabel roomNameLabel;
    private JTable availableFeaturesTable;
    private JTextField perstonsTextField;

    private int roomId;

    public SelectedRoomPanel(int roomNumber, String roomType, Date startDate, Date endDate){
        roomNameLabel.setText(roomNumber + " " + roomType);
        roomId = roomNumber;
        getAvailableFeatures(roomNumber, startDate, endDate);
    }

    public int getRoomId(){
        return roomId;
    }

    public int getAmountOfPersons(){
        return Integer.parseInt(perstonsTextField.getText());
    }

    private void getAvailableFeatures(int roomNumber, Date startDate, Date endDate) {
        Connection con = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("EXEC SP_GET_AVAILABLE_FEATURES ?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, roomNumber);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);

            ResultSet rs = stmt.executeQuery();

            availableFeaturesTable.setModel(new AvailableFeatureTableModel(rs) {

            });

            closeConn(con, stmt);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getSelectedRoomPanel(){
        return selectedRoomPanel;
    }


    public ArrayList<SelectedFeatureModel> getSelectedFeatures(){
       AvailableFeatureTableModel model = (AvailableFeatureTableModel) availableFeaturesTable.getModel();
        return model.getSelectedFeatures();

    }
}
