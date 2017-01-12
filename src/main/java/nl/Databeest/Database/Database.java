package nl.Databeest.Database;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by timok on 18-11-16.
 */
public class Database {

    // JDBC driver name and database URL
    private static String connectionString = "";


    public Database (){
        if(connectionString == "") {
            getConnectionString();
        }
    }

    protected Connection getConnection() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

//          //STEP 3: Open a connection
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

    private void getConnectionString(){
        //database.properties

        Properties prop = new Properties();

        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("database.properties");

            prop.load(stream);
            connectionString = prop.getProperty("connectionString");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}





