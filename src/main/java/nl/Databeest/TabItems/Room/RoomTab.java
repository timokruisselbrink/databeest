package nl.Databeest.TabItems.Room;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Room.Create.CreateRoom;
import nl.Databeest.TabItems.Room.Index.Index;
import nl.Databeest.TabItems.Room.Maintenance.addRoomMaintenance;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by timok on 15-12-16.
 */
public class RoomTab extends MenuItem{

    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {

        ArrayList<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();

        if(RoleHelper.isDisplayer()){
            subMenuItems.add(new Index());
        }

        if(RoleHelper.isCreator()){
            subMenuItems.add(new CreateRoom());
            subMenuItems.add(new addRoomMaintenance());
        }
        return subMenuItems;
    }
}
