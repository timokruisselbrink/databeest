package nl.Databeest.TabItems.PartnerRoom.Index;

import nl.Databeest.Helpers.JTableButtonMouseListener;
import nl.Databeest.Helpers.JTableButtonRenderer;
import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.IndexAbstractTableModel;
import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by A on 12/01/2017.
 */
public class DeletePartnerRooms extends SubMenuItem {
    private JTable roomOfPartnerIndexTable;
    private JPanel mainPanel;
    private JButton btnRefreshRoomOfPartner;

    @Override
    protected String getMenuItemName() {
        return "Index";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }

    public DeletePartnerRooms(){
        setTable();
        btnRefreshRoomOfPartner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshRoomOfPartner();
            }
        });
    }

    private void refreshRoomOfPartner() {
        setTable();
    }
    private void setTable() {
        getRoomOfPartner();
        if(RoleHelper.isDeleter()) {
            TableCellRenderer buttonRenderer = new JTableButtonRenderer();
            roomOfPartnerIndexTable.getColumn("Delete").setCellRenderer(buttonRenderer);
            roomOfPartnerIndexTable.addMouseListener(new JTableButtonMouseListener(roomOfPartnerIndexTable));
        }
    }

    private void getRoomOfPartner() {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("select p.partner_name AS 'PARTNER NAME', rop.room_id AS 'ROOM ID', rop.start_time AS 'START TIME' from room_of_partner rop inner join [partner] p on rop.partner_name = p.partner_name where END_TIME >= GETDATE()");
            stmt.setEscapeProcessing(true);

            roomOfPartnerIndexTable.setModel(new IndexAbstractTableModel(stmt.executeQuery()) {
                @Override
                public void deleteRow(Object[] row) {
                    deleteRoomOfPartner((String)row[0], Integer.parseInt((String) row[1]), (String) row[2]);
                }
            });

            closeConn(con, stmt);


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }

    private void deleteRoomOfPartner(String partnerName, int roomId, String startDate) {
        Connection con = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = con.prepareStatement("EXEC SP_DELETE_ROOM_OF_PARTNER ?,?,?,?");
            stmt.setEscapeProcessing(true);

            stmt.setInt(1, roomId);
            stmt.setString(2, partnerName);
            stmt.setString(3, startDate);
            stmt.setInt(4, UserRoles.getInstance().getUserId());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "The room of partner has been deleted successfully.", "Success!", 1);
            closeConn(con, stmt);



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        setTable();
    }

}
