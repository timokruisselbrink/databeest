package nl.Databeest.TabItems.Reservation;

import nl.Databeest.TabItems.MenuItem;
import nl.Databeest.TabItems.Reservation.SubMenuItems.CancelReservation.CancelReservation;
import nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation.MakeReservation;
import nl.Databeest.TabItems.SubMenuItem;

/**
 * Created by timok on 30-11-16.
 */
public class ReservationMenuItem  extends MenuItem {
    @Override
    protected SubMenuItem[] getMenuItems() {
        SubMenuItem[] subMenuItems = new SubMenuItem[2];
        subMenuItems[0] = new MakeReservation();
        subMenuItems[1] = new CancelReservation();
        return subMenuItems;
    }
}
