package nl.Databeest.TabItems;

import nl.Databeest.Helpers.DateHelper;
import nl.Databeest.Helpers.RoleHelper;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by timok on 15-12-16.
 */
public abstract class IndexAbstractTableModel extends AbstractTableModel {
    Vector cache;
    String[] headers;
    int colCount;

    public IndexAbstractTableModel(ResultSet rs){
        cache = new Vector();

        try {

            if(RoleHelper.isDeleter()){
                ResultSetMetaData meta = rs.getMetaData();
                colCount = meta.getColumnCount() +1;

                headers = new String[colCount];
                for (int h = 1; h <= colCount -1; h++) {
                    headers[h - 1] = meta.getColumnName(h);
                }

                headers[colCount -1] = "Delete";

                while (rs.next()) {
                    Object[] record = new Object[colCount + 1];
                    for (int i = 0; i < colCount -1; i++) {
                        record[i] = rs.getString(i + 1);
                    }

                    record[colCount -1] = "deleteButton";

                    fireTableDataChanged();
                    cache.addElement(record);
                }
            }
            else {
                ResultSetMetaData meta = rs.getMetaData();
                colCount = meta.getColumnCount();

                headers = new String[colCount];
                for (int h = 1; h <= colCount; h++) {
                    headers[h - 1] = meta.getColumnName(h);
                }

                while (rs.next()) {
                    Object[] record = new Object[colCount + 1];
                    for (int i = 0; i < colCount; i++) {
                        record[i] = rs.getString(i + 1);
                    }


                    fireTableDataChanged();
                    cache.addElement(record);
                }
            }






            fireTableChanged(null);


        } catch (SQLException e) {
            e.printStackTrace();
            cache = new Vector();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int c) {
        try {
            return getValueAt(0, c).getClass();
        }catch (Exception e) {
            return null;
        }
    }

    public String getColumnName(int i) {
        return headers[i];
    }

    public int getColumnCount() {
        return colCount;
    }

    public int getRowCount() {
        return cache.size();
    }

    public Object getValueAt(final int row, int col) {

       if(RoleHelper.isDeleter()) {
           if (col == colCount - 1) {
               final JButton button = new JButton("delete");
               button.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent arg0) {
                       deleteRow((Object[]) cache.elementAt(row));
                   }
               });
               return button;
           } else {
               return ((Object[]) cache.elementAt(row))[col];
           }
       }
        else {
           return ((Object[]) cache.elementAt(row))[col];
       }
    }

    public abstract void deleteRow(Object[] row);

}
