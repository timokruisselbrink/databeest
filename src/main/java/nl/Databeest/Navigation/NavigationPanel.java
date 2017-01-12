package nl.Databeest.Navigation;

import nl.Databeest.TabItems.Feature.FeatureTab;
import nl.Databeest.TabItems.Reservation.ReservationMenuItem;
import nl.Databeest.TabItems.Room.RoomTab;
import nl.Databeest.TabItems.Specification.SpecificationTab;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by timok on 17-11-16.
 */
public class NavigationPanel extends JPanel {

    public NavigationPanel() {
        createTabs();
    }

    private void createTabs(){
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Receptionist",  new ReservationMenuItem());
        tabbedPane.add("Feature", new FeatureTab());
        tabbedPane.add("Room", new RoomTab());
        tabbedPane.add("Specification", new SpecificationTab());


        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


}
