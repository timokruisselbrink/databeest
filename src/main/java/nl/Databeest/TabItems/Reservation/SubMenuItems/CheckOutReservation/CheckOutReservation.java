package nl.Databeest.TabItems.Reservation.SubMenuItems.CheckOutReservation;

import nl.Databeest.Helpers.User;
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
 * Created by Freek on 13-1-2017.
 */
public class CheckOutReservation extends SubMenuItem {
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JLabel firstNameLabel;
    private JTextField firstNameTextField;
    private JTextField prefixTextField;
    private JTextField reservationNumberTextField;
    private JTextField emalAddressTextField;
    private JTextField lastNameTextField;
    private JLabel prefixLabel;
    private JLabel lastNameLabel;
    private JLabel emalAddressLabel;
    private JLabel reservationNumberLabel;
    private JButton searchButon;
    private JButton clearScreenButton;
    private JButton btnCheckOut;
    private JTable reservationsJTable;

    private int selectedReservation;

    @Override
    protected String getMenuItemName() {
        return "Check Out Reservation";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public CheckOutReservation(){
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


        btnCheckOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection con = getConnection();
                PreparedStatement stmt =  null;

                try {
                    stmt = con.prepareStatement("SP_CHECK_OUT ?,?");
                    stmt.setEscapeProcessing(true);

                    stmt.setInt(1, selectedReservation);
                    stmt.setInt(2, User.getInstance().getUserId());

                    stmt.execute();
                    JOptionPane.showMessageDialog(null, "The reservation has been successfully checked out.", "Success!", 1);
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


}

