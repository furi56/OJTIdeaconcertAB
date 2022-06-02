package storybird.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static long getDifferenceDay(Date fromData, Date toDate){
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);

        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromData);

        long diffSec = (toCal.getTimeInMillis() - fromCal.getTimeInMillis()) / 1000;
        long diffDays = diffSec / (24*60*60);

        return diffDays;
    }
    /*
    * @param dateStr 일자 문자열
    * @param field 추출 필드값 [ ex) Calendar.DATE ]
    */
    public static int getDateField(String dateStr, int field) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /*
     * @param date 일자
     * @param field 추출 필드값 [ ex) Calendar.DATE ]
     */
    public static int getDateField(Date date, int field) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }
}
