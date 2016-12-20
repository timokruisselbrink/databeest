package nl.Databeest.TabItems.Feature;

import nl.Databeest.TabItems.Feature.Index.Index;
import nl.Databeest.TabItems.Feature.addFeature.addFeature;
import nl.Databeest.TabItems.Feature.addFeatureMaintenance.addFeatureMaintenance;
import nl.Databeest.TabItems.Feature.createFeatureType.createFeatureType;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by timok on 15-12-16.
 */
public class FeatureTab extends MenuItem {
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] items = new SubMenuItem[4];
        items[0] = new Index();
        items[1] = new createFeatureType();
        items[2] = new addFeature();
        items[3] = new addFeatureMaintenance();
        //items[4] = new removeFeatureMaintenance();
        return items;
    }
}
