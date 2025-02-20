package thundersharp.aigs.spectre.core.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**

     * @apiNote Enter dateString in format of
     **/
    public static long getTimeStampOfOriginToday(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        return c.getTimeInMillis();
    }

    public static String getDateOfOriginDaysBeforeAfter(@NonNull int noOfYears){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        c.add(Calendar.YEAR,noOfYears);

        return TimeUtils.getDateFromTimeStamp(c.getTimeInMillis());
    }

    public static long getTimeStampOfOriginDaysBeforeAfterr(@NonNull int noOfDays){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        c.add(Calendar.DAY_OF_MONTH,noOfDays);

        return c.getTimeInMillis();
    }

    public static long getTimeStampOfOriginMonthBeforeAfterr(@NonNull int noOfMonth){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        c.add(Calendar.MONTH,noOfMonth);

        return c.getTimeInMillis();
    }



    public static long getTimeStampOfOriginDaysBeforeAfter(@NonNull int noOfYears){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear,mMonth,mDay,00,00,00);
        c.set(Calendar.MILLISECOND,00);

        c.add(Calendar.YEAR,noOfYears);

        return c.getTimeInMillis();
    }

    public static String getTimeFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;
    }

    public static String getTimeFromTimeStamp(String timeStamp) throws NumberFormatException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time;
        time = Long.parseLong(timeStamp);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;

    }

    public static String getDateFromTimeStamp(String timeStamp) throws NumberFormatException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        long time;
        time = Long.parseLong(timeStamp);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;

    }

    public static String getDateFromTimeStamp(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean checkForCorrectDateFormatInput(String date){
        String dd = date.substring(0,2);
        String dash = date.substring(2,3);
        String month = date.substring(3,5);
        String dash2 = date.substring(5,6);
        String year = date.substring(6);

        if (year.matches("[0-9]+")) {

            if (Integer.parseInt(year) >= 1940 && Integer.parseInt(year) <= 2008) {

                if (dd.matches("[0-9]+") && dd.length() == 2 && month.matches("[0-9]+") && month.length() == 2 && dash.equals("-") && dash2.equals("-")) {

                    if (Integer.parseInt(dd) > 0 && Integer.parseInt(dd) < 32) {

                        if (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 13) {

                            return true;
                        } else {
                            //Toast.makeText(getActivity(), "Month in the date is invalid", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    } else {
                        //Toast.makeText(getActivity(), "Day in the date is invalid", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else return false;

            }else return false;

        }else return false;

    }


    public static long getDaysLeftBetweenTimeStamp(String timestamp1, String timestamp2) throws ParseException {
        String dateStart = TimeUtils.getDateFromTimeStamp(timestamp1);
        String dateStop = TimeUtils.getDateFromTimeStamp(timestamp2);

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

        Date d1 = null;
        Date d2 = null;

        d1 = format.parse(dateStart);
        d2 = format.parse(dateStop);

         //in milliseconds
        long diff = d2.getTime() - d1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return  diffDays;
    }
}
