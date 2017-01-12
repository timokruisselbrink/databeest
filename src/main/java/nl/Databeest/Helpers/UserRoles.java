package nl.Databeest.Helpers;

import java.util.ArrayList;

/**
 * Created by timok on 12-01-17.
 */
public class UserRoles {
    private static UserRoles ourInstance = new UserRoles();

    public static UserRoles getInstance() {
        return ourInstance;
    }

    private UserRoles() {
    }

    private ArrayList<String> roles;

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}
