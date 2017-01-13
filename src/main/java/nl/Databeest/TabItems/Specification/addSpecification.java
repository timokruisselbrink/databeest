package nl.Databeest.TabItems.Specification;

import microsoft.sql.Types;
import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by A on 20/12/2016.
 */
public class addSpecification extends SubMenuItem {
    private JPanel mainPanel;
    private JButton btnAddSpecification;
    private JTextField txtAddSpecificationName;
    private JTextField txtAddSpecificationPrice;


    @Override
    protected String getMenuItemName() {
        return "Add Specification";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public addSpecification() {

        btnAddSpecification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSpecification();

            }
        });

    }
    public void addSpecification() {
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try{
            stmt = con.prepareStatement("SP_CREATE_SPECIFICATION ?,?,?");
            stmt.setEscapeProcessing(true);


            stmt.setString(1, txtAddSpecificationName.getText());
            stmt.setFloat(2, Float.parseFloat(txtAddSpecificationPrice.getText()));
            stmt.setInt(3, UserRoles.getInstance().getUserId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The specification has been added successfully.", "Success!", 1);
            closeConn(con, stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }


}
