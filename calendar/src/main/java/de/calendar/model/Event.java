package de.calendar.model;

import de.calendar.utils.CalendarUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created Event in de.calendar
 * by ARSTULKE on 19.01.2017.
 */
@Entity
@Table(name = "appointment")
public class Event {

    public static final DateTimeFormatter DATETIME_FORMAT = dateFormat("dd.MM.yyyy HH:mm");

    private static DateTimeFormatter dateFormat(String s) {
        return DateTimeFormatter.ofPattern(s).withLocale(Locale.GERMANY);
    }

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Size(min = 10, max = 16)
    @Column(name = "start_event")
    private String start;

    @Size(min = 10, max = 16)
    @Column(name = "end_event")
    private String end;

    public Event() {
    }

    public Event(String title, String start, String end) {
        this(title, CalendarUtils.parse(start), CalendarUtils.parse(end));
    }

    public Event(String title, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.start = CalendarUtils.format(start);
        this.end = CalendarUtils.format(end);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartString(String start) {
        setStart(CalendarUtils.parse(start));
    }

    public void setEndString(String end) {
        setEnd(CalendarUtils.parse(end));
    }

    public void setStart(LocalDateTime start) {
        this.start = CalendarUtils.format(start);
    }

    public void setEnd(LocalDateTime end) {
        this.end = CalendarUtils.format(end);
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return CalendarUtils.parse(start);
    }

    public LocalDateTime getEnd() {
        return CalendarUtils.parse(end);
    }

    public String getStartString() {
        return start;
    }

    public String getEndString() {
        return end;
    }

    public Long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
}
