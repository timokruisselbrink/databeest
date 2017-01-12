package nl.Databeest.TabItems.Reservation;

import nl.Databeest.Helpers.RoleHelper;
import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Reservation.SubMenuItems.CancelReservation.CancelReservation;
import nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation.MakeReservation;
import nl.Databeest.TabItems.SubMenuItem;

import java.util.ArrayList;

/**
 * Created by timok on 30-11-16.
 */
public class ReservationMenuItem  extends MenuItem {
    @Override
    protected ArrayList<SubMenuItem> getMenuItems() {
        ArrayList<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();

        if(RoleHelper.isCreator()){
            subMenuItems.add(new MakeReservation());
        }
        if(RoleHelper.isDeleter()){
            subMenuItems.add(new CancelReservation());
        }
        return subMenuItems;
    }
}
