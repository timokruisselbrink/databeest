package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by timok on 09-01-17.
 */
public class Login extends SubMenuItem {
    private JPanel mainPanel;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private int guestId;

    private PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    public Login() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void login(){
        Connection con = getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT [dbo].[GUEST].[GUEST_ID], [PASSWORD] FROM [dbo].[GUEST] INNER JOIN [dbo].[GUEST_WITH_ACCOUNT] ON [dbo].[GUEST].[GUEST_ID] = [dbo].[GUEST_WITH_ACCOUNT].[GUEST_ID] WHERE [dbo].[GUEST].[EMAIL_ADDRESS] = ?");
            stmt.setEscapeProcessing(true);

            stmt.setString(1, emailTextField.getText());

            ResultSet rs = stmt.executeQuery();

            String passwordHash= "";
            int _guestId = -1;


            int count = 0;
            while(rs.next())
            {
                passwordHash = rs.getString("PASSWORD");
                _guestId = rs.getInt("GUEST_ID");
                count++;
            }

            if(count == 0){
                throw new SQLException("User unknown");
            }

            if(passwordAuthentication.authenticate(passwordField.getPassword(), passwordHash))        {
                guestId = _guestId;
                guestLoginSuccessful();
            }
            else {
                throw new SQLException("Password does not mach");
            }

            closeConn(con, stmt);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void guestLoginSuccessful(){
        mainPanel.removeAll();

        Connection con = getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT [FIRST_NAME], [LAST_NAME_AFFIX], [LAST_NAME], [GUEST_WITH_ACCOUNT].[IN_LOYALTY_PROGRAM], [GUEST_WITH_ACCOUNT].[LOYALTY_POINTS]\n" +
                    "  FROM [dbo].[GUEST]\n" +
                    "  INNER JOIN [dbo].[GUEST_WITH_ACCOUNT] ON [dbo].[GUEST].[GUEST_ID] = [dbo].[GUEST_WITH_ACCOUNT].[GUEST_ID]\n" +
                    "  WHERE [dbo].[GUEST].[GUEST_ID] = ?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {

                mainPanel.setLayout(new GridLayout());

                mainPanel.add(new JLabel(
                        "Welcome back, " + rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME_AFFIX") + " " + rs.getString("LAST_NAME")
                ));

                mainPanel.revalidate();
                mainPanel.repaint();
            }

            closeConn(con, stmt);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @Override
    protected String getMenuItemName() {
        return "Login";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public int getGuestId(Connection con) throws SQLException {
        return guestId;
    }
}
