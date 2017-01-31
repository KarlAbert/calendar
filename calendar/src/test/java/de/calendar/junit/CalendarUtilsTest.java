package de.calendar.junit;

import de.calendar.model.Event;
import de.calendar.utils.CalendarUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static de.calendar.CalendarTestUtils.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

/**
 * Created CalendarUtilsTest in de.calendar.junit
 * by ARSTULKE on 20.01.2017.
 */
public class CalendarUtilsTest {
    /**
     * Spezifiziert das Verhalten von {@link de.calendar.utils.CalendarUtils#isEventBetween(Event, LocalDateTime, LocalDateTime)}.
     */
    @Test
    public void isEventBetween() {
        //given
        Event event = new Event("Test", parse("21.01.2017 12:00"), parse("21.01.2017 15:00"));
        LocalDateTime from = parse("21.01.2017 10:00");
        LocalDateTime until = parse("21.01.2017 12:00");

        //when
        boolean between = CalendarUtils.isEventBetween(event, from, until);

        //then
        String msg = String.format("Event isn't between %s and %s.", CalendarUtils.format(from), CalendarUtils.format(until));
        assertTrue(msg, between);
    }

    /**
     * Spezifiziert das Verhalten von {@link de.calendar.utils.CalendarUtils#format(LocalDateTime)} und
     *  von {@link de.calendar.utils.CalendarUtils#parse(String)}.
     */
    @Test
    public void formatsAndParses() {
        //given
        int year = 2017;
        int month = 1;
        int dayOfMonth = 20;
        int hour = 22;
        int minute = 30;
        int second = 0;
        LocalDateTime datetime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        //when
        String out = CalendarUtils.format(datetime);
        LocalDateTime converted = CalendarUtils.parse(out);

        //then
        Assert.assertThat(converted, is(datetime));
    }
}
