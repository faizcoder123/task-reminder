package com.taskreminder.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String MESSAGE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String convertToEsDateFormat(ZonedDateTime zonedDateTime) {
        if(zonedDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MESSAGE_DATE_FORMAT);
            return zonedDateTime.format(formatter);
        }
        return null;
    }
}
