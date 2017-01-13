package nl.Databeest.Navigation;

import nl.Databeest.TabItems.Facility.facilityTabs;
import nl.Databeest.TabItems.Feature.FeatureTab;
import nl.Databeest.TabItems.PartnerRoom.PartnerRoomTabs;
import nl.Databeest.TabItems.Reservation.ReservationMenuItem;
import nl.Databeest.TabItems.Room.RoomTab;
import nl.Databeest.TabItems.Specification.SpecificationTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by timok on 17-11-16.
 */
public class NavigationPanel extends JPanel {

    public NavigationPanel() {
        createTabs();
    }
    
    private JTabbedPane tabbedPane = new JTabbedPane();

     private void createTabs(){
 
         setLayout(new BorderLayout());
 
         JButton refreshApp = new JButton("Refresh");
         refreshApp.addActionListener(new ActionListener() {
 
             public void actionPerformed(ActionEvent e) {
                 createApp();
             }
         });
 
         add(refreshApp, BorderLayout.NORTH );
 
 
 
         //Add the tabbed pane to this panel.
         add(tabbedPane, BorderLayout.CENTER);
 
         //The following line enables to use scrolling tabs.
         tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
 
         createApp();
 
     }
 
     private void createApp(){
         tabbedPane.removeAll();
  
          tabbedPane.addTab("Receptionist",  new ReservationMenuItem());
          tabbedPane.add("Feature", new FeatureTab());
          tabbedPane.add("Room", new RoomTab());
          tabbedPane.add("Specification", new SpecificationTab());
          tabbedPane.add("Room of Partner", new PartnerRoomTabs());
         tabbedPane.add("Facility", new facilityTabs());
  
 
         //Add the tabbed pane to this panel.
         add(tabbedPane);
 
         //The following line enables to use scrolling tabs.
         tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


}
