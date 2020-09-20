package com.codebase.inmateapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A class with static util methods.
 */

public class DateUtils {

    private static final String TAG = "DateUtils";

    // This class should not be initialized
    private DateUtils() {

    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static String formatTimeWithMarker(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static int getHourOfDay(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("H", Locale.getDefault());
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    public static int getMinute(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("m", Locale.getDefault());
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    public static String getMinutesAndSeconds(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * If the given time is of a different year, display the date and year.
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     *
     * @param timeInMillis The time to convert, in milliseconds.
     * @return The time or date.
     */
    public static String formatDateTime(long timeInMillis) {
        if (isToday(timeInMillis)) {
            return formatTimeWithMarker(timeInMillis);
        } else if (isSameYear(timeInMillis)) {
            return formatDate(timeInMillis);
        } else {
            return formatDateWithYear(timeInMillis);
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    public static String formatDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Formats timestamp to 'date month, year' format (e.g. 'February 3, 2020').
     */
    public static String formatDateWithYear(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    public static boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /**
     * Returns whether the given date is of same year, based on the user's current locale.
     */
    public static boolean isSameYear(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /**
     * Checks if two dates are of the same day.
     *
     * @param millisFirst  The time in milliseconds of the first date.
     * @param millisSecond The time in milliseconds of the second date.
     * @return Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    public static boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond));
    }


    public static String getTimeAgo(long pastTime) {
        String convTime;
        Date nowTime = new Date();

        long dateDiff = nowTime.getTime() - pastTime;

        long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
        long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

        if (second < 60) {
            convTime = "now";
        } else if (minute < 60) {
            convTime = minute + "m";
        } else if (hour < 24) {
            convTime = hour + "h";
        } else if (day >= 7) {
            if (day > 360) {
                convTime = (day / 360) + "y";
            } else if (day > 30) {
                convTime = (day / 30) + "m";
            } else {
                convTime = (day / 7) + "w";
            }
        } else {
            convTime = day + "d";
        }
        return convTime;
    }


    public static String getDateFull(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}

