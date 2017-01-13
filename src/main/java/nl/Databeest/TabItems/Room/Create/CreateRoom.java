package nl.Databeest.TabItems.Room.Create;

import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by timok on 15-12-16.
 */
public class CreateRoom extends SubMenuItem {
    private JPanel mainPanel;
    private JTextField roomNumberTextField;
    private JTextField maxPersonsTextField;
    private JTextField customPriceTextField;
    private JTextField surfaceTextField;
    private JComboBox roomTypeComboBox;
    private JTable specificationsTable;
    private JButton btnAddRoom;
    private JComboBox floorComboBox;
    private JTable optionalFeaturesTable;

    ArrayList<String> selectedSpecifications = new ArrayList<String>();



    selectFeatureAbstractTableModel featureTableModel;


    @Override
    protected String getMenuItemName() {
        return "Create";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public CreateRoom() {
        getRoomTypes();
        getFloors();
        Connection con = getConnection();

        try {
            featureTableModel = new selectFeatureAbstractTableModel(getFeatures(con));

            specificationsTable.setModel(new selectSpecificationAbstractTableModel(getSpecifications(con)) {
                @Override
                public void addSelectedSpecification(String name) {
                     selectedSpecifications.add(name);
                }

                @Override
                public void removeSelectedSpecification(String name) {
                    selectedSpecifications.remove(name);
                }
            });

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        optionalFeaturesTable.setModel(featureTableModel);

        btnAddRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = getConnection();

                try {
                    con.setAutoCommit(false);

                    int roomId = insertRoom(con);
                    insertSpecification(con, roomId);
                    insertFeatures(con, roomId);

                    con.commit();
                    con.close();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());


                    if (con != null) {
                        try {
                            con.rollback();
                        } catch(SQLException excep) {
                            JOptionPane.showMessageDialog(null, excep.getMessage());
                        }
                    }
                }
            }
        });
    }

    public int insertRoom(Connection con) throws SQLException {

        PreparedStatement stmt = null;

        int roomID = -1;


            stmt = con.prepareStatement("SP_ADD_ROOM ?,?,?,?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, roomTypeComboBox.getSelectedItem().toString());
            stmt.setInt(2, Integer.parseInt((String) floorComboBox.getSelectedItem()));
            stmt.setInt(3, Integer.parseInt(roomNumberTextField.getText()));
            stmt.setInt(4, Integer.parseInt(maxPersonsTextField.getText()));
            stmt.setDouble(5, 0);
            stmt.setInt(6, Integer.parseInt(surfaceTextField.getText()));
            stmt.setInt(7, UserRoles.getInstance().getUserId());



            if((customPriceTextField.getText() == null)||(customPriceTextField.getText().isEmpty())){

                stmt.setNull(5, Types.VARCHAR);

            } else {

                stmt.setFloat(5, Float.valueOf(customPriceTextField.getText()));

            }
            stmt.setInt(6, Integer.parseInt(surfaceTextField.getText()));

            ResultSet rs = stmt.executeQuery();
            JOptionPane.showMessageDialog(null, "The room has been added successfully.", "Success!", 1);

            while(rs.next())
            {
                roomID = rs.getInt("ROOM_ID");
            }


        return roomID;
    }

    private void insertSpecification(Connection con, int roomId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("SP_ADD_SPECIFICATION_FOR_ROOM ?,?,?");
        stmt.setEscapeProcessing(true);
        stmt.setInt(1, roomId);

        for(int i = 0; i<selectedSpecifications.size(); i++) {
                stmt.setString(2, selectedSpecifications.get(i).toString());
                stmt.execute();
        }
        stmt.setInt(3, UserRoles.getInstance().getUserId());
        stmt.close();

    }


    private void insertFeatures(Connection con, int roomId) throws SQLException {

        ArrayList<SelectedFeatureModel> selectedFeatures = featureTableModel.getSelectedFeatures();
        PreparedStatement stmt = null;

            stmt = con.prepareStatement("SP_ADD_FEATURE_TO_ROOM ?,?,?,?");
            stmt.setEscapeProcessing(true);
            stmt.setInt(2, roomId);

            for (SelectedFeatureModel selectedFeature: selectedFeatures) {
                stmt.setString(1, selectedFeature.getName());
                stmt.setInt(3, selectedFeature.getAmount());

                stmt.execute();
            }
             stmt.setInt(4, UserRoles.getInstance().getUserId());
            stmt.close();
    }

    public ResultSet getSpecifications(Connection con) throws SQLException {
        ResultSet rs = null;




            PreparedStatement stmt = con.prepareStatement("SELECT NAME FROM SPECIFICATION" );
            stmt.setEscapeProcessing(true);

            rs = stmt.executeQuery();

        return rs;
    }

    public ResultSet getFeatures(Connection con) throws SQLException {
        ResultSet rs = null;

            PreparedStatement stmt = con.prepareStatement("SELECT NAME FROM FEATURE_TYPE" );
            stmt.setEscapeProcessing(true);

            rs=stmt.executeQuery();

        return rs;
    }


    public void getRoomTypes() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT NAME FROM ROOM_TYPE");
            stmt.setEscapeProcessing(true);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roomTypeComboBox.addItem(rs.getString(1));
            }
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void getFloors() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT DISTINCT FLOOR_NO FROM ROOM");
            stmt.setEscapeProcessing(true);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                floorComboBox.addItem(rs.getString(1));
            }
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
