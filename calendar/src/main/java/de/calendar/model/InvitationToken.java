package de.calendar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * calendar:
 * * de.calendar.model:
 * * * Created by KAABERT on 23.01.2017.
 */
@Entity
public class InvitationToken {
    @Id
    @GeneratedValue
    private Long id;

    private String value;

    public InvitationToken() {
    }

    public InvitationToken(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvitationToken that = (InvitationToken) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
