package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by timok on 30-11-16.
 */
public abstract class AvailableRoomTableModel extends AbstractTableModel {

    Vector cache;
    String[] headers;
    int colCount;

    public AvailableRoomTableModel(ResultSet rs){
        cache = new Vector();

        try {


            ResultSetMetaData meta = rs.getMetaData();
            colCount = meta.getColumnCount() +1;

            headers = new String[colCount];
            for (int h = 1; h <= colCount -1; h++) {
                headers[h - 1] = meta.getColumnName(h);
            }

            headers[colCount -1] = "Reserve";

            while (rs.next()) {
                Object[] record = new Object[colCount + 1];
                for (int i = 0; i < colCount -1; i++) {
                    record[i] = rs.getString(i + 1);
                }

                record[colCount -1] = false;

                fireTableDataChanged();
                cache.addElement(record);
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
        if(column == colCount -1){
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        boolean b = (Boolean) aValue;
        ((Object[]) cache.elementAt(row))[col] = b;

        if (b) {
            addSelectedRoom(Integer.parseInt((String) ((Object[]) cache.elementAt(row))[0]), (String) ((Object[]) cache.elementAt(row))[2]);
        } else {
            // checked.remove(row);
        }
        fireTableRowsUpdated(row, row);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
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

    public Object getValueAt(int row, int col) {        return ((Object[]) cache.elementAt(row))[col];
    }

    public abstract void addSelectedRoom(int roomNr, String roomType);

}


