package nl.Databeest.TabItems.Room.Create;

/**
 * Created by Freek on 20-12-2016.
 */
public class SelectedFeatureModel {
    private String name;
    private int amount;
    private String startTime;

    public SelectedFeatureModel(String name, int amount, String startTime){
        this.name = name;
        this.amount = amount;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStartTime() {
        return startTime;
    }
}
