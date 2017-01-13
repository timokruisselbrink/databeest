package nl.Databeest.Login;

import nl.Databeest.Database.Database;
import nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation.PasswordAuthentication;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog{

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JButton btnAsGuest;
    private boolean succeeded;
    private boolean asGuest = false;

    private ArrayList<String> roles;

    private int userId;



    PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        tfUsername.setText(passwordAuthentication.hash("test123"));

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Database database = new Database();
                Connection con = database.getConnection();

                try {
                    PreparedStatement stmt = con.prepareStatement("SELECT [dbo].[EMPLOYEE].[EMPLOYEE_ID], [PASSWORD] FROM [dbo].[EMPLOYEE] WHERE [dbo].[EMPLOYEE].[EMAIL_ADDRESS] = ?");
                    stmt.setEscapeProcessing(true);

                    stmt.setString(1, getUsername());

                    ResultSet rs = stmt.executeQuery();

                    String passwordHash= "";



                    int count = 0;
                    while(rs.next())
                    {
                        passwordHash = rs.getString("PASSWORD");
                        userId = rs.getInt("EMPLOYEE_ID");
                        count++;
                    }

                    if(count == 0){
                        throw new SQLException("User unknown");
                    }

                    if(passwordAuthentication.authenticate(getPassword(), passwordHash))        {
                        getRoles(userId, con);
                        succeeded = true;
                        dispose();
                    }
                    else {
                        throw new SQLException("Password does not mach");
                    }

                    con.close();
                    stmt.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnAsGuest = new JButton("As Guest");
        btnAsGuest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                asGuest = true;
                succeeded = true;
                dispose();
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnAsGuest);
        bp.add(btnCancel);


        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public void getRoles(int employeeId, Connection con){
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT [ROLE_NAME] FROM [dbo].[EMPLOYEE_ROLE] WHERE [dbo].[EMPLOYEE_ROLE].[EmPLOYEE_ID] = ?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, employeeId);

            ResultSet rs = stmt.executeQuery();

            roles = new ArrayList<String>();
            while(rs.next())
            {
                roles.add(rs.getString("ROLE_NAME"));
            }
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isAsGuest() {
        return asGuest;
    }
}