package nl.Databeest.TabItems.Facility.removeFacilityMaintenance;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.Helpers.User;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by A on 13/01/2017.
 */
public class removeFacilityMaintenance extends SubMenuItem{
    private JPanel mainPanel;
    private JTable tblFacilityInMaintenance;
    private JButton refreshButton;

    public removeFacilityMaintenance() {
        setTable();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Remove Facility Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }



    public void getDataFromFacilityMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT FACILITY_NAME, ESTABLISHMENT_NAME, START_TIME FROM FACILITY_MAINTENANCE WHERE IS_CANCELLED <> 1");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();

            tblFacilityInMaintenance.setModel(new IndexAbstractTableModel(rs) {

                @Override
                public void deleteRow(Object[] row) {

                    deleteFacilityMaintenance((String) row[0], (String) row[1], (String) row[2]);
                }
            });
            closeConn(con, stmt);

            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            tblFacilityInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
            tblFacilityInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFacilityInMaintenance));


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void deleteFacilityMaintenance(String facilityName, String establishmentName, String startTime) {

            Connection con = getConnection();
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement("EXEC SP_DELETE_FACILITY_MAINTENANCE ?,?,?,?");
                stmt.setEscapeProcessing(true);

                stmt.setString(1, facilityName);
                stmt.setString(2, establishmentName);
                stmt.setString(3, startTime);
                stmt.setInt(4, User.getInstance().getUserId());

                stmt.execute();
                JOptionPane.showMessageDialog(null, "The facility maintenance type has been removed successfully.", "Success!", 1);
                closeConn(con, stmt);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

            setTable();
        }

        public void setTable() {
            getDataFromFacilityMaintenance();

            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            tblFacilityInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
            tblFacilityInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFacilityInMaintenance));
        }

        private void refresh() {
        setTable();
        }
    }

