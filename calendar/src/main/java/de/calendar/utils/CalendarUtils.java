package de.calendar.utils;

import de.calendar.model.Event;

import java.time.LocalDateTime;

/**
 * Created CalendarUtils in de.calendar.utils
 * by ARSTULKE on 20.01.2017.
 */
public class CalendarUtils {
    public static boolean isEventBetween(Event event, LocalDateTime from, LocalDateTime until) {
        boolean startAfter = event.getStart().isAfter(from);
        boolean startBefore = event.getStart().isBefore(until);
        boolean startEquals = event.getStart().equals(from) || event.getStart().equals(until);

        boolean endAfter = event.getEnd().isAfter(from);
        boolean endBefore = event.getEnd().isBefore(until);
        boolean endEquals = event.getEnd().equals(from) || event.getEnd().equals(until);


        boolean startBetween = (startAfter && startBefore) || startEquals;
        boolean endBetween = (endAfter && endBefore) || endEquals;

        return startBetween || endBetween;
    }

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(Event.DATETIME_FORMAT);
    }

    public static LocalDateTime parse(String string) {
        try {
            return LocalDateTime.parse(string, Event.DATETIME_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
}
