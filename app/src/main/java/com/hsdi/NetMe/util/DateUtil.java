package com.hsdi.NetMe.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Get Date object from a string of UTC timezone
     * @return Date object, null if failed
     */
    public static Date getDateFromStringUTC(String dateString) {
        if(dateString == null || dateString.isEmpty()) return null;

        Date date = null;

        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_STRING_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = df.parse(dateString);

        }
        catch (Exception e) {e.printStackTrace();}

        return date;
    }

    /**
     * Converts a time in milliseconds to a string date in the format received from the backend of NetMe
     * @param millisecondTime    time in milli-seconds
     * @return the string date
     * */
    public static String getUTCStringFromMillis(long millisecondTime) {
        return getUTCStringFromDate(new Date(millisecondTime));
    }

    /**
     * Converts a time in {@link Date} to a string date in the format received from the backend of NetMe
     * @param date    time in milli-seconds
     * @return the string date
     * */
    public static String getUTCStringFromDate(Date date) {
        if (date == null) return "";

        String str = "";

        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
            str = dateFormat.format(date);
        }
        catch (Exception e) {e.printStackTrace();}

        return str;
    }


    /**
     * Get string from a Date object in local time
     * @param date      the date to format
     * @param format    the format to use
     * @return empty String if failed
     */
    public static String getStringFromDate(Date date, String format) {
        if(date == null || format == null || format.isEmpty()) return "";

        String str = "";

        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            str = dateFormat.format(date);
        }
        catch (Exception e) {e.printStackTrace();}

        return str;
    }

    /**
     * Get elapsed time from now in String format
     * @param date past date to be calculate the elapse time
     * @return String which simplifies the elapsed time format to just
     * "days" or "hours" or "minutes" or "Just now"
     */
    public static String getElapsedTimeFromNow(Date date) {
        long duration = new Date().getTime()  - date.getTime();

        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        long diffInMonths = diffInDays / 30;
        long diffInYears = diffInMonths / 365;

        if (diffInYears > 0) {
            if (diffInYears == 1) return Long.toString(diffInYears) + " year ago";
            return Long.toString(diffInYears) + " years ago";
        }

        else if (diffInMonths > 0) {
            if (diffInMonths == 1) return Long.toString(diffInMonths) + " month ago";
            return  Long.toString(diffInMonths) + " months ago";
        }

        else if (diffInDays > 0) {
            if (diffInDays == 1) return Long.toString(diffInDays) + " day ago";
            return Long.toString(diffInDays) + " days ago";
        }

        else if (diffInHours > 0) {
            if (diffInHours == 1) return Long.toString(diffInHours) + " hour ago";
            return Long.toString(diffInHours) + " hours ago";
        }

        else if (diffInMinutes > 0) {
            if (diffInMinutes == 1) return Long.toString(diffInMinutes) + " minute ago";
            return Long.toString(diffInMinutes) + " minutes ago";
        }

        else return "Just now";
    }

    /**
     * Get elapsed time from now in String format
     * @param dateString past date string in UTC time zone
     * @return String which simplifies the elapsed time format to just
     * "days" or "hours" or "minutes" or "Just now"
     */
    public static String getElapsedTimeUTC(String dateString) {
        if(dateString == null || dateString.isEmpty()) return "";
        if(dateString.equalsIgnoreCase("just now")) return "Just now";
        return getElapsedTimeFromNow(getDateFromStringUTC(dateString));
    }
}

