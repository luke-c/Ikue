package com.ikue.japanesedictionary.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// Get the difference in hours between two dates.
public class DateTimeUtils {
    public static long getDifferenceInDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
