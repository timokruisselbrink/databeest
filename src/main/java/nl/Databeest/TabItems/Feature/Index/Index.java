package nl.Databeest.TabItems.Feature.Index;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.Helpers.RoleHelper;
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
 * Created by timok on 15-12-16.
 */
public class Index extends SubMenuItem {
    private JPanel mainPanel;
    private JTable featureIndexTable;
    private JButton btnRefreshFeatureIndex;

    @Override
    protected String getMenuItemName() {
        return "Index";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public Index(){
        setTable();
        btnRefreshFeatureIndex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
    }

    private void refresh() {setTable();}

    private void setTable(){
        getFeatures();

        if(RoleHelper.isDeleter()) {
            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            featureIndexTable.getColumn("Delete").setCellRenderer(buttonRenderer);
            featureIndexTable.addMouseListener(new JTableButtonMouseListener(featureIndexTable));
        }
    }

    private void getFeatures(){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("SELECT FEATURE_TYPE.NAME, SEQ_NO, FEATURE_TYPE.START_TIME FROM FEATURE INNER JOIN FEATURE_TYPE ON FEATURE.FEATURE_TYPE_ID = FEATURE_TYPE.FEATURE_TYPE_ID WHERE IS_DELETED <> 1");
            stmt.setEscapeProcessing(true);

            featureIndexTable.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {


                    deleteFeature((String)row[0], Integer.parseInt((String) row[1]),(String)row[2]);
            }
            });

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void deleteFeature(String name, int seqNo, String start_time){
            Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("EXEC SP_DELETE_FEATURE ?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, name);
            stmt.setInt(2, seqNo);
            stmt.setString(3, start_time);
            stmt.setInt(4, User.getInstance().getUserId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The feature has been successfully removed.", "Success!", 1);

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        setTable();
    }


}
