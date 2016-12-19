package nl.Databeest.Helpers;

import java.sql.Date;

/**
 * Created by timok on 30-11-16.
 */
public class DateHelper {

    public static Date createSqlDate(String day, int month, String year){
        return Date.valueOf(year +"-"+ month + "-" + day);
    }
}
