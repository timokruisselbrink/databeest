package nl.Databeest.TabItems;

import nl.Databeest.Database.Database;

import java.awt.*;

/**
 * Created by timok on 17-11-16.
 */
public abstract class SubMenuItem extends Database {

    public SubMenuItem(){
    }

    abstract protected String getMenuItemName();

    abstract protected Component getMainPanel();

}
