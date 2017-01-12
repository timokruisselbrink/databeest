package nl.Databeest.TabItems.Feature;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.TabItems.Feature.Index.Index;
import nl.Databeest.TabItems.Feature.addFeature.addFeature;
import nl.Databeest.TabItems.Feature.addFeatureMaintenance.addFeatureMaintenance;
import nl.Databeest.TabItems.Feature.createFeatureType.createFeatureType;
import nl.Databeest.TabItems.Feature.removeFeatureMaintenance.removeFeatureMaintenance;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by timok on 15-12-16.
 */
public class FeatureTab extends MenuItem {


    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {

        ArrayList<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();

        if(RoleHelper.isDisplayer()){
            subMenuItems.add(new Index());
        }

        if(RoleHelper.isCreator()){
            subMenuItems.add(new createFeatureType());
            subMenuItems.add(new addFeature());
            subMenuItems.add(new addFeatureMaintenance());
        }
        if(RoleHelper.isDeleter()){
            subMenuItems.add(new removeFeatureMaintenance());
        }
        return subMenuItems;
    }
}
