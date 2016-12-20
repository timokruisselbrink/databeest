package nl.Databeest.TabItems.Room.Create;

/**
 * Created by Freek on 20-12-2016.
 */
public class SelectedFeatureModel {
    private String name;
    private int amount;

    public SelectedFeatureModel(String name, int amount){
        this.name = name;
        this.amount = amount;
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
}
