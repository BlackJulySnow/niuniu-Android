package cc.liyaya.mylove.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static long dayTime = 1000 * 60 * 60 * 24;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");;
    public static Date string2Date(String text) throws ParseException {
        return format.parse(text);
    }
    public static long getToday(){
        Calendar c=Calendar.getInstance(Locale.CHINA);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return c.getTime().getTime() / 1000 * 1000;
    }
    public static long getMonday(){
        Calendar c=Calendar.getInstance(Locale.CHINA);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime().getTime() / 1000 * 1000;
    }
}
