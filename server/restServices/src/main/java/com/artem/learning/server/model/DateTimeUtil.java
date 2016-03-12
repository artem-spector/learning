package com.artem.learning.server.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
public class DateTimeUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS z";
    public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";

    public static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }

    public static String formatDateOnly(Date date) {
        return new SimpleDateFormat(DATE_ONLY_FORMAT).format(date);
    }

    public static Date parseDateTime(String str) {
        try {
            return new SimpleDateFormat(DATE_TIME_FORMAT).parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDateOnly(String str) {
        try {
            return new SimpleDateFormat(DATE_ONLY_FORMAT).parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
