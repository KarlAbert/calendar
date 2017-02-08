/**
 * This file was generated by the JPA Modeler
 */

package de.calendar.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static de.calendar.utils.CalendarUtils.format;
import static de.calendar.utils.CalendarUtils.parse;

/**
 * Hier werden die Events gespeichert.
 *
 * @author KAABERT
 */

@Entity
public class Event implements Serializable {

    @Deprecated
    public Event() {
    }

    public Event(String title, String start, String end) {
        this.title = title;
        setStart(parse(start));
        setEnd(parse(end));

        this.owner = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

    public Event(String title, LocalDateTime start, LocalDateTime end, User owner) {
        this.title = title;
        setStart(start);
        setEnd(end);

        this.owner = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.invitations = new ArrayList<>();

        this.owner.add(owner);
    }

    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String start;

    @Basic
    private String end;

    @Basic
    @Size(min = 3, max = 56)
    private String title;

    @OneToMany(targetEntity = Invitation.class)
    private List<Invitation> invitations;

    @ManyToMany(targetEntity = User.class)
    private List<User> owner;

    @ManyToMany(targetEntity = User.class, mappedBy = "subscriptions")
    private List<User> subscribers;

    public Event(String title, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.start = format(start);
        this.end = format(end);

        this.invitations = new ArrayList<>();
        this.owner = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return parse(this.start);
    }

    public String getStartString() {
        LocalDateTime start = parse(this.start);

        if (start == null) {
            return null;
        } else if (start.getHour() == 0 && start.getMinute() == 0 && start.getSecond() == 0) {
            return start.toString().split("T")[0];
        }

        return start.toString();
    }

    public void setStart(LocalDateTime start) {
        this.start = format(start);
    }

    public LocalDateTime getEnd() {
        return parse(this.end);
    }

    public String getEndString() {
        LocalDateTime end = parse(this.end);

        if (end == null) {
            return null;
        } else if (end.getHour() == 23 && end.getMinute() == 59 && end.getSecond() == 59) {
            return end.toString().split("T")[0];
        }

        return end.toString();
    }

    public void setEnd(LocalDateTime end) {
        this.end = format(end);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Invitation> getInvitations() {
        return this.invitations;
    }

    public List<User> getOwner() {
        return this.owner;
    }

    public List<User> getSubscribers() {
        return this.subscribers;
    }

    @Override
    public String toString() {
        return "Event{" + " id=" + id + ", start=" + start + ", end=" + end + ", title=" + title + ", owner=" + owner + ", subscribers=" + subscribers + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!start.equals(event.start)) return false;
        if (!end.equals(event.end)) return false;
        if (!title.equals(event.title)) return false;
        return invitations.equals(event.invitations);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + invitations.hashCode();
        return result;
    }
}