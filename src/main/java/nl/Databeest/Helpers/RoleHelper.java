package nl.Databeest.Helpers;

import java.util.ArrayList;

/**
 * Created by timok on 12-01-17.
 */
public class RoleHelper {

    public static boolean isEditor(){
        if(UserRoles.getInstance().getRoles().contains("Editor")){
            return true;
        }
        return false;
    }

    public static boolean isCreator(){
        if(UserRoles.getInstance().getRoles().contains("Creator")){
            return true;
        }
        return false;
    }

    public static boolean isDisplayer(){
        if(UserRoles.getInstance().getRoles().contains("Displayer")){
            return true;
        }
        return false;
    }

    public static boolean isDeleter(){
        if(UserRoles.getInstance().getRoles().contains("Deleter")){
            return true;
        }
        return false;
    }
}
