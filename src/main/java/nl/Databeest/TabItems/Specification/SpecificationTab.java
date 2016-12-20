package nl.Databeest.TabItems.Specification;

import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Specification.Index.Index;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by A on 20/12/2016.
 */
public class SpecificationTab extends MenuItem {
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] items = new SubMenuItem[1];
        items[0] = new Index();

        return items;
    }
}
