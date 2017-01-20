package nl.Databeest.TabItems.Room.Create;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Freek on 20-12-2016.
 */
public class selectFeatureAbstractTableModel extends AbstractTableModel{

    Vector cache;
    String[] headers;
    int colCount;

    ArrayList<Integer> selectedRows = new ArrayList<Integer>();

    public selectFeatureAbstractTableModel(ResultSet rs){
        cache = new Vector();

        try {


            ResultSetMetaData meta = rs.getMetaData();
            colCount = meta.getColumnCount() +2;

            headers = new String[colCount];
            for (int h = 1; h <= colCount -2; h++) {
                headers[h - 1] = meta.getColumnName(h);
            }

            headers[colCount -2] = "Select";
            headers[colCount -1] = "Max amount";

            while (rs.next()) {
                Object[] record = new Object[colCount + 2];
                for (int i = 0; i < colCount -2; i++) {
                    record[i] = rs.getString(i + 1);
                }

                record[colCount -2] = false;
                record[colCount -1] = 0;

                fireTableDataChanged();
                cache.addElement(record);
            }

            rs.close();

            fireTableChanged(null);


        } catch (SQLException e) {
            e.printStackTrace();
            cache = new Vector();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == colCount -2 || column == colCount -1){
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {

        if(col == colCount -2){
            boolean b = (Boolean) aValue;
            ((Object[]) cache.elementAt(row))[col] = b;

            if (b) {
                if(!selectedRows.contains(row)){
                    selectedRows.add(row);
                    ((Object[]) cache.elementAt(row))[col +1] = 1;
                }
            } else {
                if(selectedRows.contains(row)){
                    selectedRows.remove(row);
                    ((Object[]) cache.elementAt(row))[col +1] = 0;
                }
            }

        }
        else if(col == colCount -1){
            int v = (Integer) aValue;
            ((Object[]) cache.elementAt(row))[col] = v;
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

    public ArrayList<SelectedFeatureModel> getSelectedFeatures(){
        ArrayList<SelectedFeatureModel> result = new ArrayList<SelectedFeatureModel>();
        for (int selectedRow: selectedRows) {
            result.add(new SelectedFeatureModel(
                    ((String)getValueAt(selectedRow, 0)),
                    ((Integer)getValueAt(selectedRow, colCount -1)),
                    ((String)getValueAt(selectedRow, 1))
                    )
            );
        }

        return result;
    }

}
