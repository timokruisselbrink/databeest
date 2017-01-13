package nl.Databeest.TabItems.Facility.addFacility;

import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by A on 13/01/2017.
 */
public class addFacility extends SubMenuItem{
    private JTextField txtAddFacilityName;
    private JTextField txtAddFacilityPrice;
    private JTextField txtAddFacilityMaxPersons;
    private JPanel mainPanel;
    private JButton addFacilityButton;

    public addFacility() {
        addFacilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFacility();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Add Facility";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public void addFacility() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            if(txtAddFacilityName.getText()==null||txtAddFacilityName.getText().isEmpty()){
                throw new SQLException("Facility name can not be null");
            }

            if(txtAddFacilityPrice.getText()==null||txtAddFacilityPrice.getText().isEmpty()){
                throw new SQLException("Facility price can not be null");
            }

            if(txtAddFacilityMaxPersons.getText()==null||txtAddFacilityMaxPersons.getText().isEmpty()){
                throw new SQLException("Facility max persons can not be null");
            }

            stmt = con.prepareStatement("SP_INSERT_FACILITY ?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, txtAddFacilityName.getText());
            stmt.setFloat(2, Float.valueOf(txtAddFacilityPrice.getText()));
            stmt.setString(3, txtAddFacilityMaxPersons.getText());
            stmt.setInt(4, UserRoles.getInstance().getUserId());

            stmt.execute();

            JOptionPane.showMessageDialog(null, "The facility has been added successfully.", "Success!", 1);
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }



    }

}
