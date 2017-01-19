package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import nl.Databeest.Login.PasswordAuthentication;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Created by timok on 09-01-17.
 */
public class Register extends SubMenuItem {
    private JPanel mainPanel;
    private JPanel guestInfoPanel;
    private JTextField firstNameTextField;
    private JTextField countryTextField;
    private JTextField prefiexTextField;
    private JTextField lastNameTextField;
    private JTextField phoneNumberTextField;
    private JTextField emalAddressTextField;
    private JTextField creditCardNumberTextField;
    private JTextField address2TextField;
    private JTextField addressTextField;
    private JComboBox genderComboBox;
    private JTextField debitCardNumberTextField;
    private JPasswordField passwordField;
    private JCheckBox joinLoyaltyProgramCheckBox;

    private PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    @Override
    protected String getMenuItemName() {
        return "Register";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public Register(){
        genderComboBox.addItem("M");
        genderComboBox.addItem("F");
    }

    public int getGuestId(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("EXEC SP_REGISTER_GUEST ?,?,?,?,?,?,?,?,?,?,?,?,?,?");
        stmt.setEscapeProcessing(true);

        if(firstNameTextField.getText() == null || firstNameTextField.getText().isEmpty()){
            stmt.setNull(1, Types.VARCHAR);
        }
        else {
            stmt.setString(1, firstNameTextField.getText());
        }

        if(prefiexTextField.getText() == null || prefiexTextField.getText().isEmpty()){
            stmt.setNull(2, Types.VARCHAR);
        }
        else {
            stmt.setString(2, prefiexTextField.getText());
        }

        if(lastNameTextField.getText() == null || lastNameTextField.getText().isEmpty()){
            stmt.setNull(3, Types.VARCHAR);
        }
        else {
            stmt.setString(3, lastNameTextField.getText());
        }

        if(creditCardNumberTextField.getText() == null || creditCardNumberTextField.getText().isEmpty()){
            stmt.setNull(4, Types.VARCHAR);
        }
        else {
            stmt.setString(4, creditCardNumberTextField.getText());
        }

        if(debitCardNumberTextField.getText() == null || debitCardNumberTextField.getText().isEmpty()){
            stmt.setNull(5, Types.VARCHAR);
        }
        else {
            stmt.setString(5, debitCardNumberTextField.getText());
        }

        if(phoneNumberTextField.getText() == null || phoneNumberTextField.getText().isEmpty()){
            stmt.setNull(6, Types.VARCHAR);
        }
        else {
            stmt.setString(6, phoneNumberTextField.getText());
        }

        if(emalAddressTextField.getText() == null || emalAddressTextField.getText().isEmpty()){
            stmt.setNull(7, Types.VARCHAR);
        }
        else {
            stmt.setString(7, emalAddressTextField.getText());
        }

        stmt.setString(8, (String) genderComboBox.getSelectedItem());

        if(countryTextField.getText() == null || countryTextField.getText().isEmpty()){
            stmt.setNull(9, Types.VARCHAR);
        }
        else {
            stmt.setString(9, countryTextField.getText());
        }

        if(addressTextField.getText() == null || addressTextField.getText().isEmpty()){
            stmt.setNull(10, Types.VARCHAR);
        }
        else {
            stmt.setString(10, addressTextField.getText());
        }

        if(address2TextField.getText() == null || address2TextField.getText().isEmpty()){
            stmt.setNull(11, Types.VARCHAR);
        }
        else {
            stmt.setString(11, address2TextField.getText());
        }

        stmt.setNull(12, Types.DATE);

        if(passwordField.getPassword() == null){
            stmt.setNull(13, Types.VARCHAR);
        }
        else {
            stmt.setString(13, passwordAuthentication.hash(passwordField.getPassword()));
        }

        stmt.setBoolean(14, joinLoyaltyProgramCheckBox.isSelected());

        ResultSet rs = stmt.executeQuery();

        int guestId = -1;

        while(rs.next())
        {
            guestId = rs.getInt("GUEST_ID");
        }

        stmt.close();

        return guestId;
    }





}
