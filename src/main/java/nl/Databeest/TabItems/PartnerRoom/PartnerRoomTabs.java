package nl.Databeest.TabItems.PartnerRoom;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.PartnerRoom.Index.DeletePartnerRooms;
import nl.Databeest.TabItems.PartnerRoom.addRoomOfPartner.addRoomOfPartner;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by A on 12/01/2017.
 */
public class PartnerRoomTabs extends MenuItem {
    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {
        ArrayList<SubMenuItem> items = new ArrayList<SubMenuItem>();


        if(RoleHelper.isDisplayer()){
            items.add(new DeletePartnerRooms());
        }
        if(RoleHelper.isCreator()) {
            items.add(new addRoomOfPartner());
        }
        return items;
    }
}
