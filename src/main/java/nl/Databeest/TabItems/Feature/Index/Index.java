package nl.Databeest.TabItems.Feature.Index;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by timok on 15-12-16.
 */
public class Index extends SubMenuItem {
    private JPanel mainPanel;
    private JTable featureIndexTable;

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
    }

    private void setTable(){
        getFeatures();

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        featureIndexTable.getColumn("Delete").setCellRenderer(buttonRenderer);
        featureIndexTable.addMouseListener(new JTableButtonMouseListener(featureIndexTable));
    }

    private void getFeatures(){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("SELECT * FROM FEATURE INNER JOIN FEATURE_TYPE ON FEATURE.FEATURE_TYPE_NAME = FEATURE_TYPE.NAME WHERE FEATURE.DELETE_STATUS <> 1 ");
            stmt.setEscapeProcessing(true);

            featureIndexTable.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {
                    deleteFeature((String)row[0], Integer.parseInt((String) row[1]));
                }
            });

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void deleteFeature(String name, int seqNo){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("EXEC SP_DELETE_FEATURE ?,?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, name);
            stmt.setInt(2, seqNo);

            stmt.execute();

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        setTable();
    }
}