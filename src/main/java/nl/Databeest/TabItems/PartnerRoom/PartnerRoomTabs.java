package nl.Databeest.TabItems.PartnerRoom;

import nl.Databeest.TabItems.Feature.Index.Index;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by A on 12/01/2017.
 */
public class PartnerRoomTabs extends MenuItem {
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] items = new SubMenuItem[0];
        items[0] = new Index();
        return items;
    }
}
