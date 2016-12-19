package nl.Databeest.Database;

import javax.swing.*;
import java.sql.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by timok on 18-11-16.
 */
public class Database {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String connectionString = "";
    static final String defaultConnectionString = "jdbc:sqlserver://databeest.database.windows.net:1433;database=Hotelreservationsystem;user=databeest@databeest;password=data123beestHAN;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

    protected Connection getConnection() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        if(connectionString == "") {
            connectionString = JOptionPane.showInputDialog
                    (null, "<html>Enter connection string:", defaultConnectionString);
        }
                    //STEP 3: Open a connection
            return DriverManager.getConnection(connectionString);
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return null;
    }

    protected void closeConn(Connection conn, PreparedStatement stmt) throws SQLException {
        stmt.close();
        conn.close();
    }

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Database.class));
    public void log(SQLException e) {
        LOGGER.log(Level.SEVERE, e.getSQLState());
        LOGGER.log(Level.SEVERE, e.getMessage());
        LOGGER.log(Level.SEVERE, "Error with database", e);
    }
}





