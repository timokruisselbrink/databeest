package nl.Databeest.TabItems.Specification.Index;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by A on 20/12/2016.
 */
public class Index extends SubMenuItem {
    private JPanel mainPanel;
    private JTable tblSpecifications;

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
        getSpecifications();

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        tblSpecifications.getColumn("Delete").setCellRenderer(buttonRenderer);
        tblSpecifications.addMouseListener(new JTableButtonMouseListener(tblSpecifications));
    }

    private void getSpecifications(){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("SELECT NAME, PRICE FROM SPECIFICATION WHERE IS_DELETED <> 1");
            stmt.setEscapeProcessing(true);

            tblSpecifications.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {
                        deleteFeature((String)row[0]);
                    }
            });

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void deleteFeature(String name){
        Connection con = getConnection();
        PreparedStatement stmt = null;


        try {
            stmt = con.prepareStatement("EXEC SP_DELETE_SPECIFICATION ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, name);


            stmt.execute();

            closeConn(con, stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        setTable();
    }
}
