package nl.Databeest.TabItems.Room;

import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Room.Create.CreateRoom;
import nl.Databeest.TabItems.Room.Index.Index;
import nl.Databeest.TabItems.Room.Maintenance.addRoomMaintenance;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by timok on 15-12-16.
 */
public class RoomTab extends MenuItem{
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] items = new SubMenuItem[3];
        items[0] = new Index();
        items[1] = new CreateRoom();
        items[2] = new addRoomMaintenance();
        return items;
    }
}
