package nl.Databeest.Helpers;

/**
 * Created by timok on 12-01-17.
 */
public class RoleHelper {

    public static boolean isEditor(){
        if(!isGuest() && User.getInstance().getRoles().contains("Editor")){
            return true;
        }
        return false;
    }

    public static boolean isCreator(){
        if(!isGuest() && User.getInstance().getRoles().contains("Creator")){
            return true;
        }
        return false;
    }

    public static boolean isDisplayer(){
        if(!isGuest() && User.getInstance().getRoles().contains("Displayer")){
            return true;
        }
        return false;
    }

    public static boolean isDeleter(){
        if(!isGuest() && User.getInstance().getRoles().contains("Deleter")){
            return true;
        }
        return false;
    }

    public static boolean isGuest(){
        return User.getInstance().isGuest();
    }
}
