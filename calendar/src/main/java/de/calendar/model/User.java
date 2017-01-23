package de.calendar.model;

import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created User in de.calendar.model
 * by ARSTULKE on 19.01.2017.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3, max = 50)
    private String firstname;

    @Size(min = 3, max = 50)
    private String lastname;

    @Column(unique = true)
    @Size(min = 3, max = 50)
    private String username;

    @Column(unique = true)
    private String email;

    @Size(min = 8)
    private String password;

    @Embedded
    private Token token;

    @ManyToMany(targetEntity = Event.class, cascade = CascadeType.ALL)
    private Set<Event> events;

    public User() {
    }

    public User(String firstname, String lastname, String username, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;

        this.events = new HashSet<>();
    }

    public User(JSONObject data) {
        this(
                data.getString("firstname"),
                data.getString("lastname"),
                data.getString("username"),
                data.getString("email"),
                data.getString("password")
        );
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Set<Event> getEvents() {
        return events;
    }
}
