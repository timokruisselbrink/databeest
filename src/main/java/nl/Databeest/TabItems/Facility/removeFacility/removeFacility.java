package nl.Databeest.TabItems.Facility.removeFacility;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by A on 13/01/2017.
 */
public class removeFacility extends SubMenuItem{
    private JTable tblFacilityIndex;
    private JPanel mainPanel;
    private JButton btnRefreshFacility;

    public removeFacility() {
        setTable();

        btnRefreshFacility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facilityRefresh();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Index";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    private void setTable(){
        getFacilities();

        if(RoleHelper.isDeleter()) {
            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            tblFacilityIndex.getColumn("Delete").setCellRenderer(buttonRenderer);
            tblFacilityIndex.addMouseListener(new JTableButtonMouseListener(tblFacilityIndex));
        }
    }

    private void facilityRefresh() {setTable();}

    private void getFacilities() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT [NAME] AS 'Facility Name', START_TIME AS 'Start Time' FROM FACILITY WHERE IS_DELETED <> 1 ");
            stmt.setEscapeProcessing(true);

            tblFacilityIndex.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {
                    deleteFacilities((String)row[0], (String) row[1]);
                }
            });

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }



    private void deleteFacilities(String facilityName, String facilityStartDate) {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("EXEC SP_DELETE_FACILITY ?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, facilityName);
            stmt.setString(2, facilityStartDate);
            stmt.setInt(3, UserRoles.getInstance().getUserId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The facility has been successfully removed.", "Success!", 1);

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        setTable();
    }


}
