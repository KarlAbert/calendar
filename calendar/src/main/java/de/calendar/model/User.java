package de.calendar.model;

import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

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

    public User() {
    }

    public User(JSONObject data) {
        this.firstname = data.getString("firstname");
        this.lastname = data.getString("lastname");
        this.username = data.getString("username");
        this.email = data.getString("email");
        this.password = data.getString("password");
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
}
