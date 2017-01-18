package de.calendar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created Event in de.calendar
 * by ARSTULKE on 19.01.2017.
 */
@Entity
public class Event {

    public static final DateTimeFormatter DATE_FORMAT_DAYLONG = dateFormat("dd.mm.yyyy");
    public static final DateTimeFormatter DATE_FORMAT_TIMESPAN = dateFormat("dd.mm.yyyy HH:MM");

    private static DateTimeFormatter dateFormat(String s) {
        return DateTimeFormatter.ofPattern(s).withLocale(Locale.GERMANY);
    }

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private LocalDateTime from;
    private LocalDateTime until;

    public Event() {
    }

    public Event(String title, String from, String until) {
        this.title = title;
        this.from = parse(from);
        this.until = parse(until);
    }

    public Event(String title, LocalDateTime from, LocalDateTime until) {
        this.title = title;
        this.from = from;
        this.until = until;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFrom(String from) {
        this.from = parse(from);
    }

    public void setUntil(String until) {
        this.until = parse(until);
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public static LocalDateTime parse(String string) {
        return null;
    }

    public Long getID() {
        return id;
    }
}
