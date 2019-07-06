package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;

public class DateUtils {
    static final DateTimeFormatter accessLogDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    static final DateTimeFormatter argumentDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    public static LocalDateTime parseAccessLogDate(String localDateTime) {
        return LocalDateTime.parse(localDateTime, accessLogDateFormatter);
    }

    public static LocalDateTime add(String localDateTime, int amount, TemporalUnit unit) {
        return parseDateArgument(localDateTime).plus(amount, unit);
    }

    public static LocalDateTime parseDateArgument(String dateTime) {
        return LocalDateTime.parse(dateTime, argumentDateFormatter);
    }
}
