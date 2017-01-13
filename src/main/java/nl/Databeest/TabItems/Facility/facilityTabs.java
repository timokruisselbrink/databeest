package nl.Databeest.TabItems.Facility;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.TabItems.Facility.addFacility.addFacility;
import nl.Databeest.TabItems.Facility.addFacilityMaintenance.addFacilityMaintenance;
import nl.Databeest.TabItems.Facility.removeFacility.removeFacility;
import nl.Databeest.TabItems.Facility.removeFacilityMaintenance.removeFacilityMaintenance;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by A on 13/01/2017.
 */
public class facilityTabs extends MenuItem {
    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {

        ArrayList<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();

        if(RoleHelper.isDisplayer()){
            subMenuItems.add(new removeFacility());
        }

        if(RoleHelper.isCreator()){
            subMenuItems.add(new addFacility());
            subMenuItems.add(new addFacilityMaintenance());
            subMenuItems.add(new removeFacilityMaintenance());

        }

        return subMenuItems;
    }
}
