package nl.Databeest.TabItems.Reservation.SubMenuItems.CancelReservation;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by timok on 11-12-16.
 */
public class CancelReservation extends SubMenuItem {
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JTextField firstNameTextField;
    private JLabel firstNameLabel;
    private JLabel prefixLabel;
    private JLabel lastNameLabel;
    private JLabel emalAddressLabel;
    private JLabel reservationNumberLabel;
    private JTextField prefixTextField;
    private JTextField lastNameTextField;
    private JTextField emalAddressTextField;
    private JTextField reservationNumberTextField;
    private JButton searchButon;
    private JTable reservationsJTable;
    private JButton cancelButton;
    private JButton clearScreenButton;

    private int selectedReservation;

    public CancelReservation() {
        searchButon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchReservations();
            }
        });

        reservationsJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
               if(reservationsJTable.getSelectedRow() != -1){
                   selectedReservation = Integer.parseInt(reservationsJTable.getValueAt(reservationsJTable.getSelectedRow(), 0).toString());
               }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection con = getConnection();
                PreparedStatement stmt = null;

                try {
                    stmt = con.prepareStatement("SP_CANCEL_RESERVATION ?");
                    stmt.setEscapeProcessing(true);

                    stmt.setInt(1, selectedReservation);

                    stmt.execute();

                    closeConn(con, stmt);

                    searchReservations();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        clearScreenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                firstNameTextField.setText("");
                prefixTextField.setText("");
                lastNameTextField.setText("");
                emalAddressTextField.setText("");
                reservationNumberTextField.setText("");
                reservationsJTable.setModel(new DefaultTableModel());
            }
        });
    }

    private void searchReservations(){
        Connection con = getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SP_GET_RESERVATIONS_BY_SEARCH_CRITERIA ?,?,?,?,?");
            stmt.setEscapeProcessing(true);

            if(firstNameTextField.getText() != null && !firstNameTextField.getText().isEmpty()){
                stmt.setString(1, firstNameTextField.getText());
            }
            else {
                stmt.setNull(1, Types.VARCHAR);
            }

            if(prefixTextField.getText() != null && !prefixTextField.getText().isEmpty()){
                stmt.setString(2, prefixTextField.getText());
            }
            else {
                stmt.setNull(2, Types.VARCHAR);
            }

            if(lastNameTextField.getText() != null && !lastNameTextField.getText().isEmpty()){
                stmt.setString(3, lastNameTextField.getText());
            }
            else {
                stmt.setNull(3, Types.VARCHAR);
            }

            if(emalAddressTextField.getText() != null && !emalAddressTextField.getText().isEmpty()){
                stmt.setString(4, emalAddressTextField.getText());
            }
            else {
                stmt.setNull(4, Types.VARCHAR);
            }

            if(reservationNumberTextField.getText() != null && !reservationNumberTextField.getText().isEmpty()){
                stmt.setInt(5, Integer.parseInt(reservationNumberTextField.getText()));
            }
            else {
                stmt.setNull(5, Types.INTEGER);
            }



            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) reservationsJTable.getModel();
            model.setRowCount(0);

            String[] tableColumnsName = {"Reservation number", "Reservation date", "First name", "Prefix", "Last name", "Birthday", "Amount of rooms"};
            DefaultTableModel aModel = (DefaultTableModel) reservationsJTable.getModel();
            aModel.setColumnIdentifiers(tableColumnsName);

            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int colNo = rsmd.getColumnCount();
            while(rs.next()){
                Object[] objects = new Object[colNo];
                // tanks to umit ozkan for the bug fix!
                for(int i=0;i<colNo;i++){
                    objects[i]=rs.getObject(i+1);
                }
                aModel.addRow(objects);
            }
            reservationsJTable.setModel(aModel);



            closeConn(con, stmt);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    protected String getMenuItemName() {
        return "Cancel reservation";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }
}
