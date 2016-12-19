package nl.Databeest.TabItems.Room.Create;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by timok on 15-12-16.
 */
public class CreateRoom extends SubMenuItem {
    private JPanel mainPanel;
    private JCheckBox combineCheckBox;
    private JTextField roomNumberTextField;
    private JTextField maxPersonsTextField;
    private JTextField customPriceTextField;
    private JTextField surfaceTextField;
    private JComboBox roomTypeComboBox;
    private JTextField floorTextField;
    private JTable specificationsTable;



    @Override
    protected String getMenuItemName() {
        return "Create";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public CreateRoom(){
        final ArrayList<String> selectedSpecifications = new ArrayList<String>();

        specificationsTable.setModel(new selectSpecificationAbstractTableModel(getSpecifications()) {
            @Override
            public void addSelectedSpecification(String name) {
                //write to specification_of_room

                selectedSpecifications.add(name);

            }
        });

        //roomTypeComboBox
    }

    public ResultSet getSpecifications() {
        Connection con = getConnection();
        ResultSet rs = null;


        try {
            PreparedStatement stmt = con.prepareStatement("SELECT NAME FROM SPECIFICATION" );
            stmt.setEscapeProcessing(true);

            rs=stmt.executeQuery();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return rs;
    }


}
