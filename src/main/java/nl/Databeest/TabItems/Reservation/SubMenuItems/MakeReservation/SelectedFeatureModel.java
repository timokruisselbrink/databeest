package nl.Databeest.TabItems.Reservation.SubMenuItems.MakeReservation;

/**
 * Created by timok on 10-01-17.
 */
public class SelectedFeatureModel {
    private String featureTypeName;
    private int amount;

    public SelectedFeatureModel(String featureTypeName, int amount) {
        this.featureTypeName = featureTypeName;
        this.amount = amount;
    }

    public String getFeatureTypeName() {
        return featureTypeName;
    }

    public int getAmount() {
        return amount;
    }
}
