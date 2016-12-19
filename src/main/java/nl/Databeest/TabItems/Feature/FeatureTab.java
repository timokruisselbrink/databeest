package nl.Databeest.TabItems.Feature;

import nl.Databeest.TabItems.Feature.Index.Index;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by timok on 15-12-16.
 */
public class FeatureTab extends MenuItem {
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] items = new  SubMenuItem[1];
        items[0] = new Index();
        return items;
    }
}
