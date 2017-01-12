package nl.Databeest.TabItems.Specification;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.Helpers.UserRoles;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Specification.Index.Index;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by A on 20/12/2016.
 */
public class SpecificationTab extends MenuItem {
    public SpecificationTab() {
        super();
    }

    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {
        ArrayList<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();

        if(RoleHelper.isDisplayer()){
            subMenuItems.add(new Index());
        }

        if(RoleHelper.isCreator()){
            subMenuItems.add(new addSpecification());
        }
        return subMenuItems;
    }
}
