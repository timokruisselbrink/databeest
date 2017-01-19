package nl.Databeest.TabItems.Feature.removeFeatureMaintenance;

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
import java.sql.*;


/**
 * Created by A on 20/12/2016.
 */
public class removeFeatureMaintenance extends SubMenuItem{
    private JPanel mainPanel;
    private JTable tblFeatureInMaintenance;
    private JButton btnRefreshFeatureMaintenance;

    public removeFeatureMaintenance() {
        getRoomTypeNameAndFeatureSeqNoFromMaintenance();

        btnRefreshFeatureMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureMaintenanceRefresh();
            }
        });
    }

    @Override
    protected String getMenuItemName() {
        return "Remove Feature Maintenance";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    private void featureMaintenanceRefresh() {
        setTable();
    }


    public void getRoomTypeNameAndFeatureSeqNoFromMaintenance() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("SELECT FEATURE_TYPE.NAME, FEATURE_SEQ_NO FROM FEATURE_MAINTENANCE INNER JOIN FEATURE_TYPE ON FEATURE_MAINTENANCE.FEATURE_TYPE_ID = FEATURE_TYPE.FEATURE_TYPE_ID WHERE IS_CANCELLED <> 1");
            stmt.setEscapeProcessing(true);


            ResultSet rs = stmt.executeQuery();

            tblFeatureInMaintenance.setModel(new IndexAbstractTableModel(rs) {

                @Override
                public void deleteRow(Object[] row) {

                    deleteFeatureMaintenance((String) row[0], Integer.parseInt((String) row[1]), (String) row[2]);
                }
            });
            closeConn(con, stmt);

            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            tblFeatureInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
            tblFeatureInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFeatureInMaintenance));


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public String getFeatureTypeStartTime(String typeName) {
        Connection con = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SELECT START_TIME FROM FEATURE_TYPE WHERE NAME = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, typeName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getString("START_TIME");

            }
            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return "1";
    }

        private void deleteFeatureMaintenance(String typeName, int seqNo, String startTime){
            Connection con = getConnection();
            PreparedStatement stmt = null;

            try {
                stmt = con.prepareStatement("EXEC SP_DELETE_FEATURE_MAINTENANCE ?,?,?,?,?");
                stmt.setEscapeProcessing(true);

                stmt.setString(1, typeName);
                stmt.setInt(2, seqNo);
                stmt.setString(3, getFeatureTypeStartTime(typeName));
                stmt.setString(4, startTime);
                stmt.setInt(5, User.getInstance().getUserId());

                stmt.execute();
                JOptionPane.showMessageDialog(null, "The feature maintenance type has been removed successfully.", "Success!", 1);
                closeConn(con, stmt);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

            setTable();
        }

    private void setTable(){
        getRoomTypeNameAndFeatureSeqNoFromMaintenance();

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        tblFeatureInMaintenance.getColumn("Delete").setCellRenderer(buttonRenderer);
        tblFeatureInMaintenance.addMouseListener(new JTableButtonMouseListener(tblFeatureInMaintenance));
    }


}
