package de.calendar.utils;

import de.calendar.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parse(String string) {
        try {
            return LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime[] formatStartAndEnd(String startString, String endString) {
        if (startString != null && !startString.contains("T")) {
            startString += "T00:00:00";
        }
        if (endString != null && !endString.contains("T")) {
            endString += "T23:59:59";
        }

        LocalDateTime startTMP = parse(startString);
        LocalDateTime endTMP = parse(endString);
        LocalDateTime start, end;
        if (startTMP == null && endTMP != null) {
            start = endTMP.minusHours(1L);
            end = endTMP;
        } else if (startTMP != null && endTMP == null) {
            start = startTMP;
            end = startTMP.plusHours(1L);
        } else {
            //noinspection ConstantConditions
            if (startTMP.isAfter(endTMP)) {
                start = endTMP;
                end = startTMP;
            } else {
                start = startTMP;
                end = endTMP;
            }
        }

        return new LocalDateTime[]{start, end};
    }
}
