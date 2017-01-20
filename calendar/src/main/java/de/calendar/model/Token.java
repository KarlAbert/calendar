package de.calendar.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created Token in de.calendar.model
 * by ARSTULKE on 19.01.2017.
 */
@Embeddable
public class Token {

    @Size(min = 20)
    @Column(unique = true)
    private String value;
    private LocalDateTime expiringDate;

    public Token() {
    }

    public Token(String value, LocalDateTime expiringDate) {
        this.value = value;
        this.expiringDate = expiringDate;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getExpiringDate() {
        return expiringDate;
    }

    public boolean isExpired() {
        return expiringDate.isBefore(LocalDateTime.now());
    }
}
